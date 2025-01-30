package com.codeIn.myCash.ui.home.expenses.add_expense

import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.sales.domain.expenses.usecases.CreateExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val createExpenseUseCase: CreateExpenseUseCase
) : ViewModel() {

    private val _expenseDataResult = MutableStateFlow<ExpenseState>(ExpenseState.Idle)
    val expenseDataResult = _expenseDataResult.asStateFlow()


    fun createExpense(statement: String? , amount: String? , date : String? ,
                      note: String? , additionalInfo : String? , file : File? , tax : String?){
        launchIO {
            _expenseDataResult.emit(ExpenseState.Loading)
            createExpenseUseCase.invoke(statement, amount, date, note, additionalInfo, file , tax).let {
                _expenseDataResult.emit(it)
            }
        }
    }

    fun clearState() {
        if (_expenseDataResult.value != ExpenseState.Idle)
            _expenseDataResult.value = ExpenseState.Idle
    }

}
