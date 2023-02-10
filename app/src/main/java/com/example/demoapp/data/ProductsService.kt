package com.example.demoapp.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsService {

    @GET("/products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsResponse
}