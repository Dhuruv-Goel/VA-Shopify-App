package com.example.vashopify.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
@Parcelize
data class CartItemResponse(
    val productId: ProductResponse = ProductResponse(),
    val quantity: Int=0,
    val _id: String = " "
):Parcelable
