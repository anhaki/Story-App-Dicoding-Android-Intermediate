package com.haki.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haki.storyapp.ui.DetailActivity
import com.haki.storyapp.databinding.StoryItemBinding
import com.haki.storyapp.response.ListStoryItem

class StoryAdapter(private val listStories: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStories.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val stories = listStories[position]
        holder.bind(stories)
    }

    class ListViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storiesList: ListStoryItem) {
            Glide.with(binding.root.context)
                .load(storiesList.photoUrl)
                .into(binding.ivFoto)
            binding.tvName.text = storiesList.name

            itemView.setOnClickListener {
                val storyId = storiesList.id
                val toDetail = Intent(binding.root.context, DetailActivity::class.java)
                toDetail.putExtra(DetailActivity.ID_STORY, storyId)
                binding.root.context.startActivity(toDetail)
            }
        }

    }


}