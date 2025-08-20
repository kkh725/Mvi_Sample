package com.kkh.single.module.template.data.datasource.remote.api

import com.kkh.single.module.template.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface LoginApi {
    @GET("/posts")
    suspend fun getUserInfo(): Response<ApiResponse<Unit>>
}