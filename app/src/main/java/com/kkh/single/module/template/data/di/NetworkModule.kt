package com.kkh.single.module.template.data.di

import com.kkh.single.module.template.data.datasource.remote.api.LoginApi
import com.kkh.single.module.template.data.datasource.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(loginApi: LoginApi): RemoteDataSource {
        return RemoteDataSource(loginApi)
    }
}