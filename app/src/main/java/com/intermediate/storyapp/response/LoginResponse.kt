package com.intermediate.storyapp.response

import com.google.gson.annotations.SerializedName
import com.intermediate.storyapp.model.Login

data class LoginResponse (
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("loginResult")
    val loginResult: Login?,

)