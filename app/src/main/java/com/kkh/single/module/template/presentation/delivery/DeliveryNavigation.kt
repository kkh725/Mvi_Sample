package com.kkh.single.module.template.presentation.delivery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object DeliveryRoute {
    const val route = "delivery"
    const val routeWithArg = "delivery/{id}"
}
fun NavGraphBuilder.deliveryScreen(
    onNavigateTo: (String) -> Unit
) {
    composable(
        route = DeliveryRoute.route,
    ) {
        DeliveryScreen(onNavigateTo = onNavigateTo, patientId = null)
    }
}

fun NavGraphBuilder.deliveryScreenWithId(
    onNavigateTo: (String) -> Unit
) {
    composable(
        route = DeliveryRoute.routeWithArg,
        arguments = listOf(
            navArgument("id") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        DeliveryScreen(onNavigateTo = onNavigateTo, patientId = id)
    }
}

fun NavController.navigateTo(route: String, arg: String? = null) {
    val fullRoute = if (arg != null) "$route/$arg" else route
    navigate(fullRoute)
}
