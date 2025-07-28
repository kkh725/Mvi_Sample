package com.kkh.single.module.template.presentation.scan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkh.single.module.template.R

@Composable
fun ScanScreen() {

    //약제실인지 병동인지. 보내기받기 설정
//    val isMedicineRoomText = if (viewModel.getIsMedicineRoom()) {
//        "약제실"
//    } else {
//        "병동"
//    }

    var selectMedicineRoom by remember { mutableStateOf(false) }


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

//    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
//        Text(isMedicineRoomText)
//    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = annotatedText,
            style = TextStyle(
                fontSize = 32.sp,
                lineHeight = 50.sp,
                fontWeight = FontWeight(800),
                textAlign = TextAlign.Center,
            ),
        )
        Spacer(Modifier.height(55.dp))
        CustomIconBox(
            text = "약포지",
            bigImageSource = R.drawable.icon_medicine,
            smallImageSource = R.drawable.icon_qr,
            contentDescription = "icon_ScanQR"
        )
        Spacer(Modifier.height(29.dp))
//        CustomIconBox(
//            text = "담당자",
//            bigImageSource = R.drawable.icon_representative,
//            smallImageSource = R.drawable.icon_barcode,
//            contentDescription = "icon_ScanBarcode"
//        )
    }

    AnimatedVisibility(selectMedicineRoom) {
        Box(
            Modifier
                .fillMaxSize()
                .clickable(enabled = false) { })


    }
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
    contentDescription: String
) {
    Box(modifier = Modifier.size(140.dp, 135.dp)) {
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

