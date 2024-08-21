package kr.co.nbw.mediasearchapp.data.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.nbw.mediasearchapp.data.sharedpreferencehelper.SharedPreferenceHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {
    @Singleton
    @Provides
    fun provideSharedPreferenceHelper(
        @ApplicationContext context: Context,
        gson: Gson
    ): SharedPreferenceHelper {
        return SharedPreferenceHelper(context, gson)
    }
}