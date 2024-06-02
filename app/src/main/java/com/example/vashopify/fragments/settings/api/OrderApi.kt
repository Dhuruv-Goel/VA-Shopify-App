package com.example.vashopify.fragments.settings.api

import com.example.vashopify.data.OrderRequest
import com.example.vashopify.fragments.settings.data.OrderDataResponseItem
import com.example.vashopify.util.Constants.orderapihead
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OrderApi {

    @POST("${orderapihead}/place")
    suspend fun placeOrder(
//        @Header("Authorization") token: String,
        @Body request:OrderRequest
    )
    @GET("$orderapihead/allorder")
    suspend fun getAllOrder(
//        @Header ("Authorization") token:String
    ): List<OrderDataResponseItem>
}