package com.haki.storyapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.haki.storyapp.pref.UserModel
import com.haki.storyapp.repo.Repository
import com.haki.storyapp.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val story: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return repository.getUserSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}