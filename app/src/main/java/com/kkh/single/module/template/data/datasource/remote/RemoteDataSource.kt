package com.kkh.single.module.template.data.datasource.remote

import com.kkh.single.module.template.data.datasource.remote.api.LoginApi
import com.kkh.single.module.template.data.model.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val loginApi: LoginApi) {
    suspend fun fetchUserInfo(): Response<ApiResponse<Unit>> {
        return loginApi.getUserInfo()
    }
}