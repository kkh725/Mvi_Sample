package com.kkh.single.module.template.presentation.scan

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

object ScanRoute {
    const val route = "scan"
}

fun NavGraphBuilder.scanScreen(
    scanViewModel: ScanViewModel,
    onNavigateToDeliveryScreen: (String) -> Unit
) {
    composable(ScanRoute.route) {
        ScanScreen(
            onNavigateToDeliveryScreen = onNavigateToDeliveryScreen,
            viewModel = scanViewModel
        )
    }
}

fun NavController.navigateToScan() {
    navigate(ScanRoute.route)
}