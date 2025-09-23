package com.kkh.single.module.template.presentation.delivery

import com.kkh.single.module.template.util.common.SideEffect
import com.kkh.single.module.template.util.common.UiEvent
import com.kkh.single.module.template.util.common.UiState
import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.util.DeptMsgConstants.MEDICINE_ROOM
import kotlin.String

class DeliveryContract {
    data class DeliveryState(
        val dept : String = MEDICINE_ROOM,
        val patientList: List<PatientModel> = PatientModel.mockList,
        val deliveryScreenState: DeliveryScreenState = DeliveryScreenState.Send,
        val selectedIndexForDelete : Int = -1,
        val removeWarnDialogState : Boolean = false,
        val completeDialogState : Boolean = false
    ) : UiState {
        enum class DeliveryScreenState{
            Send, Receive
        }
    }

    sealed interface DeliveryEvent : UiEvent {
        data class OnEnterScanScreen(val patientId : String?) : DeliveryEvent
        // x 버튼 클릭하여 toast 띄우기.
        data class OnClickRemoveButton(val listNo : Int) : DeliveryEvent
        // 실제 삭제 동작
        data object OnClickDialogRemoveButton : DeliveryEvent
        data object OnClickDeliveryButton : DeliveryEvent
        data object OnClickDialogCancelButton : DeliveryEvent
        data class OnScanBarcode(val barcode : String) : DeliveryEvent

    }

    sealed interface DeliveryEffect : SideEffect {
        data object OnNavigateToScanScreen : DeliveryEffect
    }
}