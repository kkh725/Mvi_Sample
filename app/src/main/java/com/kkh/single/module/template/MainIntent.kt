package com.kkh.single.module.template

import com.kkh.single.module.template.util.ScreenState
import javax.inject.Inject

sealed class MainEvent : UiEvent {
    data class OnScreenStateChange(val screenState: ScreenState) : MainEvent()
    data class OnScanBarcode(val barcode: String) : MainEvent()
}

sealed class MainEffect : SideEffect {
    data class ShowPopup(val message: String) : MainEffect()
}

class MainReducer @Inject constructor(state: MainState) : Reducer<MainState, MainEvent, MainEffect>(state) {
    override suspend fun reduce(oldState: MainState, event: MainEvent) {
        when (event) {
            is MainEvent.OnScreenStateChange -> {
                setState(oldState.copy(screenState = event.screenState))
            }
            is MainEvent.OnScanBarcode -> {
                if (event.barcode == "fail"){
                    sendEffect(MainEffect.ShowPopup("잘못된 바코드 입니다."))
                }
            }
        }
    }
}

