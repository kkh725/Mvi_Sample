package com.kkh.single.module.template

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import com.kkh.pda.BarcodeSdkListener
import com.kkh.pda.BarcodeSdkManager
import com.kkh.pda.pointmobile.PMBarcodeManagerImpl
import com.kkh.single.module.template.presentation.RaasApp
import com.kkh.single.module.template.presentation.delivery.DeliveryEvent
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.scan.ScanEvent
import com.kkh.single.module.template.presentation.scan.ScanScreen
import com.kkh.single.module.template.presentation.scan.ScanViewModel
import com.kkh.single.module.template.presentation.ui.theme.MainTheme
import com.kkh.single.module.template.util.common.CommonEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val barcodeManager: BarcodeSdkManager = PMBarcodeManagerImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MainTheme {
                RaasApp(barcodeSdkManager = barcodeManager)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeManager.destroy()
    }
}