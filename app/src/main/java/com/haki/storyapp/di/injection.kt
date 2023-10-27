package com.haki.storyapp.di

import android.content.Context
import com.haki.storyapp.apiService.ApiConfig
import com.haki.storyapp.database.StoryDatabase
import com.haki.storyapp.pref.UserPreference
import com.haki.storyapp.pref.dataStore
import com.haki.storyapp.repo.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository? {
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getUserSession().first().token
        }
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(token)
        return Repository.getInstance(apiService, pref, database, true)
    }

}
