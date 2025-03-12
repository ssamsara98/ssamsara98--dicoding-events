package com.ssamsara98.dicodingevents.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssamsara98.dicodingevents.databinding.SearchEventItemBinding
import com.ssamsara98.dicodingevents.response.EventItem

class SearchEventItemAdapter :
    ListAdapter<EventItem, SearchEventItemAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventItem>() {
            override fun areItemsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SearchEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventItem = getItem(position)
        holder.bind(eventItem)
        holder.itemView.setOnClickListener { view ->
            this.onItemClickCallback.onItemClicked(view, eventItem)
        }
    }

    class ViewHolder(private val binding: SearchEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(eventItem: EventItem) {
            binding.apply {
                tvEventName.text = eventItem.name
                tvEventSummary.text = eventItem.summary
                Glide.with(ivMediaCover).load(eventItem.mediaCover).into(ivMediaCover)
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(view: View, data: EventItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}