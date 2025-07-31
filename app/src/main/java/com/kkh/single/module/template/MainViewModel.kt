package com.kkh.single.module.template

import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.util.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseMviViewModel<MainState, MainEvent, SideEffect>(
    reducer = MainReducer(MainState.empty)
) {

    override suspend fun onEventAfterReduce(event: MainEvent) {
        super.onEventAfterReduce(event)

        when(event){
            is MainEvent.OnScanBarcode -> {
                apiCall()
            }
            else -> {}
        }
    }

    private fun apiCall() {
        if (true) {
            reducer.sendEffect(CommonEffect.NavigateTo(ScanRoute.route))
        }
    }
}