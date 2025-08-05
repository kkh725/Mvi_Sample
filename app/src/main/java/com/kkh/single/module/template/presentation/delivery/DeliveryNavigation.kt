package com.kkh.single.module.template.presentation.delivery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object DeliveryRoute {
    const val route = "delivery/{patientId}"
}

fun NavGraphBuilder.deliveryScreen(
    onNavigateToScanScreen: () -> Unit,
    deliveryViewModel: DeliveryViewModel
) {
    composable(
        route = DeliveryRoute.route,
        arguments = listOf(
            navArgument("patientId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val patientId = backStackEntry.arguments?.getString("patientId")
        DeliveryScreen(onNavigateToScanScreen = onNavigateToScanScreen, patientId = patientId)
    }
}

fun NavController.onNavigateToDeliveryScreen(route: String, patientId: String? = null) {
    val fullRoute = if (patientId != null) "$route/$patientId" else route
    navigate(fullRoute)
}
