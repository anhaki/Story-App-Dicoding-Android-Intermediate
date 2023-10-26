package com.haki.storyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import com.haki.storyapp.repo.Repository

class MapsViewModel(private val repository: Repository) : ViewModel() {
    fun stories() = repository.stories()
}