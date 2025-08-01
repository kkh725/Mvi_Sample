package com.kkh.single.module.template.presentation.delivery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

object DeliveryRoute{
    const val route = "Delivery"
}

fun NavGraphBuilder.deliveryScreen(
    onNavigateTo : (String) -> Unit
){
    composable(DeliveryRoute.route){
        DeliveryScreen(onNavigateTo = onNavigateTo)
    }
}

fun NavController.navigateTo(route: String, arg: String? = null) {
    val fullRoute = if (arg != null) "$route/$arg" else route
    navigate(fullRoute)
}
