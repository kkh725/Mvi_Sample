package com.kkh.single.module.template.presentation.delivery

import com.kkh.single.module.template.CommonEffect
import com.kkh.single.module.template.SideEffect
import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.util.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(private val repository: MainRepository) :
    BaseMviViewModel<DeliveryState, DeliveryEvent, SideEffect>(reducer = DeliveryReducer(DeliveryState.init)) {

    override suspend fun onEventAfterReduce(event: DeliveryEvent) {
        super.onEventAfterReduce(event)

        when (event) {
            is DeliveryEvent.OnEnterScanScreen -> {
                checkAndHandleDeptState()
                event.patientId?.let{
                    checkPatientInfo(it)
                }
            }
            else -> {}
        }
    }

    private suspend fun checkAndHandleDeptState() {
        val dept = repository.getDept()

        if (dept.isEmpty()) { // 로컬에 저장된 dept가 없는경우 scan으로 보냄.
            reducer.sendEffect(CommonEffect.NavigateTo(ScanRoute.route))
        } else {
            reducer.setState(uiState.value.copy(dept = dept))
        }
    }

    private suspend fun checkPatientInfo(patientId : String){
        // api를 통해 환자 정보 세팅.
        val newPatientList = listOf(PatientModel(patientId = patientId, dept = "병동"))
        reducer.setState(uiState.value.copy(patientList = newPatientList))
    }
}