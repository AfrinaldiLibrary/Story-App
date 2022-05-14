package com.example.storyapp.fragments.map

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

class MapViewModel : ViewModel() {

    val storyList = MutableLiveData<List<ListStoryItem>>()

    fun getLocation() : LiveData<List<ListStoryItem>> { return storyList }

    fun loadStoryLocationData(token: String) {
        val client = ApiConfig.getApiService().getStoryLocation("Bearer $token")
        client.enqueue(object : Callback<DicodingStoryResponse> {
            override fun onResponse(call: Call<DicodingStoryResponse>, response: Response<DicodingStoryResponse>) {
                if (response.isSuccessful) {
                    Log.e(TAG, "success")
                    storyList.postValue(response.body()?.listStory)
                } else {
                    Log.e(TAG, "data gagal")
                }
            }

            override fun onFailure(call: Call<DicodingStoryResponse>, t: Throwable) {
                Log.e(TAG, "${t.message}")
            }
        })
    }

    companion object{
        const val TAG = "MapViewModel"
    }
}