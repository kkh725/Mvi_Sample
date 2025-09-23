package com.kkh.single.module.template.domain.repository

import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryState.DeliveryScreenState

interface MainRepository {
    suspend fun getPatientInfo(patientId: String): Result<Unit>
    suspend fun sendDeliveryInfo(
        patientList: List<PatientModel>,
        deliveryState: DeliveryScreenState,
        dprtDept: String,
        arvlDept: String
    ): Result<Unit>

    suspend fun fetchDept(dept: String)
    suspend fun getDept(): String
}