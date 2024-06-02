package com.example.vashopify.shop
// UI Event sealed class
sealed class MainUiEvent {
    data class NavigateToProductDetail(val productId: String) : MainUiEvent()
    // Add more events as needed
}