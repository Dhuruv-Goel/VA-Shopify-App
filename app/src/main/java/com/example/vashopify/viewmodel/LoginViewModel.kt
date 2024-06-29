package com.example.vashopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.auth.AuthRepository
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.auth.ui.AuthUiEvent
import com.example.vashopify.auth.ui.LoginUiState
import com.example.vashopify.util.LoginFieldsState
import com.example.vashopify.util.LoginValidation
import com.example.vashopify.util.LvalidateEmail
import com.example.vashopify.util.LvalidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState : StateFlow<LoginUiState> get()  = _loginUiState
//     private val _resetPassword = MutableSharedFlow<String>()
//      val resetPassword = _resetPassword.asSharedFlow()
    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResultslogin = resultChannel.receiveAsFlow()
    private val _validationL = Channel<LoginFieldsState>()
    val validationL = _validationL.receiveAsFlow()
//    init {
//        authenticate()
//    }

    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.LoginEmailChanged -> {
                _loginUiState.update { it.copy(loginEmail = event.value) }
            }
            is AuthUiEvent.LoginPasswordChanged -> {
                _loginUiState.update { it.copy(loginPassword = event.value) }
            }
            is AuthUiEvent.Login -> {
                signIn()
            }
            else -> {

            }
        }
    }

//  fun resetPassword(email:String){
//      viewModelScope.launch {
//          _loginUiState.update{it.copy(isLoading = true)}
//      }
//  }

    private fun signIn() {
        if(checkValidationL()) {
        viewModelScope.launch {
            try {
                _loginUiState.update { it.copy(isLoading = true) }
                val result = repository.signIn(
                    email = loginUiState.value.loginEmail,
                    password = loginUiState.value.loginPassword
                )
                resultChannel.send(result)
            } catch (e: Exception) {
                resultChannel.send(AuthResult.UnknownError())
            } finally {
                _loginUiState.update { it.copy(isLoading = false) }
            }
        }
    }else{
        val loginFieldsState = LoginFieldsState(
            LvalidateEmail(loginUiState.value.loginEmail),
            LvalidatePassword(loginUiState.value.loginPassword)
        )
        runBlocking {
            _validationL.send(loginFieldsState)
        }
    }
    }

//    private fun authenticate() {
//        viewModelScope.launch {
//            try {
//                _loginUiState.update { it.copy(isLoading = true) }
//                val result = repository.authenticate()
//                resultChannel.send(result)
//            } catch (e: Exception) {
//                // Handle authentication failure
//                resultChannel.send(AuthResult.UnknownError())
//            } finally {
//                _loginUiState.update { it.copy(isLoading = false) }
//            }
//        }
//    }
    private fun checkValidationL(): Boolean {
        val emailValidation = LvalidateEmail(loginUiState.value.loginEmail)
        val passwordValidation = LvalidatePassword(loginUiState.value.loginPassword)
        return emailValidation is LoginValidation.Success &&
                passwordValidation is LoginValidation.Success
    }
}