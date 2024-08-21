package kr.co.nbw.mediasearchapp.presentation.view.recyclerview

import androidx.recyclerview.widget.RecyclerView
import java.util.Timer
import java.util.TimerTask

class OnNextPageScrollListener(
    private val onNextPageCallListener: OnNextPageCallListener,
): RecyclerView.OnScrollListener() {
    private val loadingDelayMillis = 500L
    private var loadingTimer: Timer? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (onNextPageCallListener.hasNextPage()) {
                loadingTimer?.cancel()
                loadingTimer = Timer()
                loadingTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        if (onNextPageCallListener.hasNextPage() && !onNextPageCallListener.isLoading()) {
                            val scrollOffset = recyclerView.computeVerticalScrollOffset()
                            val scrollExtent = recyclerView.computeVerticalScrollExtent()
                            val range = recyclerView.computeVerticalScrollRange()

                            if (scrollOffset + scrollExtent / 2 > 0.7f * range) {
                                onNextPageCallListener.onNext()
                            }
                        }
                    }
                }, loadingDelayMillis)
            }
        }
    }
}