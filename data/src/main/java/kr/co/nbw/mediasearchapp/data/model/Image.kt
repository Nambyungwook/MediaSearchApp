package kr.co.nbw.mediasearchapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Image data class : Kakao API response image data class
 * @property collection String 컬렉션
 * @property thumbnailUrl String 썸네일 이미지 URL
 * @property imageUrl String 이미지 URL
 * @property width Int 이미지 가로 크기
 * @property height Int 이미지 세로 크기
 * @property displaySiteName String 출처
 * @property docUrl String 문서 URL
 * @property datetime Date 이미지 등록 날짜
 */
data class Image(
    @SerializedName("collection")
    val collection: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("display_sitename")
    val displaySiteName: String,
    @SerializedName("doc_url")
    val docUrl: String,
    @SerializedName("datetime")
    val datetime: Date,
)