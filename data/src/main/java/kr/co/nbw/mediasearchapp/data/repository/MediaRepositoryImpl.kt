package kr.co.nbw.mediasearchapp.data.repository

import kr.co.nbw.mediasearchapp.data.source.local.LocalMediaDataSource
import kr.co.nbw.mediasearchapp.data.source.remote.RemoteSearchMediaDataSource
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.MediasEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.domain.repository.MediaRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val localMediaDataSource: LocalMediaDataSource,
    private val remoteSearchMediaDataSource: RemoteSearchMediaDataSource
): MediaRepository {
    override suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<MediasEntity> {
        return remoteSearchMediaDataSource.searchImages(
            query = query,
            sort = sort,
            page = page,
            size = size
        )
    }

    override suspend fun searchVideos(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<MediasEntity> {
        return remoteSearchMediaDataSource.searchVideos(
            query = query,
            sort = sort,
            page = page,
            size = size
        )
    }

    override fun saveMedia(media: MediaEntity): ResultWrapper<MediaEntity> {
        return localMediaDataSource.saveMedia(media)
    }

    override fun deleteMedia(media: MediaEntity): ResultWrapper<MediaEntity> {
        return localMediaDataSource.deleteMedia(media)
    }

    override fun getFavoriteMedias(): ResultWrapper<List<MediaEntity>> {
        return localMediaDataSource.getFavoriteMedias()
    }
}