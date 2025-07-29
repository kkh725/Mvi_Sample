package com.kkh.single.module.template

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kkh.pda.BarcodeSdkListener
import com.kkh.pda.BarcodeSdkManager
import com.kkh.pda.pointmobile.PMBarcodeManagerImpl
import com.kkh.single.module.template.ui.RaasApp
import com.kkh.single.module.template.ui.theme.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val barcodeManager: BarcodeSdkManager = PMBarcodeManagerImpl(this)
    private val mainViewModel: MainViewModel by viewModels()

    override fun onStart() {
        super.onStart()

        barcodeManager.init()
        barcodeManager.addListener(object : BarcodeSdkListener {
            override fun onBarcodeEvent(barcode: String) {
                // 처리
                Log.d("BARCODE", "Scanned: $barcode")
                mainViewModel.sendEvent(MainEvent.OnScanBarcode(barcode))
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MainTheme {
                RaasApp(mainViewModel = mainViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeManager.destroy()
    }
}