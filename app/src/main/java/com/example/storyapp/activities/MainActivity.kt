package com.example.storyapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.fragments.stories.StoriesFragment

class MainActivity : AppCompatActivity() {
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mFragmentManager = supportFragmentManager
        val mStoriesFragment = StoriesFragment()
        val fragment = mFragmentManager.findFragmentByTag(StoriesFragment::class.java.simpleName)

        if (fragment !is StoriesFragment){
            mFragmentManager
                .beginTransaction()
                .add(R.id.stories_container, mStoriesFragment, StoriesFragment::class.java.simpleName)
                .commit()
        }
    }
}