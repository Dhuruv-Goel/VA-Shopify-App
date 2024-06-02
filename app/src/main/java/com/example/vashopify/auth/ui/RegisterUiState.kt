package com.example.vashopify.auth.ui

data class RegisterUiState(
    val isLoading: Boolean = false,
    var registerFirstName:String ="",
    var registerLastName:String ="",
    var registerMobile: String = "",
    var registerEmail:String ="",
    var registerPassword: String = "",
)
