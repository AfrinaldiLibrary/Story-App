package com.example.storyapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.storyapp.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { view ->
            postRegister(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etPassword.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun postRegister(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().postRegister(name, email, password)
        client.enqueue(object : Callback<Register> {
            override fun onResponse(
                call: Call<Register>,
                response: Response<Register>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Toast.makeText(applicationContext,"success brow",Toast.LENGTH_SHORT).show()
                    Log.e("success", "data masuk")
                } else {
                    Log.e("error2", "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<Register>, t: Throwable) {
                Log.e("error", "onFailure: ${t.message}")
            }
        })
    }
}