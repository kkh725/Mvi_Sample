package com.kkh.single.module.template.presentation.delivery

import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.SideEffect
import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.util.DeliveryScreenState
import com.kkh.single.module.template.util.DeptMsgConstants
import com.kkh.single.module.template.util.SnackBarMsgConstants
import com.kkh.single.module.template.util.common.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class DeliveryViewModel @Inject constructor(private val repository: MainRepository) :
    BaseMviViewModel<DeliveryState, DeliveryEvent, SideEffect>(reducer = DeliveryReducer(DeliveryState.init)) {

    override suspend fun onEventAfterReduce(event: DeliveryEvent) {
        super.onEventAfterReduce(event)

        when (event) {
            is DeliveryEvent.OnEnterScanScreen -> {
                checkAndHandleDeptState()
                event.patientId?.let{
                    processPatientInfo(it)
                }
            }
            is DeliveryEvent.OnClickDeliveryButton -> {
                processRequestDelivery()
            }
            
            is DeliveryEvent.OnScanBarcode -> {
                processScanBarcode(event.barcode)
            }
            else -> {}
        }
    }

    /**
     * 화면 진입 시점에 로컬에 저장된 부서 조회.
     * 약제실 -> 보내기 상태.
     * else -> 받기 상태
     */
    private suspend fun checkAndHandleDeptState() {
        val dept = repository.getDept()

        if (dept.isEmpty()) { // 로컬에 저장된 dept가 없는경우 scan으로 보냄.
            reducer.sendEffect(CommonEffect.NavigateTo(ScanRoute.route))
        } else {
            if (dept == DeptMsgConstants.MEDICINE_ROOM){
                reducer.setState(uiState.value.copy(deliveryScreenState = DeliveryScreenState.Send))
            }else{
                reducer.setState(uiState.value.copy(deliveryScreenState = DeliveryScreenState.Receive))
            }
            reducer.setState(uiState.value.copy(dept = dept))
        }
    }

    private suspend fun processPatientInfo(patientId : String){
        // api를 통해 환자 정보 세팅.
        val newPatientList = listOf(PatientModel(patientId = patientId, dept = "병동"))
        reducer.setState(uiState.value.copy(patientList = newPatientList))
    }

    private suspend fun processRequestDelivery(){
        val isSendState = uiState.value.deliveryScreenState == DeliveryScreenState.Send

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val timeStamp = dateFormat.format(Date(System.currentTimeMillis()))

        val status = if (isSendState) 0 else 1
        val toastMsg = if(isSendState) SnackBarMsgConstants.SEND_SUCCESS else SnackBarMsgConstants.RECEIVE_SUCCESS

        // ... api request logic 성공, 실패 나눌 것.

        reducer.sendEffect(CommonEffect.ShowSnackBar(toastMsg))
        delay(3000)
        reducer.sendEffect(DeliveryEffect.OnNavigateToScanScreen)
    }

    private suspend fun processScanBarcode(barcode : String){
        // 바코드 스캔 후 처리 로직
    }
}