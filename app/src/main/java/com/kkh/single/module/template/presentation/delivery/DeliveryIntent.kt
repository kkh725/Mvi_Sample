package com.kkh.single.module.template.presentation.delivery

import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.Reducer
import com.kkh.single.module.template.util.common.SideEffect
import com.kkh.single.module.template.util.common.UiEvent
import com.kkh.single.module.template.util.common.UiState
import com.kkh.single.module.template.data.model.PatientModel
import javax.inject.Inject
import kotlin.String

data class DeliveryState(
    val dept : String,
    val patientList: List<PatientModel>
) : UiState {
    companion object {
        val init = DeliveryState(
            dept  = "",
            patientList = listOf(PatientModel("1234","병동1"),
                PatientModel("2234","병동2"),
                PatientModel("3234","병동3")))
    }
}

sealed class DeliveryEvent : UiEvent {
    data class OnEnterScanScreen(val patientId : String?) : DeliveryEvent()
    data class OnClickRemovePatient(val listNo : Int) : DeliveryEvent()
}

sealed class DeliveryEffect : SideEffect {
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