package com.example.vashopify.auth.ui

sealed class AuthUiEvent {
    data class LoginEmailChanged(val value: String): AuthUiEvent()
    data class LoginPasswordChanged(val value: String): AuthUiEvent()
    object Login: AuthUiEvent()

    data class RegisterFirstNameChanged(val value: String): AuthUiEvent()
    data class RegisterLastNameChanged(val value: String): AuthUiEvent()
    data class RegisterMobileChanged(val value: String): AuthUiEvent()
    data class RegisterEmailChanged(val value: String): AuthUiEvent()
    data class RegisterPasswordChanged(val value: String): AuthUiEvent()
    object Register: AuthUiEvent()
}
