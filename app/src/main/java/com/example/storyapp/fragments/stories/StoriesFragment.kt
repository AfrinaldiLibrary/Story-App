package com.example.storyapp.fragments.stories

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.example.storyapp.R
import com.example.storyapp.activities.LoginActivity
import com.example.storyapp.adapter.LoadingStateAdapter
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.data.PrefManager
import com.example.storyapp.databinding.FragmentStoriesBinding
import com.example.storyapp.databinding.StoryCardBinding
import com.google.android.material.snackbar.Snackbar

class StoriesFragment : Fragment() {
    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefManager: PrefManager
    private val adapter: StoryAdapter by lazy {
        StoryAdapter()
    }

    private val storiesViewModel: StoriesViewModel by viewModels{
        ViewModelFactory(requireContext())
    }

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

        setHasOptionsMenu(true)
        init()
        checkLogin()
        getData()
        showStories()
        uploadButton()
    }

    private fun getData() {
        showProgressBar(true)
        postponeEnterTransition()
        val token = prefManager.getToken().toString()
        storiesViewModel.showStories(token).observe(viewLifecycleOwner){
            if (it != null){
                showProgressBar(false)
                adapter.submitData(lifecycle, it)
            } else{
                showProgressBar(false)
                Snackbar.make(binding.root, R.string.data_failed, Snackbar.LENGTH_SHORT).show()
            }
        }

        view?.doOnPreDraw {
            startPostponedEnterTransition()
        }
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
            R.id.menu_map -> {
                view?.findNavController()?.navigate(R.id.action_storiesFragment_to_mapFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showStories() {
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        binding.apply {
            rvStory.setHasFixedSize(true)
            adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
                override fun onItemClick(stories: ListStoryItem, storyCard: StoryCardBinding) {
                    val extras = FragmentNavigatorExtras(
                        Pair(storyCard.tvName, stories.name),
                        Pair(storyCard.tvDate, stories.createdAt),
                        Pair(storyCard.tvDesc, stories.description),
                        Pair(storyCard.ivPhoto, stories.photoUrl)
                    )

                    val toDetailFragment = StoriesFragmentDirections.actionStoriesFragmentToDetailFragment()
                    toDetailFragment.name = stories.name
                    toDetailFragment.date = stories.createdAt
                    toDetailFragment.desc = stories.description
                    toDetailFragment.photo = stories.photoUrl

                    view?.findNavController()?.navigate(
                        toDetailFragment,
                        extras
                    )
                }

            })
        }
    }

    private fun uploadButton() {
        binding.fabAdd.setOnClickListener {
            val toUploadFileFragment =
                StoriesFragmentDirections.actionStoriesFragmentToUploadFileFragment()

            view?.findNavController()?.navigate(toUploadFileFragment)
        }
    }


    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun checkLogin() {
        if (prefManager.isLogin() == false) {
            activity?.let {
                val intent = Intent(it, LoginActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
        }
    }

    private fun init() {
        prefManager = PrefManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}