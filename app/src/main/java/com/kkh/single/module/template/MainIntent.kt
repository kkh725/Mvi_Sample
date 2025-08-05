package com.kkh.single.module.template

import com.kkh.single.module.template.presentation.scan.ScanEffect
import com.kkh.single.module.template.util.ScreenState
import com.kkh.single.module.template.util.SnackBarMsgConstants
import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.Reducer
import com.kkh.single.module.template.util.common.SideEffect
import com.kkh.single.module.template.util.common.UiEvent
import com.kkh.single.module.template.util.common.UiState
import javax.inject.Inject


data class MainState(
    val screenState : ScreenState
) : UiState {
    companion object {
        val empty = MainState(
            screenState = ScreenState.Scan
        )
    }
}

sealed class MainEvent : UiEvent {
    data class OnScreenStateChange(val screenState: ScreenState) : MainEvent()
    data class OnScanBarcode(val barcode: String) : MainEvent()
}

sealed class MainEffect : SideEffect {
    data class OnNavigateToDeliveryScreen(val patientId : String) : MainEffect()
}

class MainReducer @Inject constructor(state: MainState) : Reducer<MainState, MainEvent, SideEffect>(state) {
    override suspend fun reduce(oldState: MainState, event: MainEvent) {
        when (event) {
            is MainEvent.OnScreenStateChange -> {
                setState(oldState.copy(screenState = event.screenState))
            }
            is MainEvent.OnScanBarcode -> {
                if (event.barcode == "fail"){
                    sendEffect(CommonEffect.ShowSnackBar(SnackBarMsgConstants.INVALID_BARCODE))
                }
            }
        }
    }
}

