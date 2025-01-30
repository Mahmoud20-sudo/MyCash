package com.codeIn.myCash.ui.home.invoices.addMoreProductsToMemorandum

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Cart
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.CartInMemorandumInvoiceFactoryUseCase
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.GetSelectedProductsInMemorandumUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class AddMoreProductsToMemorandumViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    prefs : SharedPrefsModule,
    private val handleCartInMemorandumInvoiceFactoryUseCase: CartInMemorandumInvoiceFactoryUseCase,
    private val getSelectedProductsInMemorandumUseCase: GetSelectedProductsInMemorandumUseCase
    ) : ViewModel() {

    private val _productsDataResult = MutableStateFlow<ProductsState>(ProductsState.Idle)
    val productsDataResult = _productsDataResult.asStateFlow()

    var currency : String? = prefs.getValue(Constants.getCurrency())
    var tax : String? = prefs.getValue(AppConstants.TAX)

    private val _selectedProductsInCart = MutableLiveData<ProductInInvoiceMemorandum?>(null)
    val selectedProductsInCart : LiveData<ProductInInvoiceMemorandum?> = _selectedProductsInCart

    init {
        getProducts()
        _selectedProductsInCart.postValue(ProductInInvoiceMemorandum(list = ArrayList()))
        handleProductInCart(null , Cart.INITIAL)
    }

    fun getProducts(query:String? = null ){
        launchIO {
            getProductsUseCase.invoke(sort = null , searchText = query , categoryId = null , parentCategoryId = null , null , null ).let {
                _productsDataResult.emit(it)
            }
        }
    }

    fun handleProductInCart(product: ProductModel?, type : Cart) {
        launchMain {
            handleCartInMemorandumInvoiceFactoryUseCase.invoke(product , selectedProductsInCart.value , type).let {
                _selectedProductsInCart.value = it
                Log.d("TAG" , "Products in cart to generate memmorandum ${it?.list?.size}")
            }
        }
    }

    fun getSelectedProducts(){
        launchMain {
            getSelectedProductsInMemorandumUseCase.invoke(selectedProductsInCart.value).let {
                _selectedProductsInCart.value = it
            }
        }
    }
}