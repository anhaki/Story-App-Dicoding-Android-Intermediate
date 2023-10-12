package com.haki.storyapp.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.haki.storyapp.ui.viewModel.ViewModelFactory
import com.haki.storyapp.customView.MyButton
import com.haki.storyapp.customView.MyEmailEditText
import com.haki.storyapp.customView.MyPwEditText
import com.haki.storyapp.databinding.ActivitySignupBinding
import com.haki.storyapp.repo.ResultState
import com.haki.storyapp.ui.viewModel.SignUpViewModel
import com.haki.storyapp.R

class SignupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var txName: String
    private lateinit var txEmail: String
    private lateinit var txPassword: String

    private lateinit var myPwEditText: MyPwEditText
    private lateinit var myEmailEditText: MyEmailEditText
    private lateinit var myButton: MyButton

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this, true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSign.setOnClickListener(this)
        binding.askLog.setOnClickListener(this)

        myEmailEditText = binding.etEmail
        myPwEditText = binding.etPass
        myButton = binding.btnSign

        myEmailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButton(true)
            }

            override fun afterTextChanged(s: Editable?) {
                //Do Nothing
            }

        })
        myPwEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButton(true)
            }

            override fun afterTextChanged(s: Editable?) {
                //Do Nothing
            }

        })
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnSign -> {
                signUp()
            }

            binding.askLog -> {
                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signUp() {
        txName = binding.etNama.text.toString()
        txEmail = binding.etEmail.text.toString()
        txPassword = binding.etPass.text.toString()

        viewModel.signUp(txName, txEmail, txPassword).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        setMyButton(false)
                        btnLoad(true)
                    }

                    is ResultState.Success -> {
                        setMyButton(true)
                        btnLoad(false)
                        showToast(result.data.message)
                        Log.d("pesan", result.data.message)
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }

                    is ResultState.Error -> {
                        setMyButton(true)
                        btnLoad(false)
                        showSnackBar(result.error)
                        Log.d("pesan", result.error)
                    }
                }
            }
        }
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
            .setAction("Close") { }
            .setActionTextColor(Color.parseColor("#f99932"))
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

    private fun btnLoad(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            myButton.text = ""
        } else {
            binding.progressBar.visibility = View.GONE
            myButton.text = getString(R.string.sign_up)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignupActivity, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}