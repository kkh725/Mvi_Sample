package com.kkh.single.module.template.data.di

import com.kkh.single.module.template.data.datasource.remote.api.UserApi
import com.kkh.single.module.template.data.datasource.remote.api.DeliveryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun DeliveryApi(retrofit: Retrofit): DeliveryApi {
        return retrofit.create(DeliveryApi::class.java)
    }
}