package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.R
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.StoryCardBinding
import com.example.storyapp.helper.withDateFormat

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val stories = getItem(position)
        if (stories != null) {
            holder.bind(stories)
        }

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClick(stories!!, holder.binding)
        }
    }

    inner class ListViewHolder(val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(stories : ListStoryItem){
            binding.apply {
                tvName.text = stories.name
                tvDesc.text = stories.description
                tvDate.text = stories.createdAt.withDateFormat()
                Glide.with(binding.root)
                    .load(stories.photoUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_image)
                    )
                    .centerCrop()
                    .into(ivPhoto)

                tvName.transitionName = stories.name
                tvDate.transitionName = stories.createdAt
                tvDesc.transitionName = stories.description
                ivPhoto.transitionName = stories.photoUrl
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClick(stories : ListStoryItem, storyCard: StoryCardBinding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}