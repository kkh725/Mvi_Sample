package com.kkh.single.module.template.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kkh.single.module.template.MainEffect
import com.kkh.single.module.template.util.common.CommonEffect
import com.kkh.single.module.template.MainViewModel
import com.kkh.single.module.template.presentation.delivery.DeliveryRoute
import com.kkh.single.module.template.presentation.delivery.DeliveryViewModel
import com.kkh.single.module.template.presentation.delivery.onNavigateToDeliveryScreen
import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.presentation.scan.ScanViewModel

@Composable
fun RaasApp(
    mainViewModel: MainViewModel,
    deliveryViewModel: DeliveryViewModel,
    scanViewModel: ScanViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    LaunchedEffect(Unit) {
        mainViewModel.sideEffect.collect { effect ->
            when (effect) {
                is MainEffect.OnNavigateToDeliveryScreen -> {
                    // scanRoute 일 때에만 화면 전환.
                    if (currentRoute == ScanRoute.route){
                        navController.onNavigateToDeliveryScreen(DeliveryRoute.route, effect.patientId)
                    }
                }
                else -> {
                    // Handle other effects
                }
            }
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.testTag("snackbar"))
    }) { paddingValues ->
        RaasNavigation(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            deliveryViewModel = deliveryViewModel,
            scanViewModel = scanViewModel
        )
    }
}