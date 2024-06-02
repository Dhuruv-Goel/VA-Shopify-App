package com.example.vashopify.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartResponse(
    val _id :String,
    val user: String, // Assuming user is identified by a string ID
    val items: List<CartItemResponse>,
    val __v: Int,
    val totalPrice:Int = 0
):Parcelable