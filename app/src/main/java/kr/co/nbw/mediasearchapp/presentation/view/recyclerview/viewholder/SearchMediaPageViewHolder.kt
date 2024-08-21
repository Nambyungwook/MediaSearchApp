package kr.co.nbw.mediasearchapp.presentation.view.recyclerview.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.nbw.mediasearchapp.databinding.ItemMediaSearchPageBinding

class SearchMediaPageViewHolder(
    private val binding: ItemMediaSearchPageBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        hasNextPage: Boolean,
        currentPage: Int,
    ) {
        binding.apply {
            if (hasNextPage) {
                tvPage.text = "$currentPage"
                dividerPage.isVisible = true
                lastPagePadding.isVisible = false
            } else {
                tvPage.text = "마지막"
                dividerPage.isVisible = false
                lastPagePadding.isVisible = true
            }
        }
    }
}