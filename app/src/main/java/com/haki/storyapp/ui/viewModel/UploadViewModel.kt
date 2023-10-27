package com.haki.storyapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haki.storyapp.pref.LocModel
import com.haki.storyapp.repo.Repository
import kotlinx.coroutines.launch
import java.io.File

class UploadViewModel(private val repository: Repository) : ViewModel() {
    fun upload(file: File, description: String, latitude: Double?, longitude: Double?) =
        repository.upload(file, description, latitude, longitude)

    fun saveLocSession(loc: LocModel) {
        viewModelScope.launch {
            repository.saveLocSession(loc)
        }
    }

    fun getLocSession(): LiveData<LocModel> {
        return repository.getLocSession().asLiveData()
    }

}