package com.example.vashopify.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductRequest(
//    val _id: String,
    @SerialName("brandName") val brandName: String,
    @SerialName("name") val name: String,
    @SerialName("categoryName") val categoryName: Float,
    @SerialName("mrpPrice") val mrpPrice: Float,
    @SerialName("ratePrice") val ratePrice: Float?,
    @SerialName("offerPercentage") val offerPercentage: Float? = null,
    @SerialName("description")val description: String? = null,
    @SerialName("colors") val colors: List<Int>? = null,
    @SerialName("sizes") val sizes: List<String>? = null,
    @SerialName("images")val images: String,
)
