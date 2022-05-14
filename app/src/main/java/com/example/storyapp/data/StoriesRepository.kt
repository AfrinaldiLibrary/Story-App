package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.database.StoriesDatabase

class StoriesRepository(private val storiesDatabase: StoriesDatabase, private val apiService: ApiService) {
    fun getAllStories(token : String): LiveData<PagingData<ListStoryItem>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoriesRemoteMediator(storiesDatabase, apiService, token),
            pagingSourceFactory = {
                storiesDatabase.storiesDao().getAllStories()
            }
        ).liveData
    }
}