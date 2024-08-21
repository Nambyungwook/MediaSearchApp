package kr.co.nbw.mediasearchapp.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.nbw.mediasearchapp.BuildConfig
import kr.co.nbw.mediasearchapp.data.api.HeaderInterceptor
import kr.co.nbw.mediasearchapp.data.api.SearchApi
import kr.co.nbw.mediasearchapp.data.utils.Constants.BASE_URL
import kr.co.nbw.mediasearchapp.data.utils.Constants.DATE_TIME_FOMAT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().addInterceptor(headerInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS) // 요청을 시작한 후 서버와의 TCP handshake가 완료되기까지 지속되는 시간
            .readTimeout(20, TimeUnit.SECONDS) // 연결이 설정되면 모든 바이트가 전송되는 속도를 감시
            .writeTimeout(20, TimeUnit.SECONDS) // 얼마나 빨리 서버에 바이트를 보낼 수 있는지 확인
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().apply {
            setDateFormat(DATE_TIME_FOMAT)
        }.create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): SearchApi {
        return retrofit.create(SearchApi::class.java)
    }
}