package com.kkh.single.module.template

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pda.BarcodeSdkListener
import com.example.pda.BarcodeSdkManager
import com.example.pda.PMBarcodeManagerImpl
import com.kkh.single.module.template.ui.RaasApp
import com.kkh.single.module.template.ui.theme.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val barcodeManager: BarcodeSdkManager = PMBarcodeManagerImpl(this)

    override fun onStart() {
        super.onStart()

        barcodeManager.init()
        barcodeManager.addListener(object : BarcodeSdkListener {
            override fun onBarcodeEvent(barcode: String) {
                // 처리
                Log.d("BARCODE", "Scanned: $barcode")
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MainTheme {
                RaasApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeManager.destroy()
    }
}