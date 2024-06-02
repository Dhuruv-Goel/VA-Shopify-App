package com.example.vashopify.data

import android.text.Editable

sealed class AddressUIEvent {
    data class AddressTitleChanged(val value: String) : AddressUIEvent()
    data class ShopNameTitleChanged(val value: String) : AddressUIEvent()
    data class StreetChanged(val value: String) : AddressUIEvent()
    data class PhoneChanged(val value: String) : AddressUIEvent()
    data class CityChanged(val value: String) : AddressUIEvent()
    data class StateChanged(val value: String) : AddressUIEvent()
    object Update : AddressUIEvent()
    object Delete : AddressUIEvent()
    object Save : AddressUIEvent()
}