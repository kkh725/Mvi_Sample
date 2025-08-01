package com.kkh.single.module.template.presentation.scan

import com.kkh.single.module.template.CommonEffect
import com.kkh.single.module.template.Reducer
import com.kkh.single.module.template.SideEffect
import com.kkh.single.module.template.UiEvent
import com.kkh.single.module.template.UiState
import javax.inject.Inject

data class ScanState(
    val dept : String = ""
) : UiState {
    companion object {
        val init = ScanState()
    }
}

sealed class ScanEvent : UiEvent {
    data object OnEnterScanScreen : ScanEvent()
    data class OnCompleteSelectDept(val dept : String) : ScanEvent()
}

sealed class ScanEffect : SideEffect {
}

class ScanReducer @Inject constructor(state: ScanState) : Reducer<ScanState, ScanEvent, SideEffect>(state) {
    override suspend fun reduce(oldState: ScanState, event: ScanEvent) {
        when (event) {
            is ScanEvent.OnCompleteSelectDept -> {

                sendEffect(CommonEffect.ShowDialog(false))

                val newState = oldState.copy(dept = event.dept)
                setState(newState)
            }
            else -> {}
        }
    }
}