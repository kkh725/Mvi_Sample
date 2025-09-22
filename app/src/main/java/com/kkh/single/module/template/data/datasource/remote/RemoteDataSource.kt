package com.kkh.single.module.template.data.datasource.remote

import com.kkh.single.module.template.data.datasource.remote.api.DeliveryApi
import com.kkh.single.module.template.data.datasource.remote.api.UserApi
import com.kkh.single.module.template.data.model.BaseApiResponse
import com.kkh.single.module.template.data.model.request.DeliveryRequest
import com.kkh.single.module.template.data.model.request.UserRequest
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val userApi: UserApi,
    private val deliveryApi: DeliveryApi
) {
    suspend fun fetchUserInfo(userRequest: UserRequest): BaseApiResponse<Unit> {
        return userApi.getUserInfo(userRequest)
    }

    suspend fun sendDeliveryInfo(deliveryRequest: DeliveryRequest): BaseApiResponse<Unit> {
        return deliveryApi.sendDeliveryInfo(deliveryRequest)
    }
}