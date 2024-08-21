package kr.co.nbw.mediasearchapp.data.source.remote

import kotlinx.coroutines.Dispatchers
import kr.co.nbw.mediasearchapp.data.api.SearchApi
import kr.co.nbw.mediasearchapp.data.mapper.toEntity
import kr.co.nbw.mediasearchapp.data.utils.safeApiCall
import kr.co.nbw.mediasearchapp.domain.model.MediasEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import javax.inject.Inject

class RemoteSearchMediaDataSourceImpl @Inject constructor(
    private val api: SearchApi
): RemoteSearchMediaDataSource{
    override suspend fun searchImages(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<MediasEntity> {
        val response = safeApiCall(Dispatchers.IO) {
            api.searchImages(query, sort, page, size)
        }
        return when (response) {
            is ResultWrapper.Success -> {
                ResultWrapper.Success(response.value.toEntity())
            }
            is ResultWrapper.Error -> {
                response
            }
        }
    }

    override suspend fun searchVideos(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ): ResultWrapper<MediasEntity> {
        val response = safeApiCall(Dispatchers.IO) {
            api.searchVideos(query, sort, page, size)
        }
        return when (response) {
            is ResultWrapper.Success -> {
                ResultWrapper.Success(response.value.toEntity())
            }
            is ResultWrapper.Error -> {
                response
            }
        }
    }

}
