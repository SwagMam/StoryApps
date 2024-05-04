package com.intermediate.storyapp.model.paging

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.intermediate.storyapp.api.ApiConfig
import com.intermediate.storyapp.helper.UserPreferences

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiClient()
        return StoryRepository.getInstance(preferences, apiService)
    }
}