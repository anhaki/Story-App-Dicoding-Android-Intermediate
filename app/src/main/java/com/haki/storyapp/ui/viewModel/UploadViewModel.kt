package com.haki.storyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import com.haki.storyapp.repo.Repository
import java.io.File

class UploadViewModel(private val repository: Repository) : ViewModel() {
    fun upload(file: File, description: String, latitude: Double?, longitude: Double?) = repository.upload(file, description, latitude, longitude)
}