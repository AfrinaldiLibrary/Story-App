package com.example.storyapp.fragments.stories

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.data.StoriesRepository
import com.example.storyapp.di.Injection

class StoriesViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    fun showStories(token: String): LiveData<PagingData<ListStoryItem>>{
        return storiesRepository.getAllStories(token).cachedIn(viewModelScope)
    }
}

class ViewModelFactory(private val ctx : Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoriesViewModel(Injection.provideRepository(ctx)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}