package com.example.android.uamp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.uamp.MediaItemData.Companion.PLAYBACK_RES_CHANGED
import com.example.android.uamp.databinding.FragmentMediaitemBinding
import com.example.android.uamp.fragments.MediaItemFragment

/**
 * [RecyclerView.Adapter] of [MediaItemData]s used by the [MediaItemFragment].
 */
class MediaItemAdapter(
    private val itemClickedListener: (MediaItemData) -> Unit
) : ListAdapter<MediaItemData, MediaItemAdapter.MediaViewHolder>(MediaItemData.diffCallback) {

    class MediaViewHolder(
        private val binding: FragmentMediaitemBinding,
        itemClickedListener: (MediaItemData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var item: MediaItemData? = null

        fun bind(mediaItem:MediaItemData){
            item = mediaItem
            binding.mediaItem = mediaItem
            binding.executePendingBindings()
        }

        fun playbackResChanged(res:Int){
            binding.itemState.setImageResource(res)
        }

        companion object{
            fun from(parent:ViewGroup,itemClickedListener:(MediaItemData) -> Unit): MediaViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = FragmentMediaitemBinding.inflate(inflater, parent, false)
                return MediaViewHolder(binding, itemClickedListener)
            }
        }

        init {
            binding.root.setOnClickListener {
                item?.let { itemClickedListener(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder.from(parent,itemClickedListener)
    }

    override fun onBindViewHolder(
        holder: MediaViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        val mediaItem = getItem(position)
        var fullRefresh = payloads.isEmpty()

        if (payloads.isNotEmpty()) {
            payloads.forEach { payload ->
                when (payload) {
                    PLAYBACK_RES_CHANGED -> {
                        holder.playbackResChanged(mediaItem.playbackRes)
                    }
                    // If the payload wasn't understood, refresh the full item (to be safe).
                    else -> fullRefresh = true
                }
            }
        }

        // Normally we only fully refresh the list item if it's being initially bound, but
        // we might also do it if there was a payload that wasn't understood, just to ensure
        // there isn't a stale item.
        if (fullRefresh) {
            holder.bind(mediaItem)
        }
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }
}


