package kr.co.nbw.mediasearchapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.presentation.databinding.ItemMediaFavoriteBinding
import kr.co.nbw.mediasearchapp.presentation.view.OnFavoriteMediaItemClick
import kr.co.nbw.mediasearchapp.presentation.view.recyclerview.viewholder.FavoriteMediaItemViewHolder

class FavoriteMediasAdapter(
    private val onFavoriteMediaItemClick: OnFavoriteMediaItemClick
): ListAdapter<MediaEntity, FavoriteMediaItemViewHolder>(MediaEntityDiffCallback) {
    companion object {
        private val MediaEntityDiffCallback = object : DiffUtil.ItemCallback<MediaEntity>() {
            // id와 같은 유니크한 값을 비교
            override fun areItemsTheSame(oldItem: MediaEntity, newItem: MediaEntity): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: MediaEntity, newItem: MediaEntity): Boolean {
                // areItemsTheSame()에서 true를 반환하면 해당 아이템이 완전히 같은지 비교
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMediaItemViewHolder {
        return FavoriteMediaItemViewHolder(
            ItemMediaFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onFavoriteMediaItemClick
        )
    }

    override fun onBindViewHolder(holder: FavoriteMediaItemViewHolder, position: Int) {
        val itemData = getItem(position)
        holder.bind(itemData)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(itemData) }
        }
    }

    private var onItemClickListener: ((MediaEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (MediaEntity) -> Unit) {
        onItemClickListener = listener
    }
}