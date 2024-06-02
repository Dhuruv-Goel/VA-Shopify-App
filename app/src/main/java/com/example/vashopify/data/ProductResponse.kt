package com.example.vashopify.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class ProductResponse(
    val _id: String="",
    val brandName: String="",
    val name: String="",
    val categoryName: String="",
    val quantity :String="",
    val mrpPrice: Float?=null,
    val ratePrice: Float?=null,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: String="",
    val isAdded : Boolean=false,
    val createdAt: String="",
    val updatedAt: String="",
    val __v: Int=0
):Parcelable
//{
//    constructor(): this("","",""," ",""," "," "," ",null,null,"","","")
//}
