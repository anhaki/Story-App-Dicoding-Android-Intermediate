package com.haki.storyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import com.haki.storyapp.repo.Repository
import java.io.File

class UploadViewModel(private val repository: Repository) : ViewModel() {
    fun upload(file: File, description: String) = repository.upload(file, description)
}