package com.example.storyapp.fragments.stories

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.transition.ChangeBounds
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.storyapp.R
import com.example.storyapp.activities.LoginActivity
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.data.PrefManager
import com.example.storyapp.databinding.FragmentStoriesBinding

class StoriesFragment : Fragment() {
    private var _binding : FragmentStoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefManager: PrefManager
    private val adapter: StoryAdapter by lazy {
        StoryAdapter()
    }

    private val storiesViewModel : StoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.stories_page)
        postponeEnterTransition()

        setHasOptionsMenu(true)
        init()
        checkLogin()
        getStories()
        uploadButton()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = ChangeBounds()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                activity?.let {
                    prefManager.removeData()
                    val intent = Intent(it, LoginActivity::class.java)
                    it.startActivity(intent)
                    it.finish()
                }
            }
            R.id.menu_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getStories() {
        val token = prefManager.getToken().toString()
        storiesViewModel.showStories(token)
        storiesViewModel.stories.observe(viewLifecycleOwner){
            if (it != null){
                adapter.setList(it)
                showStories()

                view?.doOnPreDraw {
                    startPostponedEnterTransition()
                }

            }
        }

        storiesViewModel.isLoading.observe(viewLifecycleOwner) {
            showProgressBar(it)
        }
    }

    private fun showStories() {
        binding.apply {
            rvStory.setHasFixedSize(true)
            rvStory.adapter = adapter
//            adapter.setOnItemClickCallback(object: StoryAdapter.OnItemClickCallback{
//                override fun onItemClick(stories: ListStoryItem) {
//                    val toDetailFragment = StoriesFragmentDirections.actionStoriesFragmentToDetailFragment()
//                    toDetailFragment.name = stories.name
//                    toDetailFragment.date = stories.createdAt
//                    toDetailFragment.description = stories.description
//                    toDetailFragment.photo = stories.photoUrl
//                    view?.findNavController()?.navigate(toDetailFragment)
//                }
//            })
        }
    }

    private fun uploadButton() {
        binding.fabAdd.setOnClickListener{
            val toUploadFileFragment = StoriesFragmentDirections.actionStoriesFragmentToUploadFileFragment()

            view?.findNavController()?.navigate(toUploadFileFragment)
        }
    }


    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun checkLogin() {
        if (prefManager.isLogin() == false){
            activity?.let {
                val intent = Intent(it, LoginActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
        }
    }

    private fun init(){
        prefManager = PrefManager(requireContext())
    }
}