package kr.co.nbw.mediasearchapp.data.source.local

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper

interface LocalMediaDataSource {
    fun saveMedia(media: MediaEntity): ResultWrapper<MediaEntity>

    fun deleteMedia(media: MediaEntity): ResultWrapper<MediaEntity>

    fun getFavoriteMedias(): ResultWrapper<List<MediaEntity>>
}