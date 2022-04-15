package com.example.storyapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.Register
import com.example.storyapp.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener { view ->
            postRegister(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etPassword.text.toString())
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                    Log.e("success", "data masuk")
                    binding.etName.setText("")
                    binding.etEmail.setText("")
                    binding.etPassword.setText("")
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