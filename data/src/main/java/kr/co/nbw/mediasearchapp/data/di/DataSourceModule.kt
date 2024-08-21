package kr.co.nbw.mediasearchapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.nbw.mediasearchapp.data.source.local.LocalMediaDataSource
import kr.co.nbw.mediasearchapp.data.source.local.LocalMediaDataSourceImpl
import kr.co.nbw.mediasearchapp.data.source.remote.RemoteSearchMediaDataSource
import kr.co.nbw.mediasearchapp.data.source.remote.RemoteSearchMediaDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindLocalMediaDataSource(
        localMediaDataSourceImpl: LocalMediaDataSourceImpl
    ): LocalMediaDataSource

    @Singleton
    @Binds
    abstract fun bindRemoteSearchMediaDataSource(
        remoteMediaDataSourceImpl: RemoteSearchMediaDataSourceImpl
    ): RemoteSearchMediaDataSource
}