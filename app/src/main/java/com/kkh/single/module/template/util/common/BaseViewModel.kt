package com.kkh.single.module.template.util.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : SideEffect> : ViewModel() {

    private val initialUiState: State by lazy { createInitialState() }
    protected abstract fun createInitialState(): State
    protected abstract suspend fun handleEvent(event: Event) // 내부 event 처리

    private val _state = MutableStateFlow(initialUiState)
    val state = _state.asStateFlow()

    private val _sideEffect: Channel<Effect> = Channel()
    val sideEffect = _sideEffect.receiveAsFlow()

    protected val currentUiState: State
        get() = state.value

    /**
     * action이 발생하면 event 전달
     * Event - 명확한 행동 명을 우선으로 작명.
     */
    fun sendEvent(event: Event) {
        viewModelScope.launch { handleEvent(event) }
    } // 외부 event 처리

    /**
     * reduce : 상태 값 Update
     */
    protected fun reduce(reduce: State.() -> State) {
        _state.update(reduce)
    }

    /**
     * SideEffect가 발생하면 이벤트 전달
     */
    protected fun postSideEffect(sideEffect: Effect) {
        viewModelScope.launch { _sideEffect.send(sideEffect) }
    }

    override fun onCleared() {
        super.onCleared()
        _sideEffect.close()
    }
}
