package kr.co.nbw.mediasearchapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Video data class : Kakao API
 * @property title String 동영상 제목
 * @property url String 동영상 URL
 * @property datetime Date 동영상 등록 날짜
 * @property playTime Int 동영상 재생 시간(초단위)
 * @property thumbnail String 동영상 썸네일 URL
 * @property author String 동영상 작성자
 */
data class Video(
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("datetime")
    val datetime: Date,
    @SerializedName("play_time")
    val playTime: Int,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("author")
    val author: String,
)