package com.example.vashopify.fragments.settings.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val addressTitle:String = "",
    val shopName:String="",
    val street:String="",
    val phone:String="",
    val city:String="",
    val state:String="",
    val _id:String=""
): Parcelable