package com.kkh.single.module.template.presentation.delivery

import android.content.ContentValues.TAG
import android.util.Log
import com.kkh.single.module.template.domain.model.PatientModel
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryEffect
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryEvent
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryState
import com.kkh.single.module.template.presentation.scan.ScanContract.ScanEffect
import com.kkh.single.module.template.util.DeptMsgConstants
import com.kkh.single.module.template.util.SnackBarMsgConstants
import com.kkh.single.module.template.util.common.BaseViewModel
import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.EffectHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class DeliveryViewModel @Inject constructor(
    private val repository: MainRepository,
    private val effectHelper: EffectHelper
) : BaseViewModel<DeliveryState, DeliveryEvent, DeliveryEffect>() {

    override fun createInitialState(): DeliveryState {
        return DeliveryState()
    }

    override suspend fun handleEvent(event: DeliveryEvent) {
        when (event) {
            // 화면 진입 시 수행.
            is DeliveryEvent.OnEnterScanScreen -> {
                checkAndHandleDeptState()
                event.patientId?.let {
                    processGetPatientInfo(it)
                }
            }

            // 보내기/받기 (배송) 버튼 클릭 시 수행
            is DeliveryEvent.OnClickDeliveryButton -> {
                processRequestDelivery()
            }

            // 바코드 스캔 시 수행
            is DeliveryEvent.OnScanBarcode -> {
                processScanBarcode(event.barcode)
            }

            // 스캔 되어있는 환자 X 버튼 클릭시 수행
            is DeliveryEvent.OnClickRemoveButton -> {
                reduce { copy(removeWarnDialogState = true, selectedIndexForDelete = event.listNo) }
            }

            // 실제 로컬 삭제 로직.
            is DeliveryEvent.OnClickDialogRemoveButton -> {
                processRemoveLocalPatientInfo(currentUiState.selectedIndexForDelete)
            }

            // 삭제하시겠습니까? dialog cancel 버튼 클릭시 수행
            DeliveryEvent.OnClickDialogCancelButton -> {
                reduce { copy(removeWarnDialogState = false) }
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

        // 로컬에 저장된 dept가 없는경우 scan으로 보냄.
        if (dept.isEmpty()) {
            postSideEffect(DeliveryEffect.OnNavigateToScanScreen)
        } else {
            if (dept == DeptMsgConstants.MEDICINE_ROOM) {
                reduce { copy(deliveryScreenState = DeliveryState.DeliveryScreenState.Send, dept = dept) }
            } else {
                reduce { copy(deliveryScreenState = DeliveryState.DeliveryScreenState.Receive, dept = dept) }
            }
        }
    }

    private suspend fun processGetPatientInfo(patientId: String) {
        repository.getPatientInfo(patientId)
            .onSuccess {
                val newPatientList = listOf(PatientModel(patientId = patientId, dept = "병동"))
                reduce { copy(patientList = newPatientList) }
            }
            .onFailure { throwable ->
                Log.e(TAG, "getPatientInfo: ${throwable.message}")
            }
    }

    private suspend fun processRequestDelivery() {
        val isSendState = state.value.deliveryScreenState == DeliveryState.DeliveryScreenState.Send

        repository.sendDeliveryInfo(
            patientList = currentUiState.patientList,
            deliveryState = currentUiState.deliveryScreenState,
            dprtDept = if (isSendState) currentUiState.dept else "",
            arvlDept = if (!isSendState) currentUiState.dept else ""
        )
            .onSuccess {
                reduce { copy(completeDialogState = true) }
                delay(3000)
                reduce { DeliveryState() }
                postSideEffect(DeliveryEffect.OnNavigateToScanScreen)
            }.onFailure { throwable ->
                effectHelper.postCommonEffect(CommonEffect.ShowSnackBar(throwable.message ?: ""))
            }
    }

    private fun processRemoveLocalPatientInfo(selectedIndexForDelete: Int) {

        val newList =
            currentUiState.patientList.toMutableList().apply { removeAt(selectedIndexForDelete) }

        reduce {
            copy(
                removeWarnDialogState = false,
                selectedIndexForDelete = -1,
                patientList = newList
            )
        }
    }

    private suspend fun processScanBarcode(barcode: String) {
        // 바코드 스캔 후 처리 로직
        if (barcode == "fail") {
            effectHelper.postCommonEffect(CommonEffect.ShowSnackBar(SnackBarMsgConstants.INVALID_BARCODE))
        }else{
            repository.getPatientInfo(barcode)
                .onSuccess {
                    reduce { copy() }
                }
                .onFailure { throwable ->
                    Log.e(TAG, "getPatientInfo: ${throwable.message}")
                }
        }
    }
}