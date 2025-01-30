package com.codeIn.myCash.ui.home.products.second_step_create_invoice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.Limit
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.common.data.PaymentType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker.Companion.checkNotPOSAndNFC
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.model.ClientRequest
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientValidationUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateVendorValidationUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.GetClientsUseCase
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.usecases.ConfirmVisaUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.MakeInvoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SecondStepCreationInvoiceViewModel @Inject constructor(
    private val state: SavedStateHandle,
    val createClientValidationUseCase: CreateClientValidationUseCase,
    val createVendorValidationUseCase: CreateVendorValidationUseCase,
    private val createClientUseCase: CreateClientUseCase,
    private val makeInvoiceUseCase: MakeInvoiceUseCase,
    prefs: SharedPrefsModule,
    private val getClientsUseCase: GetClientsUseCase,
    private val confirmVisaUseCase: ConfirmVisaUseCase
) : ViewModel() {

    private val _selectedProductsInCart = MutableLiveData<ProductsInCart?>(null)
    val selectedProductsInCart: LiveData<ProductsInCart?> = _selectedProductsInCart

    private val _clientDataResult = MutableLiveData<ClientState>(ClientState.Idle)
    val clientDataResult: LiveData<ClientState> = _clientDataResult

    private val _invoiceDataResult = MutableStateFlow<InvoiceState>(InvoiceState.Idle)
    val invoiceDataResult = _invoiceDataResult.asStateFlow()

    private val _paymentType = MutableLiveData(PaymentType.CASH)
    val paymentType: LiveData<PaymentType> = _paymentType

    var currency: String? = prefs.getValue(Constants.getCurrency())
    var tax: Int? = prefs.getValue(AppConstants.TAX).run {
        this?.toFloat()?.roundToInt()
    }

    private val _searchText = MutableStateFlow<String?>("")
    val searchText = _searchText.asStateFlow()

    var invoiceId = MutableLiveData<String?>("")

    init {
        _selectedProductsInCart.value = state["products_in_cart"]

    }

    fun updatePaymentType(type: PaymentType) {
        if (_paymentType.value != type)
            _paymentType.postValue(type)
    }

    fun createClient() {
        launchIO {
            val type =
                if (selectedProductsInCart.value?.mainTypeInvoice?.value == MainTypeInvoice.PURCHASE.value)
                    ClientsSection.VENDORS
                else
                    ClientsSection.CLIENTS

            createClientUseCase.invoke(
                AddClientRequest(
                    name = selectedProductsInCart.value?.clientRequest?.name ?: "",
                    phone = selectedProductsInCart.value?.clientRequest?.phone ?: "",
                    commercialRecordNo = selectedProductsInCart.value?.clientRequest?.commercialNumber,
                    taxNo = selectedProductsInCart.value?.clientRequest?.taxNumber,
                    address = selectedProductsInCart.value?.clientRequest?.address,
                    email = selectedProductsInCart.value?.clientRequest?.email,
                    countryId = "1",
                    type = type.value
                )
            ).let {
                _clientDataResult.postValue(it)
            }
        }
    }

    fun updateClientRequest(request: ClientRequest?) {
        _selectedProductsInCart.value?.clientRequest = request
    }

    fun clearStateClient() {
        if (_clientDataResult.value != ClientState.Idle)
            _clientDataResult.value = ClientState.Idle
    }

    fun clearStateInvoice() {
        if (_invoiceDataResult.value != InvoiceState.Idle)
            _invoiceDataResult.value = InvoiceState.Idle
    }

    fun updateClientIdInInvoiceData(clientId: String?) {
        _selectedProductsInCart.value?.clientId = clientId
    }

    fun makeInvoice(cashPrice: String?, visaPrice: String?, date: String?) {
        launchIO {
            Log.d("TAG", "data is invoice $cashPrice , $visaPrice , ${paymentType.value?.value}")
            _invoiceDataResult.value = InvoiceState.Loading
            makeInvoiceUseCase.invoke(
                cashPrice = cashPrice,
                visaPrice = visaPrice,
                nextData = date,
                products = selectedProductsInCart.value,
                paymentType = paymentType.value?.value ?: PaymentType.CASH.value
            ).let {
                _invoiceDataResult.emit(it)
            }
        }
    }

    fun confirmVisa(
        invoiceId: String?, runRefundInvoice: String? = null,
        codeRefundInvoice: String? = null, dateRefundInvoice: String? = null,
    ) {
        launchIO {
            confirmVisaUseCase.invoke(
                invoiceId,
                runRefundInvoice, codeRefundInvoice, dateRefundInvoice
            ).let {
                _invoiceDataResult.emit(it)
            }
        }
    }

    fun updateSearch(searchValue: String?) {
        _searchText.value = searchValue
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

    fun payWithCard(value: String?) {

    }
}
