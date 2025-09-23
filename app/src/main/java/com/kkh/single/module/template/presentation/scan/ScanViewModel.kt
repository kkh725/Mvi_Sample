package com.kkh.single.module.template.presentation.scan

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.presentation.scan.ScanContract.ScanState
import com.kkh.single.module.template.presentation.scan.ScanContract.ScanEvent
import com.kkh.single.module.template.presentation.scan.ScanContract.ScanEffect
import com.kkh.single.module.template.util.SnackBarMsgConstants
import com.kkh.single.module.template.util.common.BaseViewModel
import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.EffectHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val repository: MainRepository,
    private val effectHelper: EffectHelper
) :
    BaseViewModel<ScanState, ScanEvent, ScanEffect>() {

    override fun createInitialState(): ScanState {
        return ScanState.init
    }

    override suspend fun handleEvent(event: ScanEvent) {
        when (event) {
            // 화면 최초 진입.
            is ScanEvent.OnEnterScanScreen -> {
                processDeptState()
            }

            // 바코드 스캔 이벤트
            is ScanEvent.OnScanBarcode -> {
                processScanBarcode(event.barcode)
            }

            // 초기 부서 선택.
            is ScanEvent.OnCompleteSelectDept -> {
                setLocalDeptState(event.dept)
            }
        }
    }

    private suspend fun processDeptState() {
        val dept = repository.getDept()

        if (dept.isEmpty()) { // 아예 초기 인 상태
            reduce { copy(deptSelectionDialogState = true) }
        } else {
            reduce { copy(dept = dept) }
        }
    }

    private suspend fun processScanBarcode(barcode: String) {
        if (barcode == "fail") {
            effectHelper.postCommonEffect(CommonEffect.ShowSnackBar(SnackBarMsgConstants.INVALID_BARCODE))
        }else{
            repository.getPatientInfo(barcode)
                .onSuccess {
                    postSideEffect(ScanEffect.OnNavigateToDeliveryScreen(barcode))
                }
                .onFailure { throwable ->
                    Log.e(TAG, "getPatientInfo: ${throwable.message}")
                }
        }
    }

    private fun setLocalDeptState(dept: String){
        viewModelScope.launch {
            repository.fetchDept(dept)
            reduce { copy(deptSelectionDialogState = false, dept = dept) }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}