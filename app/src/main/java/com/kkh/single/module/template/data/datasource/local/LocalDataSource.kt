package com.kkh.single.module.template.data.datasource.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LocalDataSource @Inject constructor(private val dataStoreManager: DataStoreManager) {

    private val deptKey = stringPreferencesKey("deptKey")

    private val keyBoolean = booleanPreferencesKey("CUSTOM_BOOL")
    private val keyInt = intPreferencesKey("CUSTOM_INT")

    suspend fun fetchDept(dept: String) {
        dataStoreManager.saveString(deptKey, dept)
    }

    // 한 번만 값을 얻고 싶다면.
    suspend fun getDept(): String {
        return dataStoreManager.readString(deptKey).first()
    }

    // 변화를 계속 관찰하고 싶다면.
    fun observeDept(): Flow<String> {
        return dataStoreManager.readString(deptKey)
    }


}