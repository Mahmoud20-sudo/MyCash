package com.codeIn.myCash.ui.home.invoices.memorandum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumModel
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.GetSingleMemorandumUseCase
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.domain.accountSettings.usecases.GetInvoiceSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MemorandumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInvoiceSettingsUseCase: GetInvoiceSettingsUseCase,
    private val getSingleMemorandumUseCase: GetSingleMemorandumUseCase,
    prefs : SharedPrefsModule,
) : ViewModel() {

    var currency : String? = prefs.getValue(Constants.getCurrency())

    private val _memorandumId= MutableLiveData<String?>()
    val memorandumId : LiveData<String?> = _memorandumId

    private val _memorandumModel = MutableLiveData<MemorandumModel?>()
    val memorandumModel: LiveData<MemorandumModel?> = _memorandumModel

    private val _invoiceSettingsDataResult = MutableStateFlow<InvoiceSettingsState>(
        InvoiceSettingsState.Idle)
    val invoiceSettingsDataResult = _invoiceSettingsDataResult.asStateFlow()

    private val _memorandumDataResult = MutableStateFlow<MemorandumState>(MemorandumState.Idle)
    val memorandumDataResult = _memorandumDataResult.asStateFlow()

    init {

        if (savedStateHandle.contains("memorandum_id"))
            _memorandumId.postValue(savedStateHandle["memorandum_id"])

        getInvoiceSettings()
    }

    private fun getInvoiceSettings(){
        launchIO {
            getInvoiceSettingsUseCase.invoke().let {
                _invoiceSettingsDataResult.emit(it)
            }
        }
    }

    fun updateMemorandumModel(memorandumModel: MemorandumModel?){
        _memorandumDataResult.value = MemorandumState.Idle
        _memorandumModel.value = memorandumModel
    }

    fun getSingleMemorandum(){
        _memorandumDataResult.value = MemorandumState.Loading
        launchIO {
            getSingleMemorandumUseCase.invoke(memorandumId.value).let {
                _memorandumDataResult.emit(it)
            }
        }
    }
}