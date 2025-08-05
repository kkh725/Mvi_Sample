package com.kkh.single.module.template.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kkh.single.module.template.presentation.delivery.DeliveryRoute
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.delivery.deliveryScreen
import com.kkh.single.module.template.presentation.delivery.onNavigateToDeliveryScreen
import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.presentation.scan.ScanViewModel
import com.kkh.single.module.template.presentation.scan.navigateToScan
import com.kkh.single.module.template.presentation.scan.scanScreen

@Composable
fun RaasNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    scanViewModel: ScanViewModel,
    deliveryViewModel: DeliveryViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ScanRoute.route
    ) {
        scanScreen(
            scanViewModel = scanViewModel,
            onNavigateToDeliveryScreen = { patientId ->
                navController.onNavigateToDeliveryScreen(route = DeliveryRoute.baseRoute, patientId = patientId)
            }
        )
        deliveryScreen(
            deliveryViewModel = deliveryViewModel,
            onNavigateToScanScreen = navController::navigateToScan
        )
    }
}
