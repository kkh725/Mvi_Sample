package com.kkh.single.module.template.data.datasource.remote.api

import com.kkh.single.module.template.data.model.BaseApiResponse
import com.kkh.single.module.template.data.model.request.DeliveryRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DeliveryApi {
    @POST("medios/ui/pop/api/v2/rbot/PutDlvrInfo.jsp")
    suspend fun sendDeliveryInfo(
        @Body deliveryRequest: DeliveryRequest
    ): BaseApiResponse<Unit>
}