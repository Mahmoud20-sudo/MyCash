package com.codeIn.myCash.ui.home.products.first_step_create_invoice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Cart
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.model.PurchaseInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CartFactoryUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.MakePurchaseInvoiceValidationUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.ValidationDiscountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
open class FirstStepCreationInvoiceViewModel @Inject constructor(
    private val state: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val cartFactoryUseCase: CartFactoryUseCase,
    val validationDiscountUseCase: ValidationDiscountUseCase ,
    val makePurchaseInvoiceValidationUseCase: MakePurchaseInvoiceValidationUseCase
) : ViewModel() {


    private val _selectedProductsInCart = MutableLiveData<ProductsInCart?>(null)
    val selectedProductsInCart : LiveData<ProductsInCart?> = _selectedProductsInCart

    private val _invoiceType = MutableLiveData<InvoiceType>(InvoiceType.SIMPLE)
    val invoiceType : LiveData<InvoiceType> = _invoiceType

    private val _mainInvoiceType = MutableLiveData<MainTypeInvoice>(MainTypeInvoice.SALE)
    val mainInvoiceType : LiveData<MainTypeInvoice> = _mainInvoiceType

    var currency : String? = prefs.getValue(Constants.getCurrency())
    var tax : Int? = prefs.getValue(AppConstants.TAX).run {
        this?.toFloat()?.roundToInt()
    }

    init {
        _selectedProductsInCart.value = state["products_in_cart"]
    }


    fun handleProductInCart(product: ProductModel?, type : Cart) {
        Log.d("TAG" , "Product in cart $type , $product")
        launchMain {
            cartFactoryUseCase.invoke(product , selectedProductsInCart.value , type).let {
                _selectedProductsInCart.value = it
                Log.d("TAG" , "Product in cart ${_selectedProductsInCart.value}")
            }
        }
    }

    fun updateInvoiceType(type: InvoiceType) {
        if (type == _invoiceType.value) return
        _invoiceType.value =  type
        _selectedProductsInCart.value?.invoiceType = type
    }

    fun updatePurchaseInvoice(purchaseInvoiceModel: PurchaseInvoiceModel?){
        _selectedProductsInCart.value?.purchaseInvoiceModel = purchaseInvoiceModel
        updateMainInvoiceType(MainTypeInvoice.PURCHASE)
    }

    fun updateMainInvoiceType(type: MainTypeInvoice){
        if (type == _mainInvoiceType.value) return
        _mainInvoiceType.value =  type
        _selectedProductsInCart.value?.mainTypeInvoice = type
    }

}

