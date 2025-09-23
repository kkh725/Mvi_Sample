package com.kkh.single.module.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.kkh.pda.BarcodeSdkManager
import com.kkh.pda.pointmobile.PMBarcodeManagerImpl
import com.kkh.single.module.template.presentation.RaasApp
import com.kkh.single.module.template.presentation.ui.theme.MainTheme
import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.util.common.EffectHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            val snackbarHostState = remember { SnackbarHostState() }
            MainTheme {
                RaasApp(
                    barcodeSdkManager = barcodeManager,
                    snackbarHostState = snackbarHostState
                )
            }

            // 공통 sideEffect 정의
            LaunchedEffect(Unit) {
                effectHelper.effectFlow.collect { effect ->
                    when (effect) {
                        is CommonEffect.ShowSnackBar -> {
                            snackbarHostState.showSnackbar(effect.text)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeManager.destroy()
    }
}