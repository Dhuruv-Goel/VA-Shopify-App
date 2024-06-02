package com.example.vashopify.shop

import com.example.vashopify.data.AddressResponse
import com.example.vashopify.data.CartItemResponse
import com.example.vashopify.data.ProductResponse
import com.example.vashopify.data.UserResponse
import com.example.vashopify.fragments.settings.data.OrderDataResponseItem

sealed class ProductEvent{
    class Success(val list:List<ProductResponse>):ProductEvent()
    class CartSucess(val clist :List<CartItemResponse>):ProductEvent()
    class AddressSuccess(val address: AddressResponse):ProductEvent()
    class OrderSuccess(val orders: List<OrderDataResponseItem>):ProductEvent()
    class UnitSuccess(val data:Unit):ProductEvent()
    class AuthSuccess(val user:UserResponse):ProductEvent()
    object Error : ProductEvent()
    object Failure:ProductEvent()
    object Loading:ProductEvent()
    object Empty:ProductEvent()
    object Unauthorised :ProductEvent()

}
