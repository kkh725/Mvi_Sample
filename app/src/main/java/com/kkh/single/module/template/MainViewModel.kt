package com.kkh.single.module.template

import androidx.core.app.PendingIntentCompat.send
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkh.single.module.template.presentation.scan.ScanRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val reducer = MainReducer(MainState.empty)
    val uiState get() = reducer.uiState
    val sideEffect get() = reducer.effect

    fun sendEvent(event: MainEvent) {
        viewModelScope.launch {
            reducer.sendEvent(event)

            when(event){
                is MainEvent.OnScanBarcode ->{
                    if (event.barcode != "READ_FAIL"){
                        apiCall()
                    }
                }
                else -> {}
            }
        }
    }

    private fun apiCall(){
        if (true){
            reducer.sendEffect(CommonEffect.NavigateTo(ScanRoute.route))
        }
    }
}