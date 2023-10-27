package com.haki.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.haki.storyapp.ui.viewModel.ViewModelFactory
import com.haki.storyapp.customView.MyButton
import com.haki.storyapp.customView.MyEmailEditText
import com.haki.storyapp.customView.MyPwEditText
import com.haki.storyapp.databinding.ActivityLoginBinding
import com.haki.storyapp.pref.UserModel
import com.haki.storyapp.repo.ResultState
import com.haki.storyapp.R
import com.haki.storyapp.ui.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var txEmail: String
    private lateinit var txPassword: String

    private lateinit var myPwEditText: MyPwEditText
    private lateinit var myEmailEditText: MyEmailEditText
    private lateinit var myButton: MyButton

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
        binding.askSign.setOnClickListener(this)

        myPwEditText = binding.etPass
        myButton = binding.btnLogin
        myEmailEditText = binding.etEmail

        myEmailEditText.doOnTextChanged { _, _, _, _ ->
            setMyButton(true)
        }

        myPwEditText.doOnTextChanged { _, _, _, _ ->
            setMyButton(true)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        })

    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnLogin -> {
                login()
            }

            binding.askSign -> {
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun login() {
        txEmail = binding.etEmail.text.toString()
        txPassword = binding.etPass.text.toString()

        viewModel.login(txEmail, txPassword).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        setMyButton(false)
                        btnLoad(true)
                    }

                    is ResultState.Success -> {
                        viewModel.saveUserSession(
                            UserModel(
                                result.data.loginResult.name,
                                result.data.loginResult.token
                            )
                        )
                        setMyButton(true)
                        btnLoad(false)

                        showToast(result.data.message)

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is ResultState.Error -> {
                        setMyButton(true)
                        btnLoad(false)
                        showSnackBar(result.error)
                    }
                }
            }
        }
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.close)) { }
            .setActionTextColor(getColor(R.color.secCol))
            .show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun setMyButton(isEnable: Boolean) {
        val result = myPwEditText.text
        if (isEnable) {
            if (result != null) {
                myButton.isEnabled = result.length > 7 && MyEmailEditText.isValid
            }
        } else myButton.isEnabled = false
    }

    private fun btnLoad(isEnable: Boolean) {
        if (isEnable) {
            binding.progressBar.visibility = View.VISIBLE
            myButton.text = ""
        } else {
            binding.progressBar.visibility = View.GONE
            myButton.text = getString(R.string.login)
        }
    }
}



