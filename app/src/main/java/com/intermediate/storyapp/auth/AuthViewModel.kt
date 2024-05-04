package com.intermediate.storyapp.auth


import androidx.lifecycle.*
import com.intermediate.storyapp.model.User
import com.intermediate.storyapp.model.paging.StoryRepository
import kotlinx.coroutines.launch


class AuthViewModel(private val repository: StoryRepository) : ViewModel() {

    fun userLogin(email: String, password: String) = repository.userLogin(email, password)

    fun userRegister(name: String, email: String, password: String) =
        repository.userRegister(name, email, password)

    fun saveUser(user: User) {
        viewModelScope.launch {
            repository.saveUserData(user)
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.login()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}