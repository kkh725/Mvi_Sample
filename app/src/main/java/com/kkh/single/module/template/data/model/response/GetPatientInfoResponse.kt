package com.kkh.single.module.template.data.model.response

import com.kkh.single.module.template.domain.model.PatientModel
import kotlinx.serialization.Serializable

@Serializable
data class GetPatientInfoResponse(
    val result: List<PatientInfo>,
    val rsMsg: RsMsg
) {
    fun isSuccess(): Boolean {
        return rsMsg.statusCode == "S"
    }
}

@Serializable
data class PatientInfo(
    val responseData: PatientData, // {} 형태
    val patId: String,
    val count: String,
    val executeType: String,
    val status: String,
    val statusMessage: String
) {
    @Serializable
    data class PatientData(
        val ward: String,
        val patId: String,
        val dlvrDt: String
    ) {
        fun toPatientModel(): PatientModel {
            return PatientModel(
                patientId = patId,
                dept = ward
            )
        }
    }
}

@Serializable
data class RsMsg(
    val statusCode: String,
    val message: String,
    val errorCode: String
)



