package kr.co.nbw.mediasearchapp.presentation.mapper

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.presentation.model.MediaUiModel

fun MediaEntity.toMediaUiModel() = MediaUiModel(
    mediaType = this.mediaType,
    thumbnailUrl = this.thumbnailUrl,
    title = this.title,
    categoryName = this.categoryName,
    contentsUrl = this.contentsUrl,
    datetime = this.datetime
)

fun MediaUiModel.toMediaEntity() = MediaEntity(
    mediaType = this.mediaType,
    thumbnailUrl = this.thumbnailUrl,
    title = this.title,
    categoryName = this.categoryName,
    contentsUrl = this.contentsUrl,
    datetime = this.datetime
)