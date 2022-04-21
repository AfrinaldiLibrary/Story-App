package com.example.storyapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}