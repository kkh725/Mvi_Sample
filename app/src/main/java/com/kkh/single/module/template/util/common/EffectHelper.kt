package com.kkh.single.module.template.util.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EffectHelper @Inject constructor() {
    private val _effectFlow = Channel<CommonEffect>(BUFFERED)
    val effectFlow = _effectFlow.receiveAsFlow()

    // 즉시 반환. 버퍼가 꽉 찼다면 실패할 수 있음.
    fun postCommonEffect(event: CommonEffect) {
        _effectFlow.trySend(event)
    }
}

sealed interface CommonEffect : UiEvent{
    data class ShowSnackBar(val text : String) : CommonEffect
    data object HideSnackBar : CommonEffect
    data class ShowBottomSheet(val bottomSheetContent : @Composable () -> Unit) : CommonEffect
    data object HideBottomSheet : CommonEffect
    data object ShowDialog : CommonEffect
    data object HideDialog : CommonEffect
}