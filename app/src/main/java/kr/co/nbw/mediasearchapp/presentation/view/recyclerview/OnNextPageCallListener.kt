package kr.co.nbw.mediasearchapp.presentation.view.recyclerview

interface OnNextPageCallListener {
    fun onNext()
    fun hasNextPage(): Boolean
    fun getCurrentPage(): Int
    fun isLoading(): Boolean
}