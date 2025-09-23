package com.kkh.single.module.template.presentation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kkh.pda.BarcodeSdkListener
import com.kkh.pda.BarcodeSdkManager
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryEvent
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.scan.ScanContract.ScanEvent
import com.kkh.single.module.template.presentation.scan.ScanViewModel
import com.kkh.single.module.template.util.common.SnackbarComponent
import com.kkh.single.module.template.util.navigation.RaasRoute

@Composable
fun RaasApp(
    deliveryViewModel: DeliveryViewModel = hiltViewModel(),
    scanViewModel: ScanViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    barcodeSdkManager: BarcodeSdkManager
) {
    Scaffold(snackbarHost = {
        SnackbarComponent(
            snackbarHostState = snackbarHostState
        )
    }) { paddingValues ->
        RaasNavigation(
            modifier = Modifier.padding(paddingValues),
            barcodeSdkManager = barcodeSdkManager,
            deliveryViewModel = deliveryViewModel,
            scanViewModel = scanViewModel
        )
    }
}