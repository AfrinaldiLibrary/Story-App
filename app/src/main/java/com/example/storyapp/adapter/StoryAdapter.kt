package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.databinding.StoryCardBinding
import com.example.storyapp.helper.withDateFormat
import kotlin.collections.ArrayList

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    private val list = ArrayList<ListStoryItem>()

    private lateinit var onItemClickCallback: OnItemClickCallback

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
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClick(list[holder.adapterPosition])
        }
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
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClick(stories : ListStoryItem)
    }
}