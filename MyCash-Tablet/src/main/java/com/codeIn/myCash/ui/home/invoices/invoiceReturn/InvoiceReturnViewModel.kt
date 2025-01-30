package com.codeIn.myCash.ui.home.invoices.invoiceReturn

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Cart
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CartInReturnInvoiceFactoryUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.ReturnInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InvoiceReturnViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val handleReturnInvoiceUseCase: CartInReturnInvoiceFactoryUseCase,
    private val returnInvoiceUseCase: ReturnInvoiceUseCase) : ViewModel() {

    private val _invoiceDataResult = MutableStateFlow<InvoiceState>(InvoiceState.Idle)
    val invoiceDataResult = _invoiceDataResult.asStateFlow()

    private val _products = MutableLiveData<ArrayList<ProductInInvoiceModel>?>()
    val products: LiveData<ArrayList<ProductInInvoiceModel>?> = _products

    private val _invoiceModel = MutableLiveData<InvoiceModel>()
    val invoiceModel: LiveData<InvoiceModel> = _invoiceModel

    var currency : String? = prefs.getValue(Constants.getCurrency())
    var tax : String? = prefs.getValue(AppConstants.TAX)

    private val _selectedProductsInReturn = MutableLiveData<ProductInReturnInvoice?>(null)
    val selectedProductsInReturn : LiveData<ProductInReturnInvoice?> = _selectedProductsInReturn


    init {
        if (savedStateHandle.contains("invoice"))
            _invoiceModel.postValue(savedStateHandle["invoice"])
    }

    fun updateProductsInInvoice(productsInInvoice: ArrayList<ProductInInvoiceModel>?){
        _products.postValue(productsInInvoice)
    }

    fun updateSelectedProductsInReturn(invoiceModel: InvoiceModel?){
        val list : ArrayList<ProductInInvoiceModel>? = ArrayList()
        invoiceModel?.products?.forEach {product->
            list?.add(product)
        }
        _selectedProductsInReturn.postValue(ProductInReturnInvoice(list = list))
    }

    fun handleProductInInvoice(product: ProductInInvoiceModel?, type : Cart) {
        launchMain {
            handleReturnInvoiceUseCase.invoke(product , selectedProductsInReturn.value , type).let {
                _selectedProductsInReturn.value = it
            }
        }
    }

    fun returnInvoice( cashPrice: String?,
                       visaPrice: String?,
                       paymentType: Int,
                       codeRefundInvoice : String?= null ,
                       runRefundInvoice : String? = null ,
                       dateRefundInvoice : String? = null ,){

        launchIO {
            returnInvoiceUseCase.invoke(cashPrice , visaPrice ,paymentType ,  selectedProductsInReturn.value ,
                invoiceModel.value?.client?.id ,
                invoiceModel.value?.id.toString() , invoiceModel.value?.invoiceType?.toInt()?:0 ,
                codeRefundInvoice, runRefundInvoice, dateRefundInvoice , invoiceModel.value?.invoiceType?.toInt() ?:0
            ).let {
                _invoiceDataResult.emit(it)
                Log.d("TAG" , "response ... $it")
            }
        }
    }

    fun clearState(){
        _invoiceDataResult.value = InvoiceState.Idle
    }
}