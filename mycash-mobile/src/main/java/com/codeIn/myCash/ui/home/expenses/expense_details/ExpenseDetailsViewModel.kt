package com.codeIn.myCash.ui.home.expenses.expense_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseModel
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.domain.expenses.usecases.DeleteExpenseUseCase
import com.codeIn.myCash.features.sales.domain.expenses.usecases.GetSingleExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ExpenseDetailsViewModel @Inject constructor(
    state: SavedStateHandle ,
    private val deleteExpenseUseCase: DeleteExpenseUseCase ,
    private val getSingleExpenseUseCase: GetSingleExpenseUseCase ,
    prefs : SharedPrefsModule
) : ViewModel() {

    private val _expenseDataResult = MutableStateFlow<ExpenseState>(ExpenseState.Idle)
    val expenseDataResult = _expenseDataResult.asStateFlow()

    private val _expenseId = MutableStateFlow<String?>(null)
    val expenseId = _expenseId.asStateFlow()

    private val _expense = MutableStateFlow<ExpenseModel?>(null)
    val expense = _expense.asStateFlow()

    var currency : String? = prefs.getValue(Constants.getCurrency())

    init {
        _expenseId.value = state["expenseId"]
        if (_expenseId.value?.isNotEmpty() == true) {
            getSingleExpense()
        }
    }

    fun getSingleExpense(){
        launchIO {
            _expenseDataResult.emit(ExpenseState.Loading)
            getSingleExpenseUseCase.invoke(expenseId.value).let {
                _expenseDataResult.emit(it)
            }
        }
    }
    fun deleteExpense(){
        launchIO {
            _expenseDataResult.emit(ExpenseState.Loading)
            deleteExpenseUseCase.invoke(expenseId.value).let {
                _expenseDataResult.emit(it)
            }
        }
    }

    fun clearState(){
        if (_expenseDataResult.value != ExpenseState.Idle)
            _expenseDataResult.value = ExpenseState.Idle
    }

    fun updateExpense(expenseModel: ExpenseModel?){
        _expense.value = expenseModel
    }
}