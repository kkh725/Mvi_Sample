package com.kkh.single.module.template.presentation.scan

import com.kkh.single.module.template.CommonEffect
import com.kkh.single.module.template.SideEffect
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.util.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val repository: MainRepository) :
    BaseMviViewModel<ScanState, ScanEvent, SideEffect>(reducer = ScanReducer(ScanState.init)) {

    override suspend fun onEventAfterReduce(event: ScanEvent) {
        super.onEventAfterReduce(event)

        when (event) {
            is ScanEvent.OnEnterScanScreen -> {
                checkAndHandleDeptState()
            }

            is ScanEvent.OnCompleteSelectDept -> {
                repository.fetchDept(event.dept)
            }
        }
    }

    private suspend fun checkAndHandleDeptState() {
        val dept = repository.getDept()

        if (dept.isEmpty()) { // 아예 초기 인 상태
            reducer.sendEffect(CommonEffect.ShowDialog(true))
        } else {
            reducer.setState(uiState.value.copy(dept = dept))
        }
    }
}