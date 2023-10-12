package com.haki.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.haki.storyapp.R
import com.haki.storyapp.ui.viewModel.MainViewModel
import com.haki.storyapp.ui.viewModel.ViewModelFactory
import com.haki.storyapp.adapter.StoryAdapter
import com.haki.storyapp.databinding.ActivityMainBinding
import com.haki.storyapp.repo.ResultState


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)


        viewModel.getSession().observe(this) { user ->
            binding.topAppBar.title = getString(R.string.main_title, user.name)
        }


        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
        }

        showStories()
    }

    override fun onResume() {
        super.onResume()
        viewModel.stories()
    }

    private fun showStories() {

        viewModel.stories().observe(this) { result ->
            if (result != null) {

                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val adapter = StoryAdapter(result.data.listStory)
                        binding.rvStory.adapter = adapter
                    }

                    is ResultState.Error -> {
                        viewModel.getSession().observe(this) { user ->
                            showSnackBar(user.token)
                        }

                        binding.topAppBar.title
                        showLoading(false)
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


    fun logOut(menuItem: MenuItem) {
        viewModel.logout()
        val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun language(menuItem: MenuItem) {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun showLoading(isLoad: Boolean) {
        if (isLoad) {
            binding.progress.visibility = View.VISIBLE
            binding.rvStory.visibility = View.INVISIBLE
        } else {
            binding.progress.visibility = View.GONE
            binding.rvStory.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}