package com.example.storyapp.fragments.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.Register
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun postRegister(name: String, email: String, password: String) {
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().postRegister(name, email, password)
        client.enqueue(object : Callback<Register> {
            override fun onResponse(
                call: Call<Register>,
                response: Response<Register>
            ) {
                _isLoading.postValue(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.e(TAG, "onSuccess : ${response.message()}")
                    _isSuccess.postValue(true)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isSuccess.postValue(false)
                }
            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }
        })
    }

    companion object{
        const val TAG = "RegisterViewModel"
    }
}