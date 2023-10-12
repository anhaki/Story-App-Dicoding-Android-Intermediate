package com.haki.storyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import com.haki.storyapp.repo.Repository

class DetailViewModel(private val repository: Repository) : ViewModel()  {
    fun getDetail(id: String) = repository.detail(id)
}