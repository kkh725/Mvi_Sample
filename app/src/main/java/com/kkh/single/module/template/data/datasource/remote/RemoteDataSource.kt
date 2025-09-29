package com.kkh.single.module.template.data.datasource.remote

import com.kkh.single.module.template.data.datasource.remote.api.DeliveryApi
import com.kkh.single.module.template.data.datasource.remote.api.UserApi
import com.kkh.single.module.template.data.model.request.DeliveryRequest
import com.kkh.single.module.template.data.model.request.PatientInfoRequest
import com.kkh.single.module.template.data.model.response.DeliveryResponse
import com.kkh.single.module.template.data.model.response.GetPatientInfoResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val userApi: UserApi,
    private val deliveryApi: DeliveryApi
) {
    suspend fun fetchUserInfo(request: PatientInfoRequest): Response<GetPatientInfoResponse> =
        userApi.getPatientInfo(request)

    suspend fun sendDeliveryInfo(request: DeliveryRequest): Response<DeliveryResponse> =
        deliveryApi.sendDeliveryInfo(request)
}