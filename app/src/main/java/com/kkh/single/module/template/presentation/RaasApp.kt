package com.kkh.single.module.template.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.kkh.single.module.template.CommonEffect
import com.kkh.single.module.template.MainEffect
import com.kkh.single.module.template.MainViewModel

@Composable
fun RaasApp() {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val mainViewModel : MainViewModel = hiltViewModel()

    // 1회성 이벤트(Effect)는 collect로 직접 처리
    LaunchedEffect(Unit) {
        mainViewModel.sideEffect.collect { effect ->
            when (effect) {
                is CommonEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is CommonEffect.NavigateTo -> {
                    navController.navigate(effect.route)
                }
            }
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { paddingValues ->
        RaasNavigation(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}