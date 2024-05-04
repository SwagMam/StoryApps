package com.intermediate.storyapp.story


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.intermediate.storyapp.model.Story
import com.intermediate.storyapp.model.User
import com.intermediate.storyapp.model.paging.StoryRepository



class MainViewModel(private val repository: StoryRepository) : ViewModel(){

    fun getStory(): LiveData<PagingData<Story>> {
        return  repository.getStory().cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<User> {
        return repository.getUserData()
    }
}