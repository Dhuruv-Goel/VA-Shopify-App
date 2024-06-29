package com.example.vashopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.auth.AuthRepository
import com.example.vashopify.auth.AuthRepositoryImpl
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.auth.ui.AuthUiEvent
import com.example.vashopify.auth.ui.RegisterUiState
import com.example.vashopify.util.RegisterFieldsState
import com.example.vashopify.util.RegisterValidation
import com.example.vashopify.util.validateEmail
import com.example.vashopify.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {


    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState : StateFlow<RegisterUiState> get() = _registerUiState
    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResultsRegister = resultChannel.receiveAsFlow()
//    private val _register= MutableStateFlow<Resource<RegisterUiState>>(Resource.Unspecified())
//    val register : Flow<Resource<RegisterUiState>> = _register
    private val _validationR = Channel<RegisterFieldsState>()
    val validationR = _validationR.receiveAsFlow()
//    init {
//        authenticate()
//    }
    fun onEvent(event: AuthUiEvent) {
        when(event) {

            is AuthUiEvent.RegisterFirstNameChanged-> {
                _registerUiState.update { it.copy(registerFirstName = event.value) }
            }
            is AuthUiEvent.RegisterLastNameChanged-> {
                _registerUiState.update { it.copy(registerLastName = event.value) }
            }
            is AuthUiEvent.RegisterMobileChanged-> {
                _registerUiState.update { it.copy(registerMobile = event.value) }
            }
            is AuthUiEvent.RegisterEmailChanged-> {
                _registerUiState.update { it.copy(registerEmail = event.value) }
            }
            is AuthUiEvent.RegisterPasswordChanged-> {
                _registerUiState.update { it.copy(registerPassword = event.value) }
            }
            is AuthUiEvent.Register -> {
                signUp()
            }

            else -> {}
        }
    }

    private fun signUp() {
        if(checkValidation()) {
            viewModelScope.launch {
                try {
                    _registerUiState.update { it.copy(isLoading = true) }
                    val result = repository.signUp(
                        firstName = registerUiState.value.registerFirstName,
                        lastName = registerUiState.value.registerLastName,
                        mobileNumber = registerUiState.value.registerMobile,
                        email = registerUiState.value.registerEmail,
                        password = registerUiState.value.registerPassword
                    )
                    resultChannel.send(result)
                } catch (e: Exception) {
                    // Handle error
                } finally {
                    _registerUiState.update { it.copy(isLoading = false) }
                }
            }
        }else{
            val registerFieldsState = RegisterFieldsState(
                validateEmail(registerUiState.value.registerEmail),
                validatePassword(registerUiState.value.registerPassword)
            )
            runBlocking {
                _validationR.send(registerFieldsState)
            }
        }
    }


//    private fun authenticate() {
//        viewModelScope.launch {
//            _registerUiState.update{ it.copy(isLoading = true) }
//            val result = repository.authenticate()
//            resultChannel.send(result)
//            _registerUiState.update{ it.copy(isLoading = false) }
//        }
//    }
    private fun checkValidation(): Boolean {
        val emailValidation = validateEmail(registerUiState.value.registerEmail)
        val passwordValidation = validatePassword(registerUiState.value.registerPassword)
        return emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
    }



}