package com.example.vashopify.auth.ui

data class LoginUiState(
    val isLoading: Boolean = false,
    val loginEmail: String = "",
    val loginPassword: String = ""
)
