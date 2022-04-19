package com.example.storyapp.activities.stories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.DicodingStoryResponse
import com.example.storyapp.api.ListStoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun showStories(token: String) {
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getAllStories("Bearer $token")
        client.enqueue(object : Callback<DicodingStoryResponse> {
            override fun onResponse(
                call: Call<DicodingStoryResponse>,
                response: Response<DicodingStoryResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.e(TAG, "data tersedia")
                        _stories.postValue(response.body()?.listStory)

                    }
                }
            }

            override fun onFailure(call: Call<DicodingStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val TAG = "StoryViewModel"
    }
}