package kr.co.nbw.mediasearchapp.data.mapper

import kr.co.nbw.mediasearchapp.data.model.Image
import kr.co.nbw.mediasearchapp.data.model.ImagesResponse
import kr.co.nbw.mediasearchapp.data.model.Video
import kr.co.nbw.mediasearchapp.data.model.VideosResponse
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.MediaType
import kr.co.nbw.mediasearchapp.domain.model.MediasEntity

fun Image.toEntity() = MediaEntity(
    mediaType = MediaType.IMAGE,
    thumbnailUrl = this.thumbnailUrl,
    title = this.displaySiteName,
    categoryName = this.collection,
    contentsUrl = this.docUrl,
    datetime = this.datetime
)

fun Video.toEntity() = MediaEntity(
    mediaType = MediaType.VIDEO,
    thumbnailUrl = this.thumbnail,
    title = this.title,
    categoryName = null,
    contentsUrl = this.url,
    datetime = this.datetime
)

fun ImagesResponse.toEntity() = MediasEntity(
    medias = this.images.map { it.toEntity() },
    isEnd = this.meta.isEnd
)

fun VideosResponse.toEntity() = MediasEntity(
    medias = this.videos.map { it.toEntity() },
    isEnd = this.meta.isEnd
)