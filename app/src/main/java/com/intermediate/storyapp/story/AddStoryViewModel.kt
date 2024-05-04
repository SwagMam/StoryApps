package com.intermediate.storyapp.story


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.intermediate.storyapp.model.User
import com.intermediate.storyapp.model.paging.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody) = repository.addStory(token, file, description)

    fun getUser(): LiveData<User> {
        return repository.getUserData()
    }
}