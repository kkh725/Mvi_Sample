package com.kkh.single.module.template.data.datasource.remote

import com.kkh.single.module.template.data.datasource.remote.api.LoginApi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val loginApi: LoginApi) {
    suspend fun fetchUserInfo(): Result<Unit> {
        return runCatching {
            val response = loginApi.getUserInfo()
            if (response.isSuccessful) {
                response.body() ?: Unit
            } else {
                throw Exception("Server Error: ${response.code()}")
            }
        }.recoverCatching { e ->
            // Result.failure(e)로 감싸져 상위로 전달.
            throw Exception("Network Error", e)
        }
    }
}