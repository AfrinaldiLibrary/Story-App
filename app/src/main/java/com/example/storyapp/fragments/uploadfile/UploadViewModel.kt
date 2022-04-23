package com.example.storyapp.fragments.uploadfile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess : LiveData<Boolean> = _isSuccess

    fun upload(
        token : String,
        description : String,
        image : MultipartBody.Part
    ){
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().uploadStories("Bearer $token", image, description)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _isSuccess.postValue(true)
                        Log.e(TAG, "data berhasil di upload")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object{
        const val TAG = "UploadViewModel"
    }
}