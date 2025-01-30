package com.codeIn.myCash.ui.authentication.sign_up.payment_gateway

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AuthConsts
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentGatewayViewModel @Inject constructor(
    state: SavedStateHandle,
    val prefs : SharedPrefsModule,
) : ViewModel() {
    private val _payResult = MutableStateFlow<GeneralResponse>(GeneralResponse.Idle)
    val payResult = _payResult.asStateFlow()

    private var _total = MutableStateFlow<String?>("0.0")
    val total = _total.asStateFlow()

    fun updatePaymentStatus(status : GeneralResponse){
        _payResult.value = status
    }
}