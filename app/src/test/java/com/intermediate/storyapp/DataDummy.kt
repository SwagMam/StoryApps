package com.intermediate.storyapp

import com.intermediate.storyapp.model.Login
import com.intermediate.storyapp.model.Story
import com.intermediate.storyapp.response.BaseResponse
import com.intermediate.storyapp.response.LoginResponse
import com.intermediate.storyapp.response.StoryResponse

object DataDummy {

    fun generateDummyStoryResponse(): List<Story> {

        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                id = "story-cKhWZzQE0Wy9px2S",
                name = "gus",
                description = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                photoUrl = "2023-05-09T19:10:23.510Z",
               createdAt = "B.I.P",
                lat = -6.1335033,
                lon = 106.64356
            )
            items.add(story)
        }
        return items
    }
}