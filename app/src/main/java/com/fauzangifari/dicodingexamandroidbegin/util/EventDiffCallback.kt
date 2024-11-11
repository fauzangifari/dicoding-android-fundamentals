package com.fauzangifari.dicodingexamandroidbegin.util

import androidx.recyclerview.widget.DiffUtil
import com.fauzangifari.dicodingexamandroidbegin.data.local.entity.FavoriteEvent
import com.fauzangifari.dicodingexamandroidbegin.data.remote.response.ListEventsItem

class EventDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is ListEventsItem && newItem is ListEventsItem -> oldItem.id == newItem.id
            oldItem is FavoriteEvent && newItem is FavoriteEvent -> oldItem.id == newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is ListEventsItem && newItem is ListEventsItem -> oldItem.id == newItem.id
            oldItem is FavoriteEvent && newItem is FavoriteEvent -> oldItem.id == newItem.id
            else -> false
        }
    }
}