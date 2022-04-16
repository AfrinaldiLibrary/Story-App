package com.example.storyapp.fragments.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.storyapp.databinding.FragmentRegisterBinding
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton()
        inputListener()
        registerCheck()
    }

    private fun inputListener() {
        binding.apply {
            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    setButton()
                }

                override fun afterTextChanged(s: Editable) {

                }
            })

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
            btnRegister.isEnabled = etName.text!!.isNotEmpty() &&etPassword.text!!.length >= 6 && Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()
        }
    }

    private fun registerCheck() {
        binding.btnRegister.setOnClickListener {
            registerViewModel.postRegister(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        registerViewModel.isLoading.observe(viewLifecycleOwner) {
            showsLoading(it)
        }
    }

    private fun showsLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}