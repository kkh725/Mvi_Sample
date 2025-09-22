package com.kkh.single.module.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kkh.pda.BarcodeSdkManager
import com.kkh.pda.pointmobile.PMBarcodeManagerImpl
import com.kkh.single.module.template.presentation.RaasApp
import com.kkh.single.module.template.presentation.ui.theme.MainTheme
import com.kkh.single.module.template.util.common.EffectHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val barcodeManager: BarcodeSdkManager = PMBarcodeManagerImpl(this)
    @Inject
    lateinit var effectHelper: EffectHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MainTheme {
                RaasApp(barcodeSdkManager = barcodeManager)
            }
        }

        effectHelper.effectFlow.collect { effect ->

        }
    }



    override fun onDestroy() {
        super.onDestroy()
        barcodeManager.destroy()
    }
}