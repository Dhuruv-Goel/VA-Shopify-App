package com.example.vashopify.fragments.settings.data

import android.os.Parcelable
import com.example.vashopify.data.CartItemResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDataResponseItem(
//    val _id: Long = nextLong(0,100_000_000_000),
    val _id:String = "",
    val user: String="",
    val orderStatus: String="",
    val totalPrice: Int=0,
    val products: List<CartItemResponse> = emptyList(),
    val address: Address =Address() ,
    val orderId:String ="",
    val createdAt: String="",
    val updatedAt: String="",
    val __v:Int=0
):Parcelable