package com.kkh.single.module.template.data.di

import com.kkh.single.module.template.data.datasource.local.LocalDataSource
import com.kkh.single.module.template.data.datasource.remote.RemoteDataSource
import com.kkh.single.module.template.data.repository.MainRepositoryImpl
import com.kkh.single.module.template.domain.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): MainRepository {
        return MainRepositoryImpl(remoteDataSource, localDataSource)
    }
}