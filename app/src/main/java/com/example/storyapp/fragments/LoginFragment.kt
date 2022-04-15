package com.example.storyapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.Login
import retrofit2.Callback
import com.example.storyapp.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener { view ->
            postLogin(binding.etEmail.text.toString(), binding.etPassword.text.toString())
//            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun postLogin(email: String, password: String) {
        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (!response.body()?.error!!) {
                    Log.e("success", "login berhasil")
                } else {
                    Log.e("error2", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e("error", "onFailure: ${t.message}")
            }
        })
    }
}