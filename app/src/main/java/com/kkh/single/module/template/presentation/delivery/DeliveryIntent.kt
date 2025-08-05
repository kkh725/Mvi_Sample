package com.kkh.single.module.template.presentation.delivery

import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.Reducer
import com.kkh.single.module.template.util.common.SideEffect
import com.kkh.single.module.template.util.common.UiEvent
import com.kkh.single.module.template.util.common.UiState
import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.util.DeliveryScreenState
import javax.inject.Inject
import kotlin.String

data class DeliveryState(
    val dept : String,
    val patientList: List<PatientModel>,
    val deliveryScreenState: DeliveryScreenState
) : UiState {
    companion object {
        val init = DeliveryState(
            dept  = "",
            patientList = PatientModel.mockList,
            deliveryScreenState = DeliveryScreenState.Receive)
    }
}

sealed class DeliveryEvent : UiEvent {
    data class OnEnterScanScreen(val patientId : String?) : DeliveryEvent()
    data class OnClickRemovePatient(val listNo : Int) : DeliveryEvent()
    data object OnClickDeliveryButton : DeliveryEvent()
    data class OnScanBarcode(val barcode : String) : DeliveryEvent()
}

sealed class DeliveryEffect : SideEffect {
    data object OnNavigateToScanScreen : DeliveryEffect()
}

class DeliveryReducer @Inject constructor(state: DeliveryState) : Reducer<DeliveryState, DeliveryEvent, SideEffect>(state) {
    override suspend fun reduce(oldState: DeliveryState, event: DeliveryEvent) {
        when (event) {
            is DeliveryEvent.OnClickRemovePatient -> {
                val newPatientList = oldState.patientList.toMutableList()
                newPatientList.removeAt(event.listNo)

                setState(oldState.copy(patientList = newPatientList))
                sendEffect(CommonEffect.ShowDialog(false))
            }

            else -> {}
        }
    }
}