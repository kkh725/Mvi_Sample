package com.kkh.single.module.template.presentation.scan

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

object ScanRoute {
    const val route = "scan"
}

fun NavGraphBuilder.scanScreen(
    onNavigateTo: (String) -> Unit
) {
    composable(ScanRoute.route) {
        ScanScreen(onNavigateTo = onNavigateTo)
    }
}

fun NavController.navigateToScan() {
    navigate(ScanRoute.route)
}