package com.kkh.single.module.template.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.kkh.single.module.template.presentation.delivery.DeliveryRoute
import com.kkh.single.module.template.presentation.delivery.deliveryScreen
import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.presentation.scan.scanScreen

@Composable
fun RaasNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = DeliveryRoute.route
    ) {
        scanScreen(onNavigateTo = {
            navController.navigate(it)
        })
        deliveryScreen(onNavigateTo = {
            navController.navigate(it)
        })
    }
}
