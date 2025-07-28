package com.kkh.single.module.template.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun saveInt(key: Preferences.Key<Int>, value: Int) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun saveBool(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // === Read methods ===
    fun readString(key: Preferences.Key<String>): Flow<String> = read(key, "")
    fun readInt(key: Preferences.Key<Int>): Flow<Int> = read(key, 0)
    fun readBool(key: Preferences.Key<Boolean>): Flow<Boolean> = read(key, false)

    // === Generic read function ===
    private fun <T> read(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else throw exception
            }
            .map { preferences -> preferences[key] ?: defaultValue }
    }
}