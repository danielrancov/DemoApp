package com.example.demoapp.data

data class ProductsModel(
    val id: Int,
    val title: String,
    val thumbnail: String
)

data class ProductsResponse(
    val products: List<ProductsModel>,
    val total: Int,
    val skip: Int,
    val limit: Int
)