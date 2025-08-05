package com.kkh.single.module.template.presentation.scan

import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.util.SnackBarMsgConstants
import com.kkh.single.module.template.util.common.BaseMviViewModel
import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val repository: MainRepository) :
    BaseMviViewModel<ScanState, ScanEvent, SideEffect>(reducer = ScanReducer(ScanState.init)) {

    override suspend fun onEventAfterReduce(event: ScanEvent) {
        super.onEventAfterReduce(event)

        when (event) {
            is ScanEvent.OnEnterScanScreen -> {
                processDeptState()
            }

            is ScanEvent.OnCompleteSelectDept -> {
                repository.fetchDept(event.dept)
            }

            is ScanEvent.OnScanBarcode -> {
                processScanBarcode(event.barcode)
            }
        }
    }

    private suspend fun processDeptState() {
        val dept = repository.getDept()

        if (dept.isEmpty()) { // 아예 초기 인 상태
            reducer.sendEffect(CommonEffect.ShowDialog(true))
        } else {
            reducer.setState(uiState.value.copy(dept = dept))
        }
    }

    private fun processScanBarcode(barcode : String) {
        // ... api request
//        reducer.sendEffect(ScanEffect.OnNavigateToDeliveryScreen(barcode))

        if (barcode == "fail"){
            reducer.sendEffect(CommonEffect.ShowSnackBar(SnackBarMsgConstants.INVALID_BARCODE))
        }

    }
}