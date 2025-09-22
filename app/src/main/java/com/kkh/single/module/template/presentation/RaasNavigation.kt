package com.kkh.single.module.template.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.scan.ScanViewModel
import com.kkh.single.module.template.util.navigation.RaasGraphBaseRoute

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
