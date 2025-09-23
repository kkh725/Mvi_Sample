package com.kkh.single.module.template.presentation.delivery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kkh.single.module.template.R
import com.kkh.single.module.template.data.model.PatientModel
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryEffect
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryEvent
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryState
import com.kkh.single.module.template.util.ButtonMsgConstants
import com.kkh.single.module.template.util.DeliveryMsgConstants

@Composable
internal fun DeliveryRoute(
    onNavigateToScanScreen: () -> Unit,
    viewModel: DeliveryViewModel = hiltViewModel(),
    patientId: String? = null
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendEvent(DeliveryEvent.OnEnterScanScreen(patientId))
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is DeliveryEffect.OnNavigateToScanScreen -> onNavigateToScanScreen()
            }
        }
    }

    DeliveryScreen(
        uiState = uiState,
        onClickRemoveMedicine = { listNo -> viewModel.sendEvent(DeliveryEvent.OnClickRemoveButton(listNo)) },
        onClickDelivery = { viewModel.sendEvent(DeliveryEvent.OnClickDeliveryButton) },
        onClickDialogRemoveButton = { viewModel.sendEvent(DeliveryEvent.OnClickDialogRemoveButton) },
        onClickDialogCancelButton = { viewModel.sendEvent(DeliveryEvent.OnClickDialogCancelButton) }
    )
}

@Composable
private fun DeliveryScreen(
    uiState: DeliveryState,
    onClickRemoveMedicine: (Int) -> Unit,
    onClickDelivery: () -> Unit,
    onClickDialogRemoveButton: () -> Unit,
    onClickDialogCancelButton: () -> Unit
) {
    val buttonText =
        if (uiState.deliveryScreenState == DeliveryState.DeliveryScreenState.Send) ButtonMsgConstants.SEND else ButtonMsgConstants.RECEIVE

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(40.dp))
            CustomRow(R.drawable.icon_representative, DeliveryMsgConstants.DEPT)
            Spacer(Modifier.height(10.dp))
            DeptBox(uiState.dept)
            Spacer(Modifier.height(20.dp))
            Spacer(
                Modifier
                    .height(8.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFEDF1F4))
            )
            Spacer(Modifier.height(20.dp))
            CustomRow(R.drawable.icon_medicine, DeliveryMsgConstants.PATIENT_NO)
            Spacer(Modifier.height(10.dp))
            PatientListBox(
                modifier = Modifier.weight(1f),
                patientList = uiState.patientList,
                onClickRemoveMedicine = onClickRemoveMedicine
            )
            Spacer(Modifier.height(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = uiState.patientList.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                onClick = onClickDelivery
            ) {
                Text(buttonText)
            }
            Spacer(Modifier.height(10.dp))
        }

        AnimatedVisibility(uiState.warnDialogState) {
            ConfirmDeleteDialog(
                // 실제 삭제 로직
                onConfirm = onClickDialogRemoveButton,
                onCancel = onClickDialogCancelButton
            )
        }
    }
}

@Composable
private fun CustomRow(imgResource: Int, text: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(imgResource),
            contentDescription = null,
            tint = Color(0xFF345DF0),
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF345DF0),
            )
        )
    }
}

@Composable
private fun DeptBox(dept: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFFEDF1F4),
                    shape = RoundedCornerShape(size = 4.dp)
                )
                .padding(start = 15.dp, top = 5.dp, end = 15.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                dept,
                fontSize = 14.sp,
                color = Color(0xFF7B96F5),
                fontWeight = FontWeight(500),
                modifier = Modifier.weight(1f)
            )
        }
    }

}

@Composable
private fun PatientListBox(
    modifier: Modifier = Modifier,
    patientList: List<PatientModel>,
    onClickRemoveMedicine: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        AnimatedVisibility(visible = patientList.isEmpty()) {
            EmptyBox("바코드를 모두 스캔해주세요.")
        }

        AnimatedVisibility(visible = patientList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(
                    patientList,
                    key = { _, item -> item.patientId }) { index, patientModel ->
                    PatientListItem(
                        medicineNo = index,
                        patientModel = patientModel,
                        onClickRemoveMedicine = onClickRemoveMedicine,
                        modifier = Modifier.animateItem() // ✅ 애니메이션 적용
                    )
                }
            }
        }
    }
}


@Composable
private fun PatientListItem(
    medicineNo: Int,
    patientModel: PatientModel,
    onClickRemoveMedicine: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = Color(0xFFEDF1F4), shape = RoundedCornerShape(size = 4.dp))
            .padding(start = 15.dp, top = 5.dp, end = 15.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberIcon(number = medicineNo)
        Spacer(Modifier.width(20.dp))
        Text(
            patientModel.patientId,
            fontSize = 14.sp,
            modifier = Modifier.weight(8f),
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.width(20.dp))
        Text(
            patientModel.dept,
            fontSize = 14.sp,
            modifier = Modifier.weight(8f),
            textAlign = TextAlign.Start
        )
        Icon(
            Icons.Filled.Close,
            contentDescription = "icon_delete",
            modifier = Modifier.clickable { onClickRemoveMedicine(medicineNo) }
        )
    }
}

@Composable
private fun NumberIcon(number: Int) {
    Box(
        modifier = Modifier
            .size(24.dp) // 적당한 크기 조절
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(50) // 완전한 원
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun EmptyBox(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .testTag("box_empty_medicine")
            .fillMaxWidth()
            .height(32.dp)
            .background(
                color = Color(0xFFF6F2EB),
                shape = RoundedCornerShape(size = 100.dp)
            )
            .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_bulb),
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 12.sp)
    }
}

@Composable
private fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        shape = RoundedCornerShape(12.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "스캔 취소",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
            )
        },
        text = {
            Text(
                text = "스캔을 취소하시겠습니까?",
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color(0xFF444444)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4B688D)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(42.dp)
            ) {
                Text(text = "예", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onCancel,
                border = BorderStroke(1.dp, Color(0xFF4B688D)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(42.dp)
            ) {
                Text(
                    text = "아니오",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B688D)
                )
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun DeliveryContentPreview() {
    DeliveryScreen(
        uiState = DeliveryState(),
        onClickRemoveMedicine = { index -> /* 아무 행동 없음 */ },
        onClickDelivery = { /* 아무 행동 없음 */ },
        onClickDialogCancelButton = {},
        onClickDialogRemoveButton = {}
    )
}