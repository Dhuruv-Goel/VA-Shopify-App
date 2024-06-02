package com.example.vashopify.fragments.settings.data.ui

data class AccountUiState(
    val isLoading: Boolean = false,
    val isError : Boolean = false,
    var firstName : String = "",
    var lastName : String = "",
    var email :String ="",
    var profileImageUrl :String = ""
)
