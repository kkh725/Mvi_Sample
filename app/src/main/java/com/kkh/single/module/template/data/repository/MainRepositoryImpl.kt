package com.kkh.single.module.template.data.repository

import com.kkh.single.module.template.data.datasource.local.LocalDataSource
import com.kkh.single.module.template.data.datasource.remote.RemoteDataSource
import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.data.model.processApiResponse
import com.kkh.single.module.template.data.model.request.DeliveryRequest
import com.kkh.single.module.template.data.model.request.ParameterItem
import com.kkh.single.module.template.data.model.request.UserParameterItem
import com.kkh.single.module.template.data.model.request.UserRequest
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryState.DeliveryScreenState
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MainRepository {
    //business
    override suspend fun getPatientInfo(patientId: String) = runCatching {
        val request = UserRequest(
            parameter = listOf(UserParameterItem(patId = patientId))
        )
        remoteDataSource.fetchUserInfo(request).processApiResponse()
    }

    override suspend fun sendDeliveryInfo(
        patientList: List<PatientModel>,
        deliveryState: DeliveryScreenState,
        dprtDept: String,
        arvlDept: String
    ) = runCatching {
        val dlvrDvcd = when(deliveryState){
            DeliveryScreenState.Send -> "0"
            DeliveryScreenState.Receive -> "1"
        }
        val parameterList = patientList.map { patient ->
            ParameterItem(
                patId = patient.patientId,
                ward = patient.dept,
                dlvrDvcd = dlvrDvcd,
                dprtDept = dprtDept,
                arvlDept = arvlDept
            )
        }
        val request = DeliveryRequest(parameter = parameterList)
        remoteDataSource.sendDeliveryInfo(request).processApiResponse()
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