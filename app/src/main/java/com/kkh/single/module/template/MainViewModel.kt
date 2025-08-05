package com.kkh.single.module.template

import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.util.common.BaseMviViewModel
import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.SideEffect
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
                apiCall(event.barcode)
            }
            else -> {}
        }
    }

    private fun apiCall(barcode : String) {
        // ... api request
        reducer.sendEffect(MainEffect.OnNavigateToDeliveryScreen(barcode))

    }
}