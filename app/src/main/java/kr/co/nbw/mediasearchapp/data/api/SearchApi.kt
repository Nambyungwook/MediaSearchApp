package kr.co.nbw.mediasearchapp.data.api

import kr.co.nbw.mediasearchapp.data.model.ImagesResponse
import kr.co.nbw.mediasearchapp.data.model.VideosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("v2/search/image")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ImagesResponse

    @GET("v2/search/vclip")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): VideosResponse
}