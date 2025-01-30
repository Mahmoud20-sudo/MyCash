package com.codeIn.myCash.ui.home.expenses.update_expense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseModel
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.domain.expenses.usecases.UpdateExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.io.File
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UpdateExpenseViewModel @Inject constructor(
    private val state: SavedStateHandle ,
    private val updateExpenseUseCase: UpdateExpenseUseCase
) : ViewModel() {

    private val _expense = MutableStateFlow<ExpenseModel?>(null)
    val expense = _expense.asStateFlow()

    private val _expenseDataResult = MutableStateFlow<ExpenseState>(ExpenseState.Idle)
    val expenseDataResult = _expenseDataResult.asStateFlow()


    init {
        _expense.value = state["expense"]
    }

    //called only when app is minimized or image picker is launched
    fun saveState(expenseModel: ExpenseModel?){
        state["expense"] = expenseModel
    }


    fun updateExpense(statement: String? , amount: String? , date : String? ,
                      note: String? , additionalInfo : String? , file : File? , tax : String?){
        launchIO {
            _expenseDataResult.emit(ExpenseState.Loading)
            updateExpenseUseCase.invoke(expense.value?.id.toString(),statement, amount, date, note, additionalInfo, file , tax).let {
                _expenseDataResult.emit(it)
            }
        }
    }
    fun clearState() {
        if (_expenseDataResult.value != ExpenseState.Idle)
            _expenseDataResult.value = ExpenseState.Idle
    }

}