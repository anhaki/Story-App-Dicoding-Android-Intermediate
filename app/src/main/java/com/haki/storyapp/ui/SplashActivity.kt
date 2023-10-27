package com.haki.storyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.haki.storyapp.R
import com.haki.storyapp.ui.viewModel.SplashViewModel
import com.haki.storyapp.ui.viewModel.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            var intent = Intent(this, WelcomeActivity::class.java)
            viewModel.getSession().observe(this) { user ->
                if (user.isLogin) {
                    intent = Intent(this, MainActivity::class.java)
                }
            }

            startActivity(intent)
            finish()
        }, SPLASH_TIME)

    }

    companion object {
        const val SPLASH_TIME = 3000L
    }
}