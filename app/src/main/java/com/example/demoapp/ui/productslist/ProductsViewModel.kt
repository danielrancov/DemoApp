package com.example.demoapp.ui.productslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demoapp.data.NetworkResult
import com.example.demoapp.data.ProductsModel
import com.example.demoapp.data.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _productsResult = MutableLiveData<ProductsResult>()
    val productsResult: LiveData<ProductsResult>
        get() = _productsResult

    private var pageToLoad = 0
    private var totalPageCount = 0
    private var isLastPage = false;

    fun getNextProducts() {
        pageToLoad++
        viewModelScope.launch {
            productsRepository.loadProductsPage(pageToLoad).collect { networkResult ->
                when (networkResult) {
                    is NetworkResult.Error -> _productsResult.value =
                        ProductsResult.Error(networkResult.errorMessage)
                    is NetworkResult.Success -> {
                        val data = networkResult.data
                        totalPageCount = data.total
                        isLastPage =
                            (data.skip + data.limit) == totalPageCount // number of skipped items + number of current items is equal to total items, then we are at the last page
                        _productsResult.value = ProductsResult.Success(data.products)
                    }
                    is NetworkResult.Loading -> _productsResult.value = ProductsResult.Loading
                }
            }
        }
    }

    fun getTotalPageCount(): Int = totalPageCount
    fun isLastPage(): Boolean = isLastPage

    sealed class ProductsResult {
        class Success(val products: List<ProductsModel>) : ProductsResult()
        data class Error(val message: String) : ProductsResult()
        object Loading : ProductsResult()
    }
}