package kr.co.nbw.mediasearchapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.domain.usecase.DeleteMediaUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.GetFavoriteMediasUseCase
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMediasUseCase: GetFavoriteMediasUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase
): ViewModel() {
    private val _favoriteMedias = MutableStateFlow<ResultWrapper<List<MediaEntity>>>(ResultWrapper.Success(emptyList()))
    val favoriteMedias: StateFlow<ResultWrapper<List<MediaEntity>>> get() = _favoriteMedias

    fun fetchFavoriteMedias() {
        _favoriteMedias.value = getFavoriteMediasUseCase.invoke()
    }

    fun getFavoriteMediaList() = getFavoriteMediasUseCase.invoke()

    fun deleteMedia(media: MediaEntity) = deleteMediaUseCase.invoke(media)
}