package com.example.vashopify.fragments.settings.data.ui

sealed class AccountUiEvent {
    data class FirstNameChanged(val value:String): AccountUiEvent()
    data class LastNameChanged(val value:String): AccountUiEvent()
    data class EmailChanged(val value:String): AccountUiEvent()
//    data class ProfileImageChanged(val value:String) : AccountUiEvent()
    object Update : AccountUiEvent()
}
