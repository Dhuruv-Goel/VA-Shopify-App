package com.example.vashopify.fragments.settings.data

import android.os.Parcelable
import com.example.vashopify.data.ProductResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val productId: ProductResponse,
    val quantity: Int,
    val _id: String,
):Parcelable