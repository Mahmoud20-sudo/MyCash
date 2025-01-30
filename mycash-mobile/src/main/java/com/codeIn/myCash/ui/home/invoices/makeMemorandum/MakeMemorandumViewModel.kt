package com.codeIn.myCash.ui.home.invoices.makeMemorandum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Cart
import com.codeIn.common.data.MemorandumType
import com.codeIn.common.domain.usecases.MoneyValidationUseCase
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.usecases.ConfirmVisaUseCase
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.CartInMemorandumInvoiceFactoryUseCase
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.GetSelectedCommonProductsInMemorandumUseCase
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.MakeMemorandumUseCase
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.MakeMemorandumValidationToValidItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MakeMemorandumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val handleCartInMemorandumInvoiceFactoryUseCase: CartInMemorandumInvoiceFactoryUseCase,
    private val getSelectedCommonProductsInMemorandumUseCase: GetSelectedCommonProductsInMemorandumUseCase ,
    private val makeMemorandumValidationToValidItemsUseCase: MakeMemorandumValidationToValidItemsUseCase ,
    val moneyValidationUseCase: MoneyValidationUseCase ,
    private val makeMemorandumUseCase: MakeMemorandumUseCase,
    private val confirmVisaUseCase: ConfirmVisaUseCase
) : ViewModel() {

    private val _invoiceModel = MutableLiveData<InvoiceModel>()
    val invoiceModel: LiveData<InvoiceModel> = _invoiceModel

    private val _memorandumType = MutableLiveData<MemorandumType>()
    val memorandumType: LiveData<MemorandumType> = _memorandumType

    var currency : String? = prefs.getValue(Constants.getCurrency())
    var tax : Int? = prefs.getValue(AppConstants.TAX).run {
        this?.toFloat()?.roundToInt()
    }

    private val _memorandumDataResult = MutableLiveData<MemorandumState>(MemorandumState.Idle)
    val memorandumDataResult  : LiveData<MemorandumState> = _memorandumDataResult

    private val _selectedProductsInMemorandum = MutableLiveData<ProductInInvoiceMemorandum?>(null)
    val selectedProductsInMemorandum : LiveData<ProductInInvoiceMemorandum?> = _selectedProductsInMemorandum

    init {
        if (savedStateHandle.contains("invoice"))
            _invoiceModel.postValue(savedStateHandle["invoice"])
        _memorandumType.postValue(MemorandumType.CREDITOR)
    }
    fun updateMemorandumType(memorandumType: MemorandumType) {
        if (_memorandumType.value != memorandumType)
            _memorandumType.postValue(memorandumType)
    }
    fun initialProductsInInvoice(products : ArrayList<ProductInInvoiceModel>?){
        val productsInInvoice = ArrayList<ProductModel>()
        if (products?.isNotEmpty() == true){
            products.forEach {product ->
                product.product?.count = product.quantity?.toFloat()?.toInt() ?: 0
                                product.product?.finalPrice = if((product.totalAfterNotification?.toFloat() ?: 0f) > 0) product.totalAfterNotification else product.productPrice
                product.product?.discountPrice = product.discountPrice
                product.product?.taxPrice = product.taxPrice
                product.product?.productPriceAfterDiscount = product.totalPrice
                productsInInvoice.add(product.product!!)
            }
        }
        _selectedProductsInMemorandum.postValue(ProductInInvoiceMemorandum(list = productsInInvoice))

    }
    fun handleProductInMemorandum(product: ProductModel?, type : Cart) {
        launchMain {
            handleCartInMemorandumInvoiceFactoryUseCase.invoke(product, selectedProductsInMemorandum.value , type).let {
                _selectedProductsInMemorandum.value = it
            }
        }
    }
    fun getSelectedProductsInMemorandum(product: ProductInInvoiceMemorandum?){
        launchIO {
            getSelectedCommonProductsInMemorandumUseCase.invoke(selectedProductsInMemorandum.value , product).let {
                _selectedProductsInMemorandum.postValue(it)
            }
        }
    }
    fun makeValidationToValidItems(){
        launchIO {
            makeMemorandumValidationToValidItemsUseCase.invoke(selectedProductsInMemorandum.value).let {
                _memorandumDataResult.postValue(it)
            }
        }
    }
    fun makeMemorandum(cashValue : String? , visaValue : String? ,  paymentType : String? ){
        launchIO {
            _memorandumDataResult.postValue(MemorandumState.Loading)
            makeMemorandumUseCase.invoke(invoiceModel.value?.id.toString() , MemorandumType.CREDITOR.value.toString() ,
                cashValue , visaValue , selectedProductsInMemorandum.value , paymentType).let {
                _memorandumDataResult.postValue(it)
            }
        }
    }
    fun clearState(){
        _memorandumDataResult.value = MemorandumState.Idle
    }


}