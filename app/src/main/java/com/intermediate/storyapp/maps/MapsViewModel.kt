package com.intermediate.storyapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.intermediate.storyapp.model.User
import com.intermediate.storyapp.model.paging.StoryRepository

class MapsViewModel(private val repository: StoryRepository): ViewModel() {

    fun getStoryLocation(token: String) =
        repository.getStoryLocation(token)

    fun getUser(): LiveData<User> {
        return repository.getUserData()
    }
}