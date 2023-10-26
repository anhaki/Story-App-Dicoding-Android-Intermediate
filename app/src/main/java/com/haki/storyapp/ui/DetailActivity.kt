package com.haki.storyapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.haki.storyapp.R
import com.haki.storyapp.databinding.ActivityDetailBinding
import com.haki.storyapp.repo.ResultState
import com.haki.storyapp.ui.viewModel.DetailViewModel
import com.haki.storyapp.ui.viewModel.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        showDetail()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun showDetail() {
        intent.getStringExtra(ID_STORY)?.let {
            viewModel.getDetail(it).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showLoading(false)
                            Glide.with(binding.root.context)
                                .load(result.data.story.photoUrl)
                                .into(binding.ivFoto)
                            binding.tvNama.text = result.data.story.name
                            binding.tvDesk.text = result.data.story.description
                        }

                        is ResultState.Error -> {
                            showLoading(false)
                            showSnackBar(result.error)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoad: Boolean) {
        binding.progress.isVisible = isLoad
        binding.cardFoto.isVisible = !isLoad
        binding.llPertama.isVisible = !isLoad
        binding.tvDesk.isVisible = !isLoad
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.close)) { }
            .setActionTextColor(getColor(R.color.secCol))
            .show()
    }

    companion object {
        var ID_STORY = "ID_STORY"
    }
}