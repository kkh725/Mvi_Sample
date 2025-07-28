package com.kkh.single.module.template.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.kkh.single.module.template.data.datasource.local.DataStoreManager
import com.kkh.single.module.template.data.datasource.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<androidx.datastore.preferences.core.Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.dataStoreFile("app_preferences") }
        )
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(datastore: DataStore<androidx.datastore.preferences.core.Preferences>) : DataStoreManager{
        return DataStoreManager(datastore)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(dataStoreManager: DataStoreManager): LocalDataSource {
        return LocalDataSource(dataStoreManager)
    }
}