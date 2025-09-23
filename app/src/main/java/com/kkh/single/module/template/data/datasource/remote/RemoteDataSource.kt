package com.kkh.single.module.template.data.datasource.remote

import com.kkh.single.module.template.data.datasource.remote.api.DeliveryApi
import com.kkh.single.module.template.data.datasource.remote.api.UserApi
import com.kkh.single.module.template.data.model.BaseApiResponse
import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.data.model.request.DeliveryRequest
import com.kkh.single.module.template.data.model.request.ParameterItem
import com.kkh.single.module.template.data.model.request.UserParameterItem
import com.kkh.single.module.template.data.model.request.UserRequest
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val userApi: UserApi,
    private val deliveryApi: DeliveryApi
) {
    suspend fun fetchUserInfo(request: UserRequest): BaseApiResponse<Unit> =
        userApi.getUserInfo(request)

    suspend fun sendDeliveryInfo(request: DeliveryRequest): BaseApiResponse<Unit> =
        deliveryApi.sendDeliveryInfo(request)
}