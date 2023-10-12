package com.haki.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.haki.storyapp.databinding.ActivityWelcomeBinding


class WelcomeActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            binding.btnLogin -> {
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            binding.btnSignUp -> {
                val intent = Intent(this@WelcomeActivity, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }
}