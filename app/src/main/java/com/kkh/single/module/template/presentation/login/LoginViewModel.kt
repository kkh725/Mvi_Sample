package com.kkh.single.module.template.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kkh.single.module.template.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val loginReducer = LoginReducer(LoginState.init())
    val loginUiState get() = loginReducer.uiState

    fun reduce(e: LoginAction) {
        // reducer 를 통한 상태 변경 이후
        loginReducer.reduce(e)
        // viewModel 의 로직 수행
        when (e) {
            LoginAction.ClickedButton -> action()
        }
    }

    fun action() {
        viewModelScope.launch {
            val res = mainRepository.setUserInfo("username")
            res.onSuccess { Log.d("TAG", "action: success") }
            res.onFailure { Log.d("TAG", "action: fail") }
        }
    }
}