package com.example.vashopify.util

sealed class LoginValidation{
    object Success: LoginValidation()
    data class Failed(val message: String): LoginValidation()
}

data class LoginFieldsState(
    val email: LoginValidation,
    val password: LoginValidation
)
