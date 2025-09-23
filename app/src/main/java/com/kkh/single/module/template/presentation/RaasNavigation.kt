package com.kkh.single.module.template.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kkh.pda.BarcodeSdkListener
import com.kkh.pda.BarcodeSdkManager
import com.kkh.single.module.template.presentation.delivery.DeliveryContract.DeliveryEvent
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.scan.ScanContract.ScanEvent
import com.kkh.single.module.template.presentation.scan.ScanViewModel
import com.kkh.single.module.template.util.navigation.RaasGraphBaseRoute

@Composable
fun RaasNavigation(
    modifier: Modifier = Modifier,
    barcodeSdkManager: BarcodeSdkManager,
    scanViewModel: ScanViewModel,
    deliveryViewModel: DeliveryViewModel
) {

    val navController = rememberNavController()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        barcodeSdkManager.init()
        barcodeSdkManager.addListener(object : BarcodeSdkListener {
            override fun onBarcodeEvent(barcode: String) {
                val route = navController.currentBackStackEntry?.destination?.route
                // 처리
                Log.d("BARCODE", "Scanned: $barcode\n currentRoute: $route")
                when {
                    route?.contains("ScanRoute") == true -> scanViewModel.sendEvent(ScanEvent.OnScanBarcode(barcode))
                    route?.contains("DeliveryRoute") == true -> deliveryViewModel.sendEvent(DeliveryEvent.OnScanBarcode(barcode))
                }
            }
        })
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RaasGraphBaseRoute
    ) {
        raasNavGraph(
            scanViewModel = scanViewModel,
            onNavigateToDeliveryScreen = { patientId ->
                navController.onNavigateToDeliveryScreen(patientId = patientId)
            }, deliveryViewModel = deliveryViewModel,
            onNavigateToScanScreen = navController::navigateToScan
        )
    }
}
