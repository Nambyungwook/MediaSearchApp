package kr.co.nbw.mediasearchapp.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.MediaType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class MediaUiModel(
    val mediaType: MediaType,
    val thumbnailUrl: String,
    val title: String,
    val categoryName: String?,
    val contentsUrl: String,
    val datetime: Date,
): Parcelable {
    fun isImageType(): Boolean {
        return mediaType == MediaType.IMAGE
    }

    fun getDateTimeStringForSearchListItem(): String {
        return try {
            val sdf = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초", Locale.KOREA)
            sdf.format(datetime)
        } catch (e: Exception) {
            // Date 포맷 변환 실패 시 "-" 반환
            e.printStackTrace()
            "-"
        }
    }

    fun getDateTimeStringForFavoriteListItem(): String {
        return try {
            val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            sdf.format(datetime)
        } catch (e: Exception) {
            // Date 포맷 변환 실패 시 "-" 반환
            e.printStackTrace()
            "-"
        }
    }
}
