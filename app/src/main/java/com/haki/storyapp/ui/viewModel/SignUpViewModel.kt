package com.haki.storyapp.ui.viewModel

import androidx.lifecycle.ViewModel
import com.haki.storyapp.repo.Repository

class SignUpViewModel(private val repository: Repository) : ViewModel() {
    fun signUp(nama: String, email: String, password: String) =
        repository.signUp(nama, email, password)
}