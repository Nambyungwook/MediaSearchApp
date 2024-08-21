package kr.co.nbw.mediasearchapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.usecase.DeleteMediaUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.GetFavoriteMediasUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.SaveMediaUseCase
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getFavoriteMediasUseCase: GetFavoriteMediasUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase,
    private val saveMediaUseCase: SaveMediaUseCase
): ViewModel() {
    fun saveMedia(media: MediaEntity) = saveMediaUseCase.invoke(media)

    fun deleteMedia(media: MediaEntity) = deleteMediaUseCase.invoke(media)

    fun getFavoriteMedias() = getFavoriteMediasUseCase.invoke()
}