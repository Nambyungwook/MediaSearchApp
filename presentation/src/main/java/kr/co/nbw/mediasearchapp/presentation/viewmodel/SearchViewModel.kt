package kr.co.nbw.mediasearchapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.domain.usecase.DeleteMediaUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.GetFavoriteMediasUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.SaveMediaUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.SearchImagesUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.SearchVideosUseCase
import kr.co.nbw.mediasearchapp.presentation.utils.Constants
import kr.co.nbw.mediasearchapp.presentation.view.SearchViewState
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchImagesUseCase: SearchImagesUseCase,
    private val searchVideosUseCase: SearchVideosUseCase,
    private val saveMediaUseCase: SaveMediaUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase,
    private val getFavoriteMediasUseCase: GetFavoriteMediasUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _searchViewState = MutableStateFlow<SearchViewState>(SearchViewState.Empty)
    val searchViewState: StateFlow<SearchViewState> get() = _searchViewState

    var hasNextPage = true
    private var hasNextPageImageResponse = true
    private var hasNextPageVideoResponse = true
    var currentImageResponsePageIndex: Int = Constants.STARTING_PAGE_INDEX
    var currentVideoResponsePageIndex: Int = Constants.STARTING_PAGE_INDEX
    private val mediaItemList = mutableListOf<MediaSearchData>()

    var query = String()
        set(value) {
            field = value
            savedStateHandle.set(SAVE_STATE_KEY, value)
        }

    init {
        query = savedStateHandle.get<String>(SAVE_STATE_KEY) ?: ""
    }

    companion object {
        private const val SAVE_STATE_KEY = "query"
    }

    fun searchMedias(query: String) {
        _searchViewState.value = SearchViewState.Loading
        currentImageResponsePageIndex = Constants.STARTING_PAGE_INDEX
        currentVideoResponsePageIndex = Constants.STARTING_PAGE_INDEX
        mediaItemList.clear()

        viewModelScope.launch {
            val imagesResponse = searchImagesUseCase.invoke(
                query = query,
                sort = Constants.SORT_MODE,
                page = currentImageResponsePageIndex,
                size = Constants.PAGING_SIZE
            )
            val videosResponse = searchVideosUseCase.invoke(
                query = query,
                sort = Constants.SORT_MODE,
                page = currentVideoResponsePageIndex,
                size = Constants.PAGING_SIZE
            )

            val mediaEntityListUnit = mutableListOf<MediaSearchData.MediaEntity>()
            val responses = listOf(imagesResponse, videosResponse)
            responses.forEach { response ->
                when (response) {
                    is ResultWrapper.Success -> {
                        mediaEntityListUnit.addAll(response.value.medias)
                        if (response.value.medias.isNotEmpty() && response.value.medias[0].isImageType()) {
                            hasNextPageImageResponse = !response.value.isEnd
                        } else {
                            hasNextPageVideoResponse = !response.value.isEnd
                        }
                    }
                    is ResultWrapper.Error -> {
                        _searchViewState.value = SearchViewState.Error("ErrorType : ${response.errorType}\nMessage : ${response.message}")
                        hasNextPage = false
                        return@forEach
                    }
                }
            }

            if (searchViewState.value !is SearchViewState.Error) {
                if (mediaEntityListUnit.isEmpty()) {
                    hasNextPage = false
                    mediaItemList.clear()
                    _searchViewState.value = SearchViewState.Empty
                } else {
                    hasNextPage = hasNextPageImageResponse || hasNextPageVideoResponse
                    mediaEntityListUnit.sortByDescending { it.datetime }
                    mediaItemList.addAll(mediaEntityListUnit)
                    mediaItemList.add(MediaSearchData.MediaSearchPagingData(hasNextPage, maxOf(currentImageResponsePageIndex, currentVideoResponsePageIndex)))
                    _searchViewState.value = SearchViewState.Success(
                        isFirstSearch = true,
                        mediaItemList = mediaItemList
                    )
                    mediaEntityListUnit.clear()
                }
            }
        }
    }

    fun loadMoreMedias() {
        _searchViewState.value = SearchViewState.Loading
        viewModelScope.launch {
            val imagesResponse = if (hasNextPageImageResponse) {
                searchImagesUseCase.invoke(
                    query = query,
                    sort = Constants.SORT_MODE,
                    page = ++currentImageResponsePageIndex,
                    size = Constants.PAGING_SIZE
                )
            } else {
                null
            }
            val videosResponse = if (hasNextPageVideoResponse) {
                searchVideosUseCase.invoke(
                    query = query,
                    sort = Constants.SORT_MODE,
                    page = ++currentVideoResponsePageIndex,
                    size = Constants.PAGING_SIZE
                )
            } else {
                null
            }

            val loadMoreMediaEntityListUnit = mutableListOf<MediaSearchData.MediaEntity>()
            val responses = listOf(imagesResponse, videosResponse)
            responses.filterNotNull().forEach { response ->
                when (response) {
                    is ResultWrapper.Success -> {
                        loadMoreMediaEntityListUnit.addAll(response.value.medias)
                        if (response.value.medias.isNotEmpty() && response.value.medias[0].isImageType()) {
                            hasNextPageImageResponse = !response.value.isEnd
                        } else {
                            hasNextPageVideoResponse = !response.value.isEnd
                        }
                    }
                    is ResultWrapper.Error -> {
                        _searchViewState.value = SearchViewState.Error("ErrorType : ${response.errorType}\nMessage : ${response.message}")
                        return@forEach
                    }
                }
            }

            if (searchViewState.value !is SearchViewState.Error) {
                hasNextPage = hasNextPageImageResponse || hasNextPageVideoResponse
                loadMoreMediaEntityListUnit.sortByDescending { it.datetime }
                mediaItemList.addAll(loadMoreMediaEntityListUnit)
                mediaItemList.add(
                    MediaSearchData.MediaSearchPagingData(
                        hasNextPage,
                        maxOf(currentImageResponsePageIndex, currentVideoResponsePageIndex)
                    )
                )
                _searchViewState.value = SearchViewState.Success(
                    isFirstSearch = false,
                    mediaItemList = mediaItemList
                )
                loadMoreMediaEntityListUnit.clear()
            }
        }
    }

    fun saveMedia(media: MediaSearchData.MediaEntity)= saveMediaUseCase.invoke(media)

    fun deleteMedia(media: MediaSearchData.MediaEntity) = deleteMediaUseCase.invoke(media)

    fun getFavoriteMedias() = getFavoriteMediasUseCase.invoke()
}