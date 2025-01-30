package com.codeIn.myCash.ui.home.products.products.quickInvoice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Cart
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.Limit
import com.codeIn.common.data.PaymentType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.model.ClientRequest
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientValidationUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateVendorValidationUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.GetClientsUseCase
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CartInQuickInvoiceFactoryUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.MakeQuickInvoiceUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.CreateProductValidationUseCase
import com.codeIn.myCash.ui.ProductInputType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class QuickInvoiceViewModel @Inject constructor(
    prefs: SharedPrefsModule,
    private val cartInQuickInvoiceFactoryUseCase: CartInQuickInvoiceFactoryUseCase,
    val createClientValidationUseCase: CreateClientValidationUseCase,
    val createVendorValidationUseCase: CreateVendorValidationUseCase,
    private val createClientUseCase: CreateClientUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val createProductValidationUseCase: CreateProductValidationUseCase,
    private val makeQuickInvoiceUseCase: MakeQuickInvoiceUseCase,
) : ViewModel() {


    private val _invoiceType = MutableLiveData(InvoiceType.SIMPLE)
    val invoiceType: LiveData<InvoiceType> = _invoiceType

    private val _selectedProductsInCart = MutableLiveData<ProductsInCartInQuickInvoice?>(null)
    val selectedProductsInCart : LiveData<ProductsInCartInQuickInvoice?> = _selectedProductsInCart

    var tax : String? = prefs.getValue(AppConstants.TAX)

    private val _paymentType = MutableLiveData(PaymentType.CASH)
    val paymentType: LiveData<PaymentType> = _paymentType

    private val _clientDataResult = MutableLiveData<ClientState>(ClientState.Idle)
    val clientDataResult : LiveData<ClientState> = _clientDataResult

    private val _invoiceDataResult = MutableStateFlow<InvoiceState>(InvoiceState.Idle)
    val invoiceDataResult = _invoiceDataResult.asStateFlow()

    private val _searchText = MutableStateFlow<String?>("")
    val searchText = _searchText.asStateFlow()


    private val _invalidInput = MutableLiveData<InvalidInput>(InvalidInput.INITIAL)
    val invalidInput: LiveData<InvalidInput> = _invalidInput

    private var _currency = MutableLiveData(prefs.getValue(Constants.getCurrency()))
    val currency : LiveData<String?> = _currency
    private var currencyFlow = prefs.getStringFlowForKey(Constants.getCurrency())
        .stateIn(viewModelScope, SharingStarted.Eagerly, "SAR")

    //***************************************
    private val mProductNameLiveData = MutableLiveData<String>()
    private val mProductPriceLiveData = MutableLiveData<String>()
    private val mProductQuantityLiveData = MutableLiveData<String>()
    val mAddProductMediator = MediatorLiveData<Boolean>()


    init {
        launchIO {
            currencyFlow.collect {
                _currency.postValue(it)
                _selectedProductsInCart.postValue(ProductsInCartInQuickInvoice())
            }
        }
        mAddProductMediator.addSource(mProductNameLiveData) { validateForm() }
        mAddProductMediator.addSource(mProductPriceLiveData) { validateForm() }
        mAddProductMediator.addSource(mProductQuantityLiveData) { validateForm() }
    }

    fun setProductInputs(inputType: ProductInputType, value: String) {
        when (inputType) {
            ProductInputType.NAME -> mProductNameLiveData.value = value
            ProductInputType.QUANTITY -> mProductQuantityLiveData.value = value
            else -> mProductPriceLiveData.value = value
        }
    }

    private fun validateForm() = try {
        // put your validation logic here, and update the following value
        // as `true` or `false` based on validation result
        // mLoginPasswordMediator.value = ...
        val nameValidator = !mProductNameLiveData.value.isNullOrEmpty()
        val priceValidator = !mProductQuantityLiveData.value.isNullOrEmpty() && mProductQuantityLiveData.value!!.toInt() > 0
        val amountValidator = !mProductPriceLiveData.value.isNullOrEmpty() && mProductPriceLiveData.value!!.toInt() > 0

        mAddProductMediator.value = nameValidator && priceValidator && amountValidator
    } catch (e: Exception) { }

    fun updateInvoiceType(type: InvoiceType) {
        if (type == _invoiceType.value) return
        _invoiceType.value = type
        _selectedProductsInCart.value?.invoiceType = type
    }

    fun updatePaymentType(type: PaymentType) {
        if (_paymentType.value != type)
            _paymentType.postValue(type)
    }

    fun handleProductInCart(product: ProductInQuickInvoice, type: Cart) {
        launchMain {
            cartInQuickInvoiceFactoryUseCase.invoke(product , selectedProductsInCart.value , type).let {
                _selectedProductsInCart.value = it
                if (type == Cart.ADD)
                    _invalidInput.value = InvalidInput.INITIAL
                Log.d("TAG" , "Product in cart in quick invocie $product ${_selectedProductsInCart.value}")
            }
        }
    }


    fun updateSearch(searchValue: String?) {
        _searchText.value = searchValue
    }

    fun updateClientIdInInvoiceData(clientId: String?) {
        _selectedProductsInCart.value?.clientId = clientId
    }

    fun clearStateClient() {
        if (_clientDataResult.value != ClientState.Idle)
            _clientDataResult.value = ClientState.Idle
    }

    fun clearStateInvoice() {
        if (_invoiceDataResult.value != InvoiceState.Idle)
            _invoiceDataResult.value = InvoiceState.Idle
    }

    fun clearSelectedCart() {
        _selectedProductsInCart.value = ProductsInCartInQuickInvoice()
    }

    fun updateClientRequest(request: ClientRequest?) {
        _selectedProductsInCart.value?.clientRequest = request
    }

    fun getClients(phone: String?) {
        val type = selectedProductsInCart.value?.invoiceType ?: InvoiceType.SIMPLE
        launchIO {
            getClientsUseCase.invoke(
                phone,
                null, null,
                type.value.toString(),
                Limit.HUNDRED.value.toString()
            ).let {
                _clientDataResult.postValue(it)
            }
        }
    }

    fun createClient() = launchIO {
        val request = AddClientRequest(
            name = selectedProductsInCart.value?.clientRequest?.name ?: "",
            phone = selectedProductsInCart.value?.clientRequest?.phone ?: "",
            type = ClientsSection.CLIENTS.value,
            commercialRecordNo = selectedProductsInCart.value?.clientRequest?.commercialNumber,
            taxNo = selectedProductsInCart.value?.clientRequest?.taxNumber,
            address = selectedProductsInCart.value?.clientRequest?.address,
            email = selectedProductsInCart.value?.clientRequest?.email,
            countryId = "1",
            extra = null,
            clientId = null
        )
        createClientUseCase.invoke(request).let {
            _clientDataResult.postValue(it)
        }
    }

    fun addProductInQuickInvoice(name : String , price : String? , quantity : String? , createInvoice : Boolean = false  ){
        launchIO {
            createProductValidationUseCase.invoke(
                name, price, quantity , "0.0"
            ).let {
                _invalidInput.postValue(it)
            }
        }
    }

    fun makeQuickInvoice(cashPrice : String? , visaPrice : String? ,
                         date: String? , product: ProductInQuickInvoice?,
                         runRefundInvoice : String?= null , codeRefundInvoice : String?= null , dateRefundInvoice : String?= null ){
        launchIO {
            _invoiceDataResult.value = InvoiceState.Loading
            makeQuickInvoiceUseCase.invoke(
                cashPrice= cashPrice , visaPrice = visaPrice , nextData = date ,
                products = selectedProductsInCart.value ,
                paymentType = paymentType.value?.value ?: PaymentType.CASH.value ,
                product = product , runRefundInvoice = runRefundInvoice , codeRefundInvoice = codeRefundInvoice ,
                dateRefundInvoice = dateRefundInvoice
            ).let {
                _invoiceDataResult.emit(it)
            }
        }
    }

    override fun onCleared() {
        // DO NOT forget to remove sources from mediator
        mAddProductMediator.removeSource(mProductNameLiveData)
        mAddProductMediator.removeSource(mProductPriceLiveData)
        mAddProductMediator.removeSource(mProductQuantityLiveData)
    }
}