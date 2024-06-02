package com.example.vashopify.shop

// UI State sealed class
sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val productListResponse: ProductListResponse) : MainUiState()
    data class Error(val errorMessage: String) : MainUiState()
}
