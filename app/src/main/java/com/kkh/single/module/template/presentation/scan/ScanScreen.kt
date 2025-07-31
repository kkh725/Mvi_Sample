package com.kkh.single.module.template.presentation.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkh.single.module.template.CommonEffect
import com.kkh.single.module.template.R
import com.kkh.single.module.template.util.DebugClickHandler

@Composable
fun ScanScreen(viewModel: ScanViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.sendEvent(ScanEvent.OnEnterScanScreen)
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is CommonEffect.ShowDialog -> showDialog = effect.isVisible
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleWithHighlight()
        Spacer(Modifier.height(55.dp))
        CustomIconBox(
            text = "약포지",
            bigImageSource = R.drawable.icon_medicine,
            smallImageSource = R.drawable.icon_qr,
            contentDescription = "icon_ScanQR"
        )
        Spacer(Modifier.height(29.dp))
        if (showDialog) {
            DeptSelectionDialog(
                onSelectDept = { dept ->
                    viewModel.sendEvent(ScanEvent.OnCompleteSelectDept(dept))
                }
            )
        }
        Spacer(Modifier.height(24.dp))
        uiState.dept.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun TitleWithHighlight() {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFF345DF0))) {
            append("QR 코드")
        }
        append(" 또는 ")
        withStyle(style = SpanStyle(color = Color(0xFFFFC122))) {
            append("바코드")
        }
        append("를\n스캔해주세요.")
    }
    Text(
        text = annotatedText,
        style = TextStyle(
            fontSize = 32.sp,
            lineHeight = 50.sp,
            fontWeight = FontWeight(800),
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier.testTag("text_barcode_scan")
    )
}

@Composable
fun SmallCustomIcon(
    imgDataSource: Int,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .border(
                width = 3.dp,
                color = Color(0xFFEBF0FF),
                shape = RoundedCornerShape(size = 500.dp)
            )
            .size(60.dp)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 500.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(imgDataSource),
            contentDescription = contentDescription,
            tint = Color.Unspecified
        )
    }
}

@Composable
fun BigCustomIcon(
    text: String,
    imgDataSource: Int,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .background(color = Color(0x1A3560FA), shape = RoundedCornerShape(size = 150.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(imgDataSource),
                contentDescription = contentDescription,
                tint = Color.Unspecified
            )
            Text(text, fontSize = 13.sp)
        }
    }
}

@Composable
fun CustomIconBox(
    text: String,
    bigImageSource: Int,
    smallImageSource: Int,
    contentDescription: String,
) {
    val context = LocalContext.current
    val debugClickHandler = remember { DebugClickHandler(context) }
    var clickCount by rememberSaveable { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .size(140.dp, 135.dp)
            .testTag("box_email")
            .clickable(onClick = {
                clickCount++
                if (clickCount >= 5) {
                    clickCount = 0
                    debugClickHandler.sendLogFileViaEmail(context)
                }
            })
    ) {
        BigCustomIcon(
            text = text,
            imgDataSource = bigImageSource,
            contentDescription = contentDescription
        )
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            SmallCustomIcon(
                imgDataSource = smallImageSource,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
fun DeptSelectionDialog(
    onSelectDept: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "부서를 선택해주세요")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                listOf("약제실", "42병동", "중환자실").forEach { dept ->
                    Text(
                        text = dept,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectDept(dept)
                            }
                            .padding(vertical = 8.dp)
                            .testTag("item_${dept}")
                    )
                }
            }
        },
        confirmButton = {}
    )

}
