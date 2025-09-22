package com.kkh.single.module.template.presentation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.compose.rememberNavController
import com.kkh.pda.BarcodeSdkListener
import com.kkh.pda.BarcodeSdkManager
import com.kkh.single.module.template.presentation.delivery.DeliveryEvent
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.scan.ScanEvent
import com.kkh.single.module.template.presentation.scan.ScanViewModel
import com.kkh.single.module.template.util.navigation.RaasRoute

@Composable
fun RaasApp(
    deliveryViewModel: DeliveryViewModel = hiltViewModel(),
    scanViewModel: ScanViewModel = hiltViewModel(),
    barcodeSdkManager: BarcodeSdkManager
) {
    val navController = rememberNavController()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        barcodeSdkManager.init()
        barcodeSdkManager.addListener(object : BarcodeSdkListener {
            override fun onBarcodeEvent(barcode: String) {
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                // 처리
                Log.d("BARCODE", "Scanned: $barcode\n currentRoute: $currentRoute")
                when {
                    currentRoute == RaasRoute.ScanRoute.toString() -> {
                        scanViewModel.sendEvent(ScanEvent.OnScanBarcode(barcode))
                    }
                    currentRoute?.startsWith("delivery") == true -> {
                        deliveryViewModel.sendEvent(DeliveryEvent.OnScanBarcode(barcode))
                    }
                }
            }
        })
    }

    Scaffold { paddingValues ->
        RaasNavigation(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            deliveryViewModel = deliveryViewModel,
            scanViewModel = scanViewModel
        )
    }
}