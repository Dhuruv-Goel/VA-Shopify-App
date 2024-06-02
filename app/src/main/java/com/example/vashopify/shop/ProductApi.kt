package com.example.vashopify.shop

import com.example.vashopify.data.ProductResponse
import com.example.vashopify.util.Constants.prdtapihead
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface ProductApi  {


    @GET("$prdtapihead/allproducts")
    suspend fun getAllProducts(): List<ProductResponse>
//    suspend fun getAllProducts(): Response<ProductListResponse>

//    @GET("$prdtapihead/{productName}")
//    suspend fun getProductByName(@Path("productName") productName : String): Response<ProductResponse>
//    suspend fun getProductByName(@Path("productName") productName : String): Response<Product>

    @GET("$prdtapihead/byCategory/categoryName")
    suspend fun getProductsByCategory(
        @Query("categoryName") categoryName: String,
        @Query("page") page: Long,
        @Query("pageSize") pageSize: Long
    ) : List<ProductResponse>

    @GET("$prdtapihead/listProduct")
    suspend fun getSearchedProducts(
        @Query("q") query:String,
        @Query("page") page: Long,
        @Query("pageSize") pageSize: Long
    ):List<ProductResponse>

//    @GET("$prdtapihead/productId")
//    suspend fun getProduct(
//        @Query("productId") productId:String
//    ): ProductResponse
    @PUT("$prdtapihead/id")
    suspend fun updateAdded(
        @Query("productId") productId:String
    )

//    suspend fun getProductsByCategory(@Path("categoryName") categoryName : String) : List<Product>
//    suspend fun getProductsByCategory(@Path("categoryName") categoryName : String) : Response<ProductListResponse>

//    @GET("$prdtapihead/byBrand/brandName")
//    suspend fun getProductsByBrand(
//        @Query("brandName") brandName: String
//    ): Response<ProductListResponse>
//    suspend fun getProductsByBrand(@Path("brandName") brandName: String): Response<ProductListResponse>




}