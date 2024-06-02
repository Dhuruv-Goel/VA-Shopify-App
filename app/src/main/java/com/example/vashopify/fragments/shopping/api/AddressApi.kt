package com.example.vashopify.fragments.shopping.api

import com.example.vashopify.data.AddressRequest
import com.example.vashopify.data.AddressResponse
import com.example.vashopify.util.Constants.addressapihead
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface AddressApi {


    @GET("$addressapihead/id")
    suspend fun getAddress(
//        @Header("Authorization") token: String
    ):AddressResponse

    @POST("$addressapihead/new")
    suspend fun createAddress(
//        @Header("Authorization") token: String,
        @Body request:AddressRequest
    )

    @PUT("$addressapihead/id")
    suspend fun updateAddress(
//        @Header("Authorization") token: String,
        @Body request: AddressRequest
    )

    @DELETE("$addressapihead/id")
    suspend fun deleteAddress(
//        @Header("Authorization") token: String,
    )
}