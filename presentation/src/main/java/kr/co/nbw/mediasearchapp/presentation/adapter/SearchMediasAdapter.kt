package kr.co.nbw.mediasearchapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.presentation.databinding.ItemMediaSearchBinding
import kr.co.nbw.mediasearchapp.presentation.databinding.ItemMediaSearchPageBinding
import kr.co.nbw.mediasearchapp.presentation.view.OnFavoriteMediaItemClick
import kr.co.nbw.mediasearchapp.presentation.view.recyclerview.viewholder.SearchMediaItemViewHolder
import kr.co.nbw.mediasearchapp.presentation.view.recyclerview.viewholder.SearchMediaPageViewHolder

class SearchMediasAdapter(
    private val onFavoriteMediaItemClick: OnFavoriteMediaItemClick
): ListAdapter<MediaSearchData, RecyclerView.ViewHolder>(MediaEntityDiffCallback) {

    companion object {
        private val MediaEntityDiffCallback = object : DiffUtil.ItemCallback<MediaSearchData>() {
            // id와 같은 유니크한 값을 비교
            override fun areItemsTheSame(oldItem: MediaSearchData, newItem: MediaSearchData): Boolean {
                return if (oldItem is MediaSearchData.MediaEntity && newItem is MediaSearchData.MediaEntity) {
                    oldItem.title == newItem.title
                } else if (oldItem is MediaSearchData.MediaSearchPagingData && newItem is MediaSearchData.MediaSearchPagingData) {
                    oldItem.currentPage == newItem.currentPage
                } else {
                    false
                }
            }

            override fun areContentsTheSame(oldItem: MediaSearchData, newItem: MediaSearchData): Boolean {
                // areItemsTheSame()에서 true를 반환하면 해당 아이템이 완전히 같은지 비교
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            MediaSearchViewHolderType.MEDIA.viewType -> {
                return SearchMediaItemViewHolder(
                    ItemMediaSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    onFavoriteMediaItemClick
                )
            }
            MediaSearchViewHolderType.MEDIA_PAGING.viewType -> {
                return SearchMediaPageViewHolder(
                    ItemMediaSearchPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> throw ClassCastException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val itemData = getItem(position)) {
            is MediaSearchData.MediaEntity -> {
                if (holder is SearchMediaItemViewHolder) {
                    holder.bind(itemData)
                    holder.itemView.setOnClickListener {
                        onItemClickListener?.let { it(itemData) }
                    }
                }
            }
            is MediaSearchData.MediaSearchPagingData -> {
                if (holder is SearchMediaPageViewHolder) {
                    holder.bind(itemData.hasNextPage, itemData.currentPage)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is MediaSearchData.MediaEntity -> MediaSearchViewHolderType.MEDIA.viewType
            is MediaSearchData.MediaSearchPagingData -> MediaSearchViewHolderType.MEDIA_PAGING.viewType
        }
    }

    private var onItemClickListener: ((MediaSearchData.MediaEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (MediaSearchData.MediaEntity) -> Unit) {
        onItemClickListener = listener
    }
}

enum class MediaSearchViewHolderType(val viewType: Int) {
    MEDIA(0),
    MEDIA_PAGING(1)
}