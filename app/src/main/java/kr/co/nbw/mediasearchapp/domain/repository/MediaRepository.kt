package kr.co.nbw.mediasearchapp.domain.repository

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.MediasEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper

interface MediaRepository {
    suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<MediasEntity>

    suspend fun searchVideos(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<MediasEntity>

    fun saveMedia(media: MediaEntity): ResultWrapper<MediaEntity>

    fun deleteMedia(media: MediaEntity): ResultWrapper<MediaEntity>

    fun getFavoriteMedias(): ResultWrapper<List<MediaEntity>>
}