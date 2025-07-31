package com

import app.cash.turbine.test
import com.kkh.single.module.template.CommonEffect
import com.kkh.single.module.template.MainEvent
import com.kkh.single.module.template.MainViewModel
import com.kkh.single.module.template.presentation.scan.ScanRoute
import com.kkh.single.module.template.util.SnackBarMsgConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel()
    }

    @Test
    fun `OnScanBarcode`() = runTest {
        viewModel.sideEffect.test {
            viewModel.sendEvent(MainEvent.OnScanBarcode("valid_barcode"))

            val effect = awaitItem()
            assertTrue(effect is CommonEffect.NavigateTo)
            assertEquals(ScanRoute.route, (effect as CommonEffect.NavigateTo).route)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnScanBarcode2`() = runTest {
        viewModel.sideEffect.test {
            viewModel.sendEvent(MainEvent.OnScanBarcode("fail"))

            val effect = awaitItem()
            assertTrue(effect is CommonEffect.ShowSnackBar)
            assertEquals(SnackBarMsgConstants.INVALID_BARCODE, (effect as CommonEffect.ShowSnackBar).message)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
