package com.kkh.single.module.template.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkh.single.module.template.domain.repository.MainRepository
import com.kkh.single.module.template.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ScanViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val scanReducer = ScanReducer(ScanState.empty)
    val uiState get() = scanReducer.uiState
    val sideEffect get() = scanReducer.effect

    fun sendEvent(event: ScanEvent) {
        viewModelScope.launch {
            scanReducer.sendEvent(event)

            when (event) {
                is ScanEvent.OnEnterScanScreen -> {
                    handleIfNotExistDept()
                }
                is ScanEvent.OnCompleteSelectDept ->{
                    repository.fetchDept(event.dept)
                }
            }
        }
    }

    fun sendEffect(effect: ScanEffect) {
        viewModelScope.launch {
            scanReducer.sendEffect(effect)
        }
    }
    
    private suspend fun handleIfNotExistDept(){
        if (repository.getDept().isEmpty()){ // 아예 초기 인 상태
            scanReducer.sendEffect(ScanEffect.ShowDialog(true))
        }else{
            val dept = repository.getDept()
            scanReducer.setState(uiState.value.copy(dept = dept))
        }
    }
}