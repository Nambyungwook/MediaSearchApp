package kr.co.nbw.mediasearchapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * ImagesResponse data class : Kakao API response data class
 * @property images List<Image> 이미지 리스트
 * @property meta Meta 응답 관련 정보
 */
data class ImagesResponse(
    @SerializedName("documents")
    val images: List<Image>,
    @SerializedName("meta")
    val meta: Meta
)