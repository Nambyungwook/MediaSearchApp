package kr.co.nbw.mediasearchapp.data.repository

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.MediasEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.domain.repository.MediaRepository

class FakeMediaRepository: MediaRepository {
    private val medias = mutableListOf<MediaSearchData.MediaEntity>()

    override suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<MediasEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun searchVideos(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<MediasEntity> {
        TODO("Not yet implemented")
    }

    override fun saveMedia(media: MediaSearchData.MediaEntity): ResultWrapper<MediaSearchData.MediaEntity> {
        medias.add(media)
        return ResultWrapper.Success(media)
    }

    override fun deleteMedia(media: MediaSearchData.MediaEntity): ResultWrapper<MediaSearchData.MediaEntity> {
        medias.remove(media)
        return ResultWrapper.Success(media)
    }

    override fun getFavoriteMedias(): ResultWrapper<List<MediaSearchData.MediaEntity>> {
        return ResultWrapper.Success(medias)
    }
}