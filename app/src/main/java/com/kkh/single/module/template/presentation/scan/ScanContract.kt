package com.kkh.single.module.template.presentation.scan

import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.SideEffect
import com.kkh.single.module.template.util.common.UiEvent
import com.kkh.single.module.template.util.common.UiState

class ScanContract {
    data class ScanState(
        val dept : String = "",
        val deptSelectionDialogState : Boolean = false
    ) : UiState {
        companion object {
            val init = ScanState()
        }
    }

    sealed class ScanEvent : UiEvent {
        data object OnEnterScanScreen : ScanEvent()
        data class OnCompleteSelectDept(val dept : String) : ScanEvent()
        data class OnScanBarcode(val barcode: String) : ScanEvent()
    }

    sealed interface ScanEffect : SideEffect {
        data class OnNavigateToDeliveryScreen(val patientId : String) : ScanEffect
    }
}
