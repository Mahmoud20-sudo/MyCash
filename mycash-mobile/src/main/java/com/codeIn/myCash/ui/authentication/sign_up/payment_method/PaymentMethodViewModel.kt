package com.codeIn.myCash.ui.authentication.sign_up.payment_method

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants.INFLUNCER_ID_REGISTER
import com.codeIn.common.data.AuthConsts.DEVICE_SUBSCRIPTION
import com.codeIn.common.data.AuthConsts.PACKAGE_SUBSCRIPTION
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountCodeData
import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountState
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import com.codeIn.myCash.features.user.data.settings.remote.response.settings.SettingsState
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.features.user.domain.authentication.usecases.CheckCodeDiscountUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.PaymentLinkUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.RenewPaymentLinkUseCase
import com.codeIn.myCash.features.user.domain.settings.model.PaymentMethodState
import com.codeIn.myCash.features.user.domain.settings.usecases.GetSettingsUseCase
import com.codeIn.myCash.features.user.domain.settings.usecases.SetSubscriptionInfoUseCase
import com.codeIn.myCash.features.user.domain.settings.usecases.UpdateSubscriptionInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


/**
 * a ViewModel class for [PaymentMethodFragment] fragment. This class is responsible for handling UI logic and preparing vendorsData for [PaymentMethodFragment] fragment.
 */
@HiltViewModel
class PaymentMethodViewModel @Inject constructor(
    state: SavedStateHandle,
    private val checkCodeDiscountUseCase: CheckCodeDiscountUseCase,
    private val setSubscriptionInfoUseCase: SetSubscriptionInfoUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSubscriptionInfoUseCase: UpdateSubscriptionInfoUseCase,
    private val paymentLinkUseCase: PaymentLinkUseCase,
    private val getRenewPaymentLinkUseCase: RenewPaymentLinkUseCase,
    private val prefs : SharedPrefsModule,

    ) : ViewModel() {

    private val _discountResult = MutableStateFlow<DiscountState>(DiscountState.Idle)
    val discountResult = _discountResult.asStateFlow()

    private val _settingsResult = MutableStateFlow<SettingsState>(SettingsState.Idle)
    val settingsResult = _settingsResult.asStateFlow()

    private val _generalResult = MutableStateFlow<GeneralResponse>(GeneralResponse.Idle)
    val generalResult = _generalResult.asStateFlow()

    private val _viewState = MutableStateFlow(PaymentMethodState())
    val viewState = _viewState.asStateFlow()

    private var _selectedDevice = MutableStateFlow<DeviceModel?>(null)
    val selectedDevice = _selectedDevice.asStateFlow()

    private var _selectedPackage = MutableStateFlow<Package?>(null)
    val selectedPackage = _selectedPackage.asStateFlow()

    private var _influenceId = MutableStateFlow<String?>("")
    val influenceId = _influenceId.asStateFlow()

    private var _tax = MutableStateFlow<String?>("0.0")
    val tax = _tax.asStateFlow()

    init {
        if (state.contains(PACKAGE_SUBSCRIPTION)) {
            _selectedPackage.value = state[PACKAGE_SUBSCRIPTION]
        }
        if (state.contains(DEVICE_SUBSCRIPTION)) {
            _selectedDevice.value = state[DEVICE_SUBSCRIPTION]
        }

        getTaxValue()
    }

    fun getPaymentState(tax : String?){
        launchIO {
            _tax.value = tax
            setSubscriptionInfoUseCase.invoke(_selectedPackage.value , _selectedDevice.value , tax).let {
                _viewState.emit(it)
                Log.d("TAG" , "Result payment state: ${it.toString()}")
            }
        }
    }
    fun checkCodeDiscount(code: String) {
        launchIO {
            _viewState.value.discount = code
            _discountResult.emit(DiscountState.Loading)
            checkCodeDiscountUseCase.invoke(
                discount = code,
                countryId = "1"
            ).let {
                _discountResult.emit(it)
                Log.e("TAG", "result code result : $it")
            }
        }
    }

    private fun getTaxValue(){
        launchIO {
            getSettingsUseCase.invoke().let {
                _settingsResult.emit(it)
                Log.d("TAG" , "$it")
            }
        }
    }
    fun updateValues(data: DiscountCodeData?) {
        _influenceId.value = data?.id.toString()
        prefs.putValue(INFLUNCER_ID_REGISTER , data?.id.toString())
        launchIO {
            updateSubscriptionInfoUseCase.invoke(
                data?.discount , viewState.value , _tax.value
            ).let {
                _viewState.emit(it)
            }
        }
    }
    fun clearState() {
        _discountResult.value = DiscountState.Idle
        _generalResult.value = GeneralResponse.Idle
    }

    fun getPaymentLink(){
        launchIO {
            _generalResult.value = GeneralResponse.Loading
            paymentLinkUseCase.invoke(
                _influenceId.value,
                _selectedPackage.value?.id.toString(),
                _selectedDevice.value?.id.toString())
                .let {
                    _generalResult.emit(it)
                    Log.d("TAG" , "Payment gateway ${_influenceId.value} , ${_selectedDevice.value?.id} , ${_selectedPackage.value?.id} , $it")
                }
        }
    }
    fun getRenewPaymentLink(){
        launchIO {
            _generalResult.value = GeneralResponse.Loading
            getRenewPaymentLinkUseCase.invoke(
                _influenceId.value,
                _selectedPackage.value?.id.toString(),
                _selectedDevice.value?.id.toString())
                .let {
                    _generalResult.emit(it)
                    Log.d("TAG" , "Payment gateway ${_influenceId.value} , ${_selectedDevice.value?.id} , ${_selectedPackage.value?.id} , $it")
                }
        }
    }
}