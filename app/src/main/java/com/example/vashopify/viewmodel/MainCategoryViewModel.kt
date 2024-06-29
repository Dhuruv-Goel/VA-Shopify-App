package com.example.vashopify.viewmodel

import CategoryEvent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vashopify.auth.AuthResult
import com.example.vashopify.fragments.categories.repository.CatRepo
import com.example.vashopify.shop.ProductEvent
import com.example.vashopify.shop.ProductRepository
import com.example.vashopify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val catRepo: CatRepo
) : ViewModel() {
    //    private var productList = ArrayList<ProductResponse>()
    private val _beautyState =
        MutableLiveData<ProductEvent>(ProductEvent.Empty)
    val beautyState: LiveData<ProductEvent> get() = _beautyState
    private val catName1 = "Beauty"
    private val catName2 = "Personal Care"
    private val catName3 = "Winter Wear"
    private val _personalState =
        MutableLiveData<ProductEvent>(ProductEvent.Empty)
    val personalState: LiveData<ProductEvent> get() = _personalState
    private val _winterState =
        MutableLiveData<ProductEvent>(ProductEvent.Empty)
    val winterState: LiveData<ProductEvent> get() = _winterState
    private val _catState =
        MutableLiveData<CategoryEvent>(CategoryEvent.Empty)
    val catState: LiveData<CategoryEvent> get() = _catState
    private val _showLoading = MutableStateFlow<Boolean>(false)
    val showLoading: StateFlow<Boolean> get() = _showLoading.asStateFlow()
    private val resultUpdate1 = Channel<AuthResult<Unit>>()
    val updateResult1 = resultUpdate1.receiveAsFlow()
    init {
        fetchCategory()
        fetchBeautyProduct()
        fetchPersonalCareProduct()
        fetchWinterWearProduct()
    }

    fun fetchBeautyProduct() {
//        _showLoading.value = true
        if (_beautyState.value !is ProductEvent.Success) {
            viewModelScope.launch {
                _beautyState.value = ProductEvent.Loading

                when (val productResponse =
                    productRepository.getProductsByCategory(catName1, page = 1, pageSize = 10)) {
                    is Resource.Error -> {
                        Log.e("error", "Resource Error")
                        _beautyState.value = ProductEvent.Failure
                    }

                    is Resource.Success -> {
                        val list = productResponse.data!!
                        Log.d("error", "Resource Success")
//                    Log.d("error",list.toString())

                        _beautyState.value = ProductEvent.Success(list)

                    }

                    else -> Unit
                }
            }
        }
    }

    fun fetchPersonalCareProduct() {
//        _showLoading.value = true
        if (_personalState.value !is ProductEvent.Success) {

            viewModelScope.launch {
                _personalState.value = ProductEvent.Loading

                when (val productResponse =
                    productRepository.getProductsByCategory(catName2, page = 1, pageSize = 10)) {
                    is Resource.Error -> {
                        Log.e("error", "Resource Error")
                        _personalState.value = ProductEvent.Failure
                    }

                    is Resource.Success -> {
                        val list = productResponse.data!!
                        Log.d("error", "Resource Success")
//                    Log.d("error",list.toString())

                        _personalState.value = ProductEvent.Success(list)

                    }

                    else -> Unit
                }
            }
        }
    }

    fun fetchWinterWearProduct() {
//        _showLoading.value = true
        if (_winterState.value !is ProductEvent.Success) {

            viewModelScope.launch {
                _winterState.value = ProductEvent.Loading

                when (val productResponse =
                    productRepository.getProductsByCategory(catName3, page = 1, pageSize = 10)) {
                    is Resource.Error -> {
                        Log.e("error", "Resource Error")
                        _winterState.value = ProductEvent.Failure
                    }

                    is Resource.Success -> {
                        val list = productResponse.data!!
                        Log.d("error", "Resource Success")
//                    Log.d("error",list.toString())

                        _winterState.value = ProductEvent.Success(list)

                    }

                    else -> Unit
                }
            }
        }
    }
    fun fetchCategory() {
//        _showLoading.value = true
        if (_catState.value !is CategoryEvent.Success) {

            viewModelScope.launch {
                _catState.value = CategoryEvent.Loading

                when (val catResponse = catRepo.getAllCategories()) {
                    is Resource.Error -> {
                        Log.e("error", "Resource Error")
                        _catState.value = CategoryEvent.Failure()
                    }

                    is Resource.Success -> {
                        val list = catResponse.data!!
                        Log.d("error", "Resource Success")
//                    Log.d("error",list.toString())

                        _catState.value = CategoryEvent.Success(list)

                    }

                    else -> Unit
                }
            }
        }
    }
}
//    private suspend fun handleSuccess(categoryName: String, products: List<Product>) {
//        when (categoryName) {
//            "Beauty" -> {
//                Log.d("size Handle Viewmodel",products.size.toString())
//                beautyProducts = products ?: emptyList()
//                _beautyState.emit(Resource.Success(beautyProducts))
//            }

//            "Personal Care" -> {
//                personalCareProducts = products ?: emptyList()
//                _personalState.emit(Resource.Success(personalCareProducts))
//            }
//
//            "Winter Wear" -> {
//                winterWears = products ?: emptyList()
//                _winterState.emit(Resource.Success(winterWears))
//            }

//    }

//    private suspend fun handleError() {
////        _showLoading.value = false
////        _beautyState.emit(Resource.Error())
////        _personalState.emit(Resource.Error())
////        _winterState.emit(Resource.Error())
//    }
//
//}    try {
//    when (val result = repository.getProductsByCategory(categoryName)) {
////                    is Resource.Success -> result.data?.let { handleSuccess(categoryName, it) }
//        is Resource.Success -> {
//
//
//        }
//        is Resource.Error -> handleError()
//        else -> Unit
//    }
//} catch (e: Exception) {
//    handleError()
//} finally {
////                _showLoading.value = false
////                Log.d("loading",_showLoading.value.toString())
//
//}


//
//try{
//    val call = productApi.getProductsByCategory("Beauty")
//    call.enqueue(object : retrofit2.Callback<ProductListResponse> {
//        override fun onResponse(
//            call: Call<ProductListResponse>,
//            response: Response<ProductListResponse>
//        ) {
//            if (response.isSuccessful) {
////                        productList = response.body()!!.productResponses
//                Log.d("data", response.body()!!.productResponses.toList().toString())
//                _beautyState.value = response.body()!!.productResponses ?: ArrayList()
//                onSuccess.invoke(true)
//                _showLoading.value = false
//            } else {
//                onSuccess.invoke(false)
//                _showLoading.value = false
//            }
//        }
//
//        override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
//            onSuccess.invoke(false)
//            _showLoading.value = false
//
//        }
//
//    })
//} catch (e: Exception) {
//    onSuccess.invoke(false)
//    _showLoading.value = false
//}


//    private val _showLoading = MutableLiveData<Boolean>()
//    val showLoading: LiveData<Boolean> get() = _showLoading
//    private var beautyProducts: List<Product> = emptyList()
//    private var personalCareProducts: List<Product> = emptyList()
//    private var winterWears: List<Product> = emptyList()

//    init {
//        fetchProductByCategoryName("Beauty")
////        fetchProductByCategoryName("Personal Care")
////        fetchProductByCategoryName("Winter Wear")
//    }
//    init {
//        fetchBeautyProduct()
//    }