package com.intermediate.storyapp.model

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("userId")
    val userId: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("token")
    val token: String?,
)
