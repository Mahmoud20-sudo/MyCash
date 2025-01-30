package com.codeIn.myCash.ui.home.expenses.see_more_expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.ExpensesFilter
import com.codeIn.common.data.Limit
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.domain.expenses.usecases.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SeeMoreExpensesViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    prefs : SharedPrefsModule
) : ViewModel() {

    private val _dataResult = MutableStateFlow<ExpenseState>(ExpenseState.Idle)
    val dataResult = _dataResult.asStateFlow()

    private val _duration = MutableStateFlow<ExpensesFilter>(ExpensesFilter.DAILY)
    val duration  = _duration.asStateFlow()

    var currency : String? = prefs.getValue(Constants.getCurrency())

    private var _searchText = MutableLiveData<String?>(null)
    val searchText : LiveData<String?> = _searchText

    private var _dateText = MutableLiveData<String?>(null)
    val dateText : LiveData<String?> = _dateText

    init {
        getExpenses(_duration.value)
    }
    fun getExpenses(type : ExpensesFilter){
        launchIO {
            _dataResult.emit(ExpenseState.Loading)
            getExpensesUseCase.invoke(type.value.toString() ,
                limit = Limit.HUNDRED.value.toString() ,
                search = searchText.value , date = dateText.value ).let {
                _dataResult.emit(it)
            }
        }
    }
    fun updateSearchText(query : String?){
        _searchText.value = query
        getExpenses(type = _duration.value)
    }
    fun updateDateText(query : String?){
        _dateText.value = query
    }

    fun updateDurationType(filter: ExpensesFilter) {
        if (_duration.value != filter)
            _duration.value = filter
    }


    fun clearState(){
        _dataResult.value = ExpenseState.Idle
    }
}