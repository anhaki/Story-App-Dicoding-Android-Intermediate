package com.haki.storyapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.haki.storyapp.R
import com.haki.storyapp.adapter.LoadingStateAdapter
import com.haki.storyapp.adapter.StoryAdapter
import com.haki.storyapp.ui.viewModel.MainViewModel
import com.haki.storyapp.ui.viewModel.ViewModelFactory
import com.haki.storyapp.databinding.ActivityMainBinding


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

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

    }

    private fun showStories() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }

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

    fun maps(menuItem: MenuItem) {
        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        startActivity(intent)
    }

}