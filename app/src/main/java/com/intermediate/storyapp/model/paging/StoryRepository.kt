package com.intermediate.storyapp.model.paging

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.intermediate.storyapp.api.ApiService
import com.intermediate.storyapp.helper.Status
import com.intermediate.storyapp.helper.UserPreferences
import com.intermediate.storyapp.model.Story
import com.intermediate.storyapp.model.User
import com.intermediate.storyapp.request.LoginRequest
import com.intermediate.storyapp.request.RegisterRequest
import com.intermediate.storyapp.response.BaseResponse
import com.intermediate.storyapp.response.LoginResponse
import com.intermediate.storyapp.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyapp")
class StoryRepository (private val pref: UserPreferences, private val apiService: ApiService) {

    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService, pref)
            }
        ).liveData
    }

    fun userLogin(email: String, password: String): LiveData<Status<LoginResponse>> = liveData {
        emit(Status.Loading)
        try {
            val response = apiService.login(LoginRequest(email, password))
            emit(Status.Success(response))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(Status.Error(e.message.toString()))
        }
    }



    fun userRegister(name: String, email: String, password: String): LiveData<Status<BaseResponse>> = liveData {
        emit(Status.Loading)
        try {
            val response = apiService.register(
                RegisterRequest(name, email, password)
            )
            emit(Status.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(Status.Error(e.message.toString()))
        }
    }

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Status<BaseResponse>> = liveData {
        emit(Status.Loading)
        try {
            val response = apiService.addStory(token, file, description)
            emit(Status.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(Status.Error(e.message.toString()))
        }
    }

    fun getUserData(): LiveData<User> {
        return pref.getUserData().asLiveData()
    }

    suspend fun saveUserData(user: User) {
        pref.saveUserData(user)
    }

    suspend fun login() {
        pref.login()
    }

    suspend fun logout() {
        pref.logout()
    }

    fun getStoryLocation(token: String): LiveData<Status<StoryResponse>> = liveData {
        emit(Status.Loading)
        try {
            val response = apiService.getStoryLocation(token, 1)
            emit(Status.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(Status.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: UserPreferences,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}