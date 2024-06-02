package com.example.vashopify.data

data class OrderRequest(
    val orderStatus:String="",
    val totalPrice:Int=0,
    val products:List<CartItem> = emptyList(),
    val address:AddressRequest = AddressRequest(),
    val orderId: String =""
)