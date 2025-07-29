package com.kkh.single.module.template.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.kkh.single.module.template.MainEffect
import com.kkh.single.module.template.MainViewModel

@Composable
fun RaasApp(mainViewModel: MainViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    // 1회성 이벤트(Effect)는 collect로 직접 처리
    LaunchedEffect(Unit) {
        mainViewModel.sideEffect.collect { effect ->
            when (effect) {
                is MainEffect.ShowPopup -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { paddingValues ->
        AppNavGraph(
            modifier = Modifier.padding(paddingValues),
            navController = rememberNavController()
        )
    }
}