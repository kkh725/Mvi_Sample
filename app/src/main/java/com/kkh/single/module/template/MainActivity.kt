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
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.scan.ScanViewModel
import com.kkh.single.module.template.presentation.ui.theme.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val barcodeManager: BarcodeSdkManager = PMBarcodeManagerImpl(this)
    val mainViewModel: MainViewModel by viewModels()

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            val deliveryViewModel: DeliveryViewModel = hiltViewModel()
            val scanViewModel: ScanViewModel = hiltViewModel()
            scanViewModel.uiState

            barcodeManager.init()
            barcodeManager.addListener(object : BarcodeSdkListener {
                override fun onBarcodeEvent(barcode: String) {
                    // 처리
                    Log.d("BARCODE", "Scanned: $barcode")
                    mainViewModel.sendEvent(MainEvent.OnScanBarcode(barcode))
                }
            })

            MainTheme {
                RaasApp(
                    mainViewModel = mainViewModel,
                    deliveryViewModel = deliveryViewModel,
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeManager.destroy()
    }
}