package kr.co.nbw.mediasearchapp.presentation.view

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity

interface OnFavoriteMediaItemClick {
    fun saveMedia(media: MediaEntity)
    fun deleteMedia(media: MediaEntity)
    fun getFavoriteMedias(): List<MediaEntity>
}