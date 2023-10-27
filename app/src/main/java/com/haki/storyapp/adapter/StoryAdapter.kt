package com.haki.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haki.storyapp.ui.DetailActivity
import com.haki.storyapp.databinding.StoryItemBinding
import com.haki.storyapp.response.ListStoryItem

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ListViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(storiesList: ListStoryItem) {
            binding.ivFoto.loadImage(storiesList.photoUrl)
            binding.tvName.text = storiesList.name

            itemView.setOnClickListener {
                val storyId = storiesList.id
                val toDetail = Intent(binding.root.context, DetailActivity::class.java)
                toDetail.putExtra(DetailActivity.ID_STORY, storyId)
                binding.root.context.startActivity(toDetail)
            }
        }

        private fun ImageView.loadImage(url: String) {
            Glide.with(this.context)
                .load(url)
                .centerCrop()
                .into(this)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}