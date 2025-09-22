package com.kkh.single.module.template.presentation.scan

import android.content.ContentValues.TAG
import android.util.Log
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryEffect
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryEvent
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryState
import com.kkh.single.module.template.util.SnackBarMsgConstants
import com.kkh.single.module.template.util.common.BaseViewModel
import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.EffectHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val repository: MainRepository,
    private val effectHelper: EffectHelper
) :
    BaseViewModel<DeliveryState, DeliveryEvent, DeliveryEffect>() {

    override fun createInitialState(): DeliveryState {
        return DeliveryState.init
    }

    override suspend fun handleEvent(event: DeliveryEvent) {
        when (event) {
            is DeliveryEvent.OnEnterScanScreen -> {
                processDeptState()
            }

//            is DeliveryEvent.OnClickDeliveryButton -> {
//                repository.fetchDept(event.dept)
//            }

            is DeliveryEvent.OnScanBarcode -> {
                processScanBarcode(event.barcode)
            }

            else -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    private suspend fun processDeptState() {
        val dept = repository.getDept()

        if (dept.isEmpty()) { // 아예 초기 인 상태
            effectHelper.postCommonEffect(CommonEffect.ShowDialog)
        } else {
            reduce { copy(dept = dept) }
        }
    }

    private fun processScanBarcode(barcode: String) {
        // ... api request
//        reducer.sendEffect(ScanEffect.OnNavigateToDeliveryScreen(barcode))

        if (barcode == "fail") {
            effectHelper.postCommonEffect(CommonEffect.ShowSnackBar(SnackBarMsgConstants.INVALID_BARCODE))
        }
    }

    private suspend fun setUserInfo() {
        repository.setUserInfo("test")
            .onSuccess {

            }.onFailure { throwable ->
                Log.e(TAG, "setUserInfo: ${throwable.message}")
            }
    }
}