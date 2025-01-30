package com.codeIn.myCash.ui.home.invoices.madaTransactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionState
import com.codeIn.myCash.features.stock.domain.madaTransactions.usecases.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MadaTransactionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTransactionsVisaUseCase: GetTransactionsUseCase ,
    prefs : SharedPrefsModule,
) : ViewModel() {

    private val _transactionResult = MutableLiveData<TransactionState>(TransactionState.Idle)
    val transactionResult : LiveData<TransactionState> = _transactionResult

    var currency : String? = prefs.getValue(Constants.getCurrency())


    init {
        getTransaction()
    }

    private fun getTransaction(){
        _transactionResult.value = TransactionState.Loading
        launchIO {
            getTransactionsVisaUseCase.invoke().let {
                _transactionResult.postValue(it)
            }
        }
    }

    fun clearState(){
        _transactionResult.value = TransactionState.Idle
    }

}