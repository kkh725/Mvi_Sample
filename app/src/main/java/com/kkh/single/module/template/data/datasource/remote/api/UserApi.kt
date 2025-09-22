package com.kkh.single.module.template.data.datasource.remote.api

import com.kkh.single.module.template.data.model.ApiResponse
import com.kkh.single.module.template.data.model.BaseApiResponse
import com.kkh.single.module.template.data.model.request.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @POST("medios/ui/pop/api/v2/rbot/GetPatInfo.jsp")
    suspend fun getUserInfo(
        @Body userRequest: UserRequest
    ): BaseApiResponse<Unit>
}