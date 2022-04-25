package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.StoryCardBinding
import com.example.storyapp.fragments.stories.StoriesFragmentDirections
import com.example.storyapp.helper.withDateFormat
import kotlin.collections.ArrayList

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private val list = ArrayList<ListStoryItem>()

    fun setList(stories: List<ListStoryItem>){
        list.clear()
        list.addAll(stories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class ListViewHolder(private val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(stories : ListStoryItem){
            binding.apply {
                tvName.text = stories.name
                tvDesc.text = stories.description
                tvDate.text = stories.createdAt.withDateFormat()
                Glide.with(binding.root)
                    .load(stories.photoUrl)
                    .centerCrop()
                    .into(ivPhoto)
            }
            ViewCompat.setTransitionName(binding.ivPhoto, "iv_photo")
            ViewCompat.setTransitionName(binding.tvDate, "tv_date")
            ViewCompat.setTransitionName(binding.tvName, "tv_name")
            ViewCompat.setTransitionName(binding.tvDesc, "tv_desc")

            val toDetailFragment = StoriesFragmentDirections.actionStoriesFragmentToDetailFragment()
            toDetailFragment.name = stories.name
            toDetailFragment.date = stories.createdAt
            toDetailFragment.description = stories.description
            toDetailFragment.photo = stories.photoUrl
            val extras = FragmentNavigatorExtras(
                Pair(binding.ivPhoto, "iv_photo"),
                Pair(binding.tvDate, "tv_date"),
                Pair(binding.tvName, "tv_name"),
                Pair(binding.tvDesc, "tv_desc")
            )

            binding.card.setOnClickListener{

                it?.findNavController()?.navigate(toDetailFragment, extras)
            }
        }
    }
}