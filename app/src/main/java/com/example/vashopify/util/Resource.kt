package com.example.vashopify.util

import com.example.vashopify.data.ProductResponse

sealed class Resource<T>(val data:T?) {
    class Error<T>:Resource<T>(null)
    class Success<T>(data: T):Resource<T>(data)
    class Unauthorized<T>:Resource<T>(null)
}