package kr.co.nbw.mediasearchapp.presentation.view

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData

sealed class SearchViewState {
    data object Empty: SearchViewState()
    data object Loading: SearchViewState()
    data class Success(
        val isFirstSearch: Boolean,
        val mediaItemList: List<MediaSearchData>
    ): SearchViewState()
    data class Error(val errorMessage: String): SearchViewState()
}