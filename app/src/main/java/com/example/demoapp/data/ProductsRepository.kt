package com.example.demoapp.data

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

import javax.inject.Inject


class ProductsRepository @Inject constructor(private val productsService: ProductsService) {

    val ELEMENTS_ON_PAGE = 10

    suspend fun loadProductsPage(page: Int) = flow {
        emit(NetworkResult.Loading(true))
        val skip = ELEMENTS_ON_PAGE * ( page - 1)
        val response = productsService.getProducts(ELEMENTS_ON_PAGE, skip)
        emit(NetworkResult.Success(response))
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Something went wrong"))
    }
}