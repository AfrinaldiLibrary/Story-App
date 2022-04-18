package com.example.storyapp.fragments.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.storyapp.activities.stories.StoryActivity
import com.example.storyapp.data.PrefManager
import com.example.storyapp.databinding.FragmentLoginBinding

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

    private fun init(){
        prefManager = PrefManager(requireContext())
    }

    private fun inputListener() {
        binding.apply {
            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setButton()
                }

                override fun afterTextChanged(s: Editable) {

                }
            })

            etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

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
            btnLogin.isEnabled = etPassword.text!!.length >= 6 && Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()
        }
    }

    private fun loginCheck() {
        binding.btnLogin.setOnClickListener {
            loginViewModel.response.observe(viewLifecycleOwner){
                prefManager.setToken(it.token)
                prefManager.setLoggin(true)
            }
            loginViewModel.postLogin(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        loginViewModel.isLoading.observe(viewLifecycleOwner) {
            showsLoading(it)
        }

        loginViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess){
                activity?.let{
                    val intent = Intent (it, StoryActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (prefManager.isLogin()!!){
            activity?.let{
                val intent = Intent (it, StoryActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun showsLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}