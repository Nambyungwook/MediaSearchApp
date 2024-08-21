package kr.co.nbw.mediasearchapp.presentation.view.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.nbw.mediasearchapp.presentation.R
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.MediaType
import kr.co.nbw.mediasearchapp.presentation.databinding.ItemMediaFavoriteBinding
import kr.co.nbw.mediasearchapp.presentation.view.OnFavoriteMediaItemClick

class FavoriteMediaItemViewHolder(
    private val binding: ItemMediaFavoriteBinding,
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
            ivFavorite.setImageResource(R.drawable.icon_like_on)
            tvMediaTitle.text = media.title
            tvMediaDatetime.text = media.getDateTimeStringForFavoriteListItem()
        }

        when (media.mediaType) {
            MediaType.IMAGE -> {
                binding.ivMediaType.setImageResource(R.drawable.icon_image)
            }
            MediaType.VIDEO -> {
                binding.ivMediaType.setImageResource(R.drawable.icon_video)
            }
        }

        binding.ivFavorite.setOnClickListener {
            onFavoriteMediaItemClick.deleteMedia(media)
        }
    }
}