package kr.co.nbw.mediasearchapp.data.source.remote

import kr.co.nbw.mediasearchapp.domain.model.MediasEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper

interface RemoteSearchMediaDataSource {
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
}