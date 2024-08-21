package kr.co.nbw.mediasearchapp.presentation.view.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.nbw.mediasearchapp.R
import kr.co.nbw.mediasearchapp.databinding.ItemMediaSearchBinding
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.MediaType
import kr.co.nbw.mediasearchapp.presentation.view.OnFavoriteMediaItemClick

class SearchMediaItemViewHolder(
    private val binding: ItemMediaSearchBinding,
    private val onFavoriteMediaItemClick: OnFavoriteMediaItemClick
): RecyclerView.ViewHolder(binding.root) {
    fun bind(media: MediaEntity) {
        Glide.with(binding.root)
            .load(media.thumbnailUrl)
            .placeholder(R.color.gray_400)
            .error(R.drawable.icon_error)
            .fallback(R.drawable.icon_warning)
            .into(binding.ivMediaThumbnail)

        binding.apply {
            tvMediaTitle.text = media.title
            tvMediaCategory.text = media.categoryName
            tvMediaContents.text = media.contentsUrl
            tvMediaDatetime.text = media.getDateTimeStringForSearchListItem()
        }

        updateFavoriteMediaView(media)

        when (media.mediaType) {
            MediaType.IMAGE -> {
                binding.ivMediaType.setImageResource(R.drawable.icon_image)
            }
            MediaType.VIDEO -> {
                binding.ivMediaType.setImageResource(R.drawable.icon_video)
            }
        }

        binding.ivFavorite.setOnClickListener {
            // fixme : 이런식으로 코드로 구현하지 말고 flow collect 하는 방식으로 구현해보자
            if (onFavoriteMediaItemClick.getFavoriteMedias().contains(media)) {
                onFavoriteMediaItemClick.deleteMedia(media)
                binding.ivFavorite.setImageResource(R.drawable.icon_like_off)
            } else {
                onFavoriteMediaItemClick.saveMedia(media)
                binding.ivFavorite.setImageResource(R.drawable.icon_like_on)
            }
        }
    }

    fun updateFavoriteMediaView(media: MediaEntity) {
        if (onFavoriteMediaItemClick.getFavoriteMedias().contains(media)) {
            binding.ivFavorite.setImageResource(R.drawable.icon_like_on)
        } else {
            binding.ivFavorite.setImageResource(R.drawable.icon_like_off)
        }
    }
}