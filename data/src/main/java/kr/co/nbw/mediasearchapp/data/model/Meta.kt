package kr.co.nbw.mediasearchapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Meta data class : Kakao API response meta data class
 * @property isEnd Boolean? Is end of the list
 * @property pageableCount Int? Pageable count
 * @property totalCount Int? Total count
 */
data class Meta(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int
)