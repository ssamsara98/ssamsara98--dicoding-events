package com.ssamsara98.dicodingevents.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity
import com.ssamsara98.dicodingevents.databinding.FinishedEventItemBinding

class FavoriteEventItemAdapter :
    ListAdapter<FavoriteEventEntity, FavoriteEventItemAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEventEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FinishedEventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventItem = this.getItem(position)
        holder.bind(eventItem)
        holder.itemView.setOnClickListener { view ->
            this.onItemClickCallback.onItemClicked(view, eventItem)
        }
    }

    class ViewHolder(private val binding: FinishedEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(eventItem: FavoriteEventEntity) {
            binding.apply {
                tvEventName.text = eventItem.name
                Glide.with(ivMediaCover).load(eventItem.imageLogo).into(ivMediaCover)
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(view: View, data: FavoriteEventEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}