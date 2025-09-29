package com.kkh.single.module.template.data.datasource.remote.api

import com.kkh.single.module.template.data.model.request.PatientInfoRequest
import com.kkh.single.module.template.data.model.response.GetPatientInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("medios/ui/pop/api/v2/rbot/GetPatInfo.jsp")
    suspend fun getPatientInfo(
        @Body patientInfoRequest: PatientInfoRequest
    ): Response<GetPatientInfoResponse>
}