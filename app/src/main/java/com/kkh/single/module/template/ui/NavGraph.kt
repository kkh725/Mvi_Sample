package com.kkh.single.module.template.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kkh.single.module.template.presentation.scan.ScanScreen

object Routes {
    const val HOME = "home"
}

@Composable
fun AppNavGraph(modifier: Modifier = Modifier,navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            ScanScreen()
        }
    }
}
