package com.example.vashopify.fragments.shopping.repository


import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.CartItem
import com.example.vashopify.data.CartResponse
import com.example.vashopify.data.OrderRequest
import com.example.vashopify.fragments.settings.data.OrderDataResponseItem
import com.example.vashopify.util.Resource

interface CartRepository {
    suspend fun addProductToCart( cartItem: CartItem): AuthResult<Unit>
    suspend fun getUserCart(): Resource<CartResponse>
    suspend fun updateQuantity(productId:String,quantity:String): AuthResult<Unit>
    suspend fun deleteProductFromCart(productId:String):Resource<CartResponse>
    suspend fun increaseQuantity(productId:String): Resource<CartResponse>
    suspend fun updateTotalPrice(totalPrice:Int): Resource<CartResponse>
    suspend fun decreaseQuantity(productId:String): Resource<CartResponse>
    suspend fun placeOrder(request: OrderRequest):AuthResult<Unit>
    suspend fun getAllOrders():Resource<List<OrderDataResponseItem>>
}