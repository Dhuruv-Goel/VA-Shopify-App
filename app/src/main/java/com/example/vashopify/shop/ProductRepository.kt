package com.example.vashopify.shop


import com.example.vashopify.auth.AuthResult
import com.example.vashopify.data.ProductResponse
import com.example.vashopify.util.Resource

interface ProductRepository {

    suspend fun getAllProducts(): Resource<List<ProductResponse>>

//    suspend fun getProductByName(productName: String): Resource<Product>

    suspend fun getProductsByCategory(categoryName: String, page: Long, pageSize: Long): Resource<List<ProductResponse>>
    suspend fun getSearchedProducts(query: String,page:Long,pageSize:Long): Resource<List<ProductResponse>>
    suspend fun updateAdded(productId:String):AuthResult<Unit>
//    suspend fun getProductsByBrand(brandName: String): Resource<List<Product>>
//    suspend fun getProduct(productId: String): Resource<ProductResponse>

}