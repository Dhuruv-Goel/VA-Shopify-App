package com.example.vashopify.data


data class CartItem(
    val productId: String= ProductResponse()._id,
    val quantity: Int=0
)