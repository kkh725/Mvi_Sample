package com.kkh.single.module.template.domain.model

import com.kkh.single.module.template.data.model.request.ParameterItem
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryState.DeliveryScreenState

data class PatientModel(val patientId: String, val dept: String) {
    companion object {
        val mockList = listOf(
            PatientModel("1234", "병동1"),
            PatientModel("2234", "병동2"),
            PatientModel("3234", "병동3")
        )
    }

    fun toPatientRequest(
        deliveryState: DeliveryScreenState,
        dprtDept: String,
        arvlDept: String
    ): ParameterItem {
        val dlvrDvcd = when (deliveryState) {
            DeliveryScreenState.Send -> "0"
            DeliveryScreenState.Receive -> "1"
        }
        return ParameterItem(
            patId = patientId,
            ward = dept,
            dlvrDvcd = dlvrDvcd,
            dprtDept = dprtDept,
            arvlDept = arvlDept
        )
    }
}