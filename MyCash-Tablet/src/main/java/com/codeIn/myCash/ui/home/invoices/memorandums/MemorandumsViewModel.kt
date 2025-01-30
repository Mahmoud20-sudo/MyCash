package com.codeIn.myCash.ui.home.invoices.memorandums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.Limit
import com.codeIn.common.data.NoteType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.ui.home.invoices.CreditorDebtorNote
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.GetMemorandumsUseCase
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.GetSingleMemorandumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemorandumsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    prefs : SharedPrefsModule,
    private val getMemorandumsUseCase: GetMemorandumsUseCase ,
    private val gteSingleMemorandumUseCase: GetSingleMemorandumUseCase
) : ViewModel() {

    private var invoiceId : String? = ""

    private val _filter = MutableLiveData<NoteType>()
    val filter: LiveData<NoteType> = _filter

    private val _memorandumDataResult = MutableStateFlow<MemorandumState>(MemorandumState.Idle)
    val memorandumDataResult = _memorandumDataResult.asStateFlow()

    var currency : String? = prefs.getValue(Constants.getCurrency())
    init {
        _filter.postValue(NoteType.ALL)

        if (savedStateHandle.contains("invoice_id"))
            invoiceId = savedStateHandle["invoice_id"]

        getMemorandums()
    }

    fun updateFilter(filter: NoteType) {
        if (_filter.value == filter) return
        _filter.postValue(filter)
        handleMemorandum(filter)
    }

    fun handleMemorandum(filter: NoteType){
        when(filter){
            NoteType.ALL -> getMemorandums(null)
            else -> getMemorandums(filter.value.toString())
        }
    }
    private fun getMemorandums(type : String? = null,){
        launchIO {
            getMemorandumsUseCase.invoke(invoiceId  , type, limit = Limit.HUNDRED.value.toString()).let {
                _memorandumDataResult.emit(it)
            }
        }
    }

    fun clearState(){
        _memorandumDataResult.value = MemorandumState.Idle
    }

    fun getSingleMemorandum(memorandumId : String?){
        launchIO {
            gteSingleMemorandumUseCase.invoke(memorandumId).let {
                _memorandumDataResult.emit(it)
            }
        }
    }
}