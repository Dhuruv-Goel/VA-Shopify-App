package com.example.vashopify.data

data class OrderResponse(
    val _id:String,
    val user:String,
    val orderStatus:String,
    val totalPrice:Int,
    val products:List<CartItemResponse>,
    val address:AddressResponse,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)