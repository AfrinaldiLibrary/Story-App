package com.example.storyapp.fragments.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.storyapp.R
import com.example.storyapp.activities.MainActivity
import com.example.storyapp.data.PrefManager
import com.example.storyapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefManager: PrefManager
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setButton()
        inputListener()
        loginCheck()

    }

    private fun init() {
        prefManager = PrefManager(requireContext())
    }

    private fun inputListener() {
        binding.apply {
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setButton()
                }

                override fun afterTextChanged(s: Editable) {

                }
            })

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setButton()
                }

                override fun afterTextChanged(s: Editable) {

                }
            })
        }
    }

    private fun setButton() {
        binding.apply {
            btnLogin.isEnabled =
                etPassword.text!!.length >= 6 && Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString())
                    .matches()
        }
    }

    private fun loginCheck() {
        binding.btnLogin.setOnClickListener {
            loginViewModel.response.observe(viewLifecycleOwner) {
                prefManager.setToken(it.token)
                prefManager.setLoggin(true)
            }
            loginViewModel.postLogin(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        loginViewModel.isLoading.observe(viewLifecycleOwner) {
            showsLoading(it)
        }

        loginViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(binding.root, R.string.login_success, Snackbar.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    activity?.let {
                        val intent = Intent(it, MainActivity::class.java)
                        it.startActivity(intent)
                        it.finish()
                    }
                }, 1000)
            } else Snackbar.make(binding.root, R.string.login_fail, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        if (prefManager.isLogin()!!) {
            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showsLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}