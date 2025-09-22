package com.kkh.single.module.template.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.kkh.single.module.template.presentation.delivery.DeliveryRoute
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.presentation.scan.ScanViewModel
import com.kkh.single.module.template.util.navigation.RaasGraphBaseRoute
import com.kkh.single.module.template.util.navigation.RaasRoute

// 모듈이 분리되어 있다고 가정하고 코드 작성.
fun NavGraphBuilder.raasNavGraph(
    scanViewModel: ScanViewModel,
    deliveryViewModel: DeliveryViewModel,
    onNavigateToDeliveryScreen: (String) -> Unit,
    onNavigateToScanScreen: () -> Unit
) {
    navigation< RaasGraphBaseRoute>(RaasRoute.ScanRoute){
        composable<RaasRoute.ScanRoute>{
            ScanRoute(
                onNavigateToDeliveryScreen = onNavigateToDeliveryScreen,
                viewModel = scanViewModel
            )
        }
        composable<RaasRoute.DeliveryRoute>{ backStackEntry ->
            val deliveryItem = backStackEntry.toRoute<RaasRoute.DeliveryRoute>()
            DeliveryRoute(
                onNavigateToScanScreen = onNavigateToScanScreen,
                patientId = deliveryItem.patientId?.takeIf { it.isNotEmpty() },
                viewModel = deliveryViewModel
            )
        }
    }
}

fun NavController.navigateToScan() {
    navigate(RaasRoute.ScanRoute)
}

fun NavController.onNavigateToDeliveryScreen(patientId: String? = null) {
    if (patientId != null){
        navigate(RaasRoute.DeliveryRoute(patientId = patientId))
    }else{
        navigate(RaasRoute.DeliveryRoute())
    }
}