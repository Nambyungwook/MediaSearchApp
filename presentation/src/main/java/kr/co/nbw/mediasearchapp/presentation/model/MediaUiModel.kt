package kr.co.nbw.mediasearchapp.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.co.nbw.mediasearchapp.domain.model.MediaType
import java.util.Date

@Parcelize
data class MediaUiModel(
    val mediaType: MediaType,
    val thumbnailUrl: String,
    val title: String,
    val categoryName: String?,
    val contentsUrl: String,
    val datetime: Date,
): Parcelable
