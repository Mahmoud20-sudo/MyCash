package com.codeIn.myCash.ui.home.products.products.quickInvoice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Cart
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.Limit
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.common.data.PaymentType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
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
import com.codeIn.myCash.features.stock.domain.invoice.usecases.ConfirmVisaUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.MakeQuickInvoiceUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.CreateProductValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class QuickInvoiceViewModel @Inject constructor(
    prefs : SharedPrefsModule,
    private val cartInQuickInvoiceFactoryUseCase: CartInQuickInvoiceFactoryUseCase,
    val createClientValidationUseCase: CreateClientValidationUseCase,
    val createVendorValidationUseCase: CreateVendorValidationUseCase,
    private val createClientUseCase: CreateClientUseCase,
    private val getClientsUseCase: GetClientsUseCase ,
    private val createProductValidationUseCase : CreateProductValidationUseCase,
    private val makeQuickInvoiceUseCase: MakeQuickInvoiceUseCase ,
) : ViewModel() {


    private val _invoiceType = MutableLiveData(InvoiceType.SIMPLE)
    val invoiceType: LiveData<InvoiceType> = _invoiceType

    private val _mainInvoiceType = MutableLiveData(MainTypeInvoice.PURCHASE)
    val mainInvoiceType: LiveData<MainTypeInvoice> = _mainInvoiceType

    private val _selectedProductsInCart = MutableLiveData<ProductsInCartInQuickInvoice?>(null)
    val selectedProductsInCart : LiveData<ProductsInCartInQuickInvoice?> = _selectedProductsInCart

    var currency : String? = prefs.getValue(Constants.getCurrency())
    var tax : String? = prefs.getValue(AppConstants.TAX)

    private val _paymentType = MutableLiveData(PaymentType.CREDIT_CARD)
    val paymentType: LiveData<PaymentType> = _paymentType

    private val _clientDataResult = MutableLiveData<ClientState>(ClientState.Idle)
    val clientDataResult : LiveData<ClientState> = _clientDataResult

    private val _invoiceDataResult = MutableStateFlow<InvoiceState>(InvoiceState.Idle)
    val invoiceDataResult = _invoiceDataResult.asStateFlow()

    private val _searchText = MutableStateFlow<String?>("")
    val searchText = _searchText.asStateFlow()


    private val _invalidInput = MutableLiveData<InvalidInput>(InvalidInput.INITIAL)
    val invalidInput: LiveData<InvalidInput> = _invalidInput
    init {
        _selectedProductsInCart.postValue(ProductsInCartInQuickInvoice())
    }
    fun updateInvoiceType(type: InvoiceType) {
        if (type == _invoiceType.value) return
        _invoiceType.value =  type
        _selectedProductsInCart.value?.invoiceType = type
    }

    fun updateMainInvoiceType(type: MainTypeInvoice) {
        if (type == _mainInvoiceType.value) return
        _mainInvoiceType.value =  type
        _selectedProductsInCart.value?.mainTypeInvoice = type
    }

    fun updatePaymentType(type: PaymentType) {
        if (_paymentType.value != type)
            _paymentType.postValue(type)
    }

    fun handleProductInCart(product: ProductInQuickInvoice, type : Cart) {
        launchMain {
            cartInQuickInvoiceFactoryUseCase.invoke(product , selectedProductsInCart.value , type).let {
                _selectedProductsInCart.value = it
                if (type == Cart.ADD)
                    _invalidInput.value = InvalidInput.INITIAL
                Log.d("TAG" , "Product in cart in quick invocie $product ${_selectedProductsInCart.value}")
            }
        }
    }


    fun updateSearch(searchValue : String?){
        _searchText.value = searchValue
    }

    fun updateClientIdInInvoiceData(clientId : String?){
        _selectedProductsInCart.value?.clientId = clientId
    }

    fun clearStateClient(){
        if (_clientDataResult.value != ClientState.Idle)
            _clientDataResult.value = ClientState.Idle
    }

    fun clearStateInvoice(){
        if (_invoiceDataResult.value != InvoiceState.Idle)
            _invoiceDataResult.value = InvoiceState.Idle
    }

    fun clearSelectedCart(){
        _selectedProductsInCart.value = ProductsInCartInQuickInvoice()
    }
    fun updateClientRequest(request: ClientRequest?){
        _selectedProductsInCart.value?.clientRequest = request
    }

    fun getClients(phone : String?){
        val type = selectedProductsInCart.value?.invoiceType ?: InvoiceType.SIMPLE
        launchIO {
            getClientsUseCase.invoke(phone ,
                null , null ,
                type.value.toString() ,
                Limit.HUNDRED.value.toString()).let {
                _clientDataResult.postValue(it)
            }
        }
    }

    fun createClient(){
        launchIO {
            val type = ClientsSection.CLIENTS
            createClientUseCase.invoke(name = selectedProductsInCart.value?.clientRequest?.name ,
                phone = selectedProductsInCart.value?.clientRequest?.phone ,
                commercialRecordNo = selectedProductsInCart.value?.clientRequest?.commercialNumber ,
                taxNo = selectedProductsInCart.value?.clientRequest?.taxNumber ,
                address = selectedProductsInCart.value?.clientRequest?.address,
                email = selectedProductsInCart.value?.clientRequest?.email ,
                countryId = "1" ,
                extra = null ,
                type = type.value.toString()
            ).let {
                _clientDataResult.postValue(it)
            }
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
}