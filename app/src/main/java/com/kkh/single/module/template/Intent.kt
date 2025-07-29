package com.kkh.single.module.template

import com.kkh.single.module.template.util.ScreenState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow


abstract class Reducer<S : UiState, E : UiEvent, F : SideEffect>(initialState: S) {
    private val _uiState = MutableStateFlow(initialState)
    val uiState get() = _uiState.asStateFlow()

    private val _effect = Channel<F>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    suspend fun sendEvent(event: E) {
        reduce(_uiState.value, event)
    }

    fun setState(newState: S) {
        _uiState.value = newState
    }

    fun sendEffect(effect: F) {
        _effect.trySend(effect)
    }

    abstract suspend fun reduce(oldState: S, event: E)
}

interface UiState

interface UiEvent

interface SideEffect

