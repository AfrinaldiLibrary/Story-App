package com.example.storyapp.activities.stories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.activities.LoginActivity
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.data.PrefManager
import com.example.storyapp.databinding.ActivityStoryBinding

class StoryActivity : AppCompatActivity() {
    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefManager: PrefManager
    private val adapter: StoryAdapter by lazy {
        StoryAdapter()
    }

    private val storyViewModel: StoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        checkLogin()
        getStories()
    }

    private fun getStories() {
        storyViewModel.stories.observe(this@StoryActivity){
            if (it != null){
                val token = prefManager.getToken().toString()
                storyViewModel.showStories(token)
                adapter.setList(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                prefManager.removeData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkLogin() {
        if (prefManager.isLogin() == false){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun init(){
        prefManager = PrefManager(this)
    }
}