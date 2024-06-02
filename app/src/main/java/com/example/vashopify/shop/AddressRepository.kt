package com.example.vashopify.shop

import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.AddressRequest
import com.example.vashopify.data.AddressResponse
import com.example.vashopify.data.CartItem
import com.example.vashopify.data.CartResponse
import com.example.vashopify.util.Resource

interface AddressRepository {

    suspend fun getAddress(): Resource<AddressResponse>
    suspend fun createAddress(addressTitle:String,shopName:String,street:String,phone:String,city:String, state:String): AuthResult<Unit>
    suspend fun deleteAddress(): AuthResult<Unit>
    suspend fun updateAddress(addressTitle:String,shopName:String,street:String,phone:String,city:String, state:String): AuthResult<Unit>
}