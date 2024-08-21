package kr.co.nbw.mediasearchapp.domain.model

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity

data class MediasEntity(
    val medias: List<MediaEntity>,
    val isEnd: Boolean
)