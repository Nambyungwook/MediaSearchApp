package kr.co.nbw.mediasearchapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * VideosResponse data class : Kakao API response data class
 * @property videos List<Video> 동영상 리스트
 * @property meta Meta 응답 관련 정보
 */
data class VideosResponse(
    @SerializedName("documents")
    val videos: List<Video>,
    @SerializedName("meta")
    val meta: Meta
)