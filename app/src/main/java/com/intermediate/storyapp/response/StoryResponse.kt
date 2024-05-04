package com.intermediate.storyapp.response

import com.google.gson.annotations.SerializedName
import com.intermediate.storyapp.model.Story

data class StoryResponse(
    @field:SerializedName("error")
    val error: Boolean?,
    @field:SerializedName("message")
    val message: String?,
    @field:SerializedName("listStory")
    val listStory: List<Story>
)
