package com.kkh.single.module.template.util.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<State : UiState, Event : UiEvent, Effect : SideEffect>(
    protected val reducer: Reducer<State, Event, Effect>
) : ViewModel() {

    val uiState get() = reducer.uiState
    val sideEffect get() = reducer.effect

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            reducer.sendEvent(event)
            onEventAfterReduce(event)
        }
    }

    fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            reducer.sendEffect(effect)
        }
    }

    /**
     * 상태변경 이후 실행되는 비즈니스 로직
     * 불가피하다면 이쪽에서 상태변경도 허용.
     */
    open suspend fun onEventAfterReduce(event: Event) {}
}
