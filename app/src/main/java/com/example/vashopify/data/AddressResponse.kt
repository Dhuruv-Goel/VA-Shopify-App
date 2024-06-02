package com.example.vashopify.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressResponse(
    val _id:String,
    val user:String,
    val addressTitle:String,
    val shopName:String,
    val street:String,
    val phone:String,
    val city:String,
    val state:String,
    val __v:Int
):Parcelable