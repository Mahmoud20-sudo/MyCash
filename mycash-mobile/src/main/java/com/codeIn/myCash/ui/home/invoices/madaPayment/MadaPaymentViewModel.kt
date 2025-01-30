package com.codeIn.myCash.ui.home.invoices.madaPayment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.offline.SharedPrefsModule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MadaPaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefs : SharedPrefsModule,
) : ViewModel() {

    var amount = MutableLiveData<String>("")
    var rrn = MutableLiveData<String>("")


}