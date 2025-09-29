package com.kkh.single.module.template.data.repository

import com.kkh.single.module.template.data.datasource.local.LocalDataSource
import com.kkh.single.module.template.data.datasource.remote.RemoteDataSource
import com.kkh.single.module.template.domain.model.PatientModel
import com.kkh.single.module.template.data.model.request.DeliveryRequest
import com.kkh.single.module.template.data.model.request.ParameterItem
import com.kkh.single.module.template.data.model.request.UserParameterItem
import com.kkh.single.module.template.data.model.request.PatientInfoRequest
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryState.DeliveryScreenState
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MainRepository {
    //business
    override suspend fun getPatientInfo(patientId: String): Result<PatientModel> =
        runCatching {
            val request = PatientInfoRequest(
                parameter = listOf(UserParameterItem(patId = patientId))
            )
            val res = remoteDataSource.fetchUserInfo(request)
            if (res.isSuccessful) {
                if (res.body()?.isSuccess() == true) {
                    res.body()?.result?.first()?.responseData?.toPatientModel()
                        ?: throw Exception("환자 정보 인식에 실패하였습니다. statusCodeError")
                } else {
                    throw Exception("환자 정보 인식에 실패하였습니다. ${res.body()} ApiError")
                }
            } else {
                throw Exception("환자 정보 인식에 실패하였습니다. ${res.code()} HttpError")
            }
        }

    override suspend fun sendDeliveryInfo(
        patientList: List<PatientModel>,
        deliveryState: DeliveryScreenState,
        dprtDept: String,
        arvlDept: String
    ): Result<Unit> = runCatching {
        val parameterList = patientList.map { patient ->
            patient.toPatientRequest(
                deliveryState = deliveryState,
                dprtDept = dprtDept,
                arvlDept = arvlDept
            )
        }
        val request = DeliveryRequest(parameter = parameterList)
        val res = remoteDataSource.sendDeliveryInfo(request)

        if (res.isSuccessful) {
            if (res.body()?.isSuccess() == true) {
                Unit
            } else {
                throw Exception("배송 정보 전송에 실패하였습니다. statusCodeError")
            }
        } else {
            throw Exception(
                "배송 정보 전송에 실패하였습니다. ${res.code()} HttpError"
            )
        }
    }

    /**
     * Local
     * @param dept 부서명 - 약제실, 42병동, 중환자실 등
     */

    override suspend fun fetchDept(dept: String) {
        localDataSource.fetchDept(dept)
    }

    override suspend fun getDept(): String {
        return localDataSource.getDept()
    }
}