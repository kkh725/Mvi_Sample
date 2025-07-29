package com.kkh.single.module.template.presentation.scan

import com.kkh.single.module.template.Reducer
import com.kkh.single.module.template.SideEffect
import com.kkh.single.module.template.UiEvent
import com.kkh.single.module.template.UiState
import javax.inject.Inject

data class ScanState(
    val dept : String = ""
) : UiState {
    companion object {
        val empty = ScanState()
    }
}

sealed class ScanEvent : UiEvent {
    data object OnEnterScanScreen : ScanEvent()
    data class OnCompleteSelectDept(val dept : String) : ScanEvent()
}

sealed class ScanEffect : SideEffect {
    data class ShowDialog(val show : Boolean) : ScanEffect()
}

class ScanReducer @Inject constructor(state: ScanState) : Reducer<ScanState, ScanEvent, ScanEffect>(state) {
    override suspend fun reduce(oldState: ScanState, event: ScanEvent) {
        when (event) {
            is ScanEvent.OnCompleteSelectDept -> {

                sendEffect(ScanEffect.ShowDialog(false))

                val newState = oldState.copy(dept = event.dept)
                setState(newState)
            }
            else -> {}
        }
    }
}