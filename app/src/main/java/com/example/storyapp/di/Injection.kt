package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.database.StoriesDatabase

object Injection {
    fun provideRepository(ctx : Context): StoriesRepository{
        val database = StoriesDatabase.getDatabase(ctx)
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(database, apiService)
    }
}