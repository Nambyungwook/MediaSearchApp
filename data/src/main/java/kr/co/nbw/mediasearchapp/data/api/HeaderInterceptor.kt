package kr.co.nbw.mediasearchapp.data.api

import kr.co.nbw.mediasearchapp.data.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader("Authorization", "KakaoAK ${Constants.API_KEY}")

        return chain.proceed(requestBuilder.build())
    }
}