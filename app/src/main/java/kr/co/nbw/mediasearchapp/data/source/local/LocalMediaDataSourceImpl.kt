package kr.co.nbw.mediasearchapp.data.source.local

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMediaDataSourceImpl @Inject constructor(
    private val preferenceHelper: SharedPreferenceHelper
) : LocalMediaDataSource {
    override fun saveMedia(
        media: MediaSearchData.MediaEntity
    ): ResultWrapper<MediaSearchData.MediaEntity> {
        return preferenceHelper.saveMedia(media)
    }

    override fun deleteMedia(
        media: MediaSearchData.MediaEntity
    ): ResultWrapper<MediaSearchData.MediaEntity> {
        return preferenceHelper.deleteMedia(media)
    }

    override fun getFavoriteMedias(): ResultWrapper<List<MediaSearchData.MediaEntity>> {
        return preferenceHelper.getFavoriteMedias()
    }
}