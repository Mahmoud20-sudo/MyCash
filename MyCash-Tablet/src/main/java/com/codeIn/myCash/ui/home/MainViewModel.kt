package com.codeIn.myCash.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.data.AppConstants.DEVICE_REGISTER
import com.codeIn.common.data.AppConstants.INFLUNCER_ID_REGISTER
import com.codeIn.common.data.AppConstants.PACKAGE_REGISTER
import com.codeIn.common.data.AppConstants.TAX
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionState
import com.codeIn.myCash.features.stock.domain.madaTransactions.usecases.CreateMadaTransactionsVisaUseCase
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftState
import com.codeIn.myCash.features.user.domain.authentication.usecases.GetProfileInfoUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.PaymentLinkUseCase
import com.codeIn.myCash.features.user.domain.shift.usecases.CurrentShiftUseCase
import com.codeIn.myCash.features.user.domain.shift.usecases.EndShiftUseCase
import com.codeIn.myCash.features.user.domain.shift.usecases.StartShiftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val endShiftUseCase: EndShiftUseCase,
    private val startShiftUseCase: StartShiftUseCase,
    private val currentShiftUseCase: CurrentShiftUseCase,
    private val infoUseCase : GetProfileInfoUseCase,
    val pref : SharedPrefsModule,
    private val paymentLinkUseCase: PaymentLinkUseCase,
    ) : ViewModel() {
    private val _generalResult = MutableStateFlow<GeneralResponse>(GeneralResponse.Idle)
    val generalResult = _generalResult.asStateFlow()

    private val _dataResult = MutableStateFlow<ShiftState>(ShiftState.Idle)
    val dataResult = _dataResult.asStateFlow()

    private val _userResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val userResult = _userResult.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _shiftStatus = MutableStateFlow<Int>(0)
    val shiftStatus = _shiftStatus.asStateFlow()

    var expire = MutableLiveData<Boolean>(false)
    var warning = MutableLiveData<Boolean>(false)
    var isPaid = MutableLiveData<Boolean>(true)
    var warningMessage = MutableLiveData<String?>("")

    private var _isCompleteInfo = MutableLiveData(true)
    val isCompleteInfo : LiveData<Boolean> = _isCompleteInfo
    private var isCompleteInfoFlow = pref.getStringFlowForKey(Constants.completeInoStore())
        .stateIn(viewModelScope, SharingStarted.Eagerly, "0")
    init {
        profile()
        launchIO {
            isCompleteInfoFlow.collect {
                _isCompleteInfo.postValue(it == "1")
            }
        }
    }

    fun logout(visa : String? , cash : String?){
        launchIO {
            endShiftUseCase.invoke(visa = visa , cash= cash).let {
                _dataResult.emit(it)
                Log.d("TAG" , "Logout $it")
            }
        }
    }

    fun startShift(cash : String? , visa : String? , differentCash : String? , differentVisa : String?){
        launchIO {
            startShiftUseCase.invoke(visa = visa , cash= cash , differentCash = differentCash , differentVisa = differentVisa).let {
                _dataResult.emit(it)
                Log.d("TAG" , "$visa , $cash Start Shift $it")
            }
        }
    }

    fun currentShift(){
        launchIO {
            currentShiftUseCase.invoke().let {
                _dataResult.emit(it)
                Log.d("TAG" , " Current shift $it")
            }
        }
    }

    fun profile(){
        launchIO {
            infoUseCase.invoke().let {
                _userResult.emit(it)
                Log.d("TAG" , " Profile $it")
            }
        }
    }

    fun updateUser(user : User?){
        launchIO {
            _user.emit(user)
        }
    }

    fun clearAuthenticationState(){
        if (_userResult.value != AuthenticationState.Idle)
            _userResult.value = AuthenticationState.Idle
    }

    fun clearShiftState(){
        if (_dataResult.value != ShiftState.Idle)
            _dataResult.value = ShiftState.Idle
    }

    fun updateShiftState(value : Int){
        _shiftStatus.value = value
    }

    fun clearState(){
        _generalResult.value = GeneralResponse.Idle
    }

    fun saveTax(value : String){
        pref.putValue(TAX , value)
    }

    fun getPaymentLink(){
        launchIO {
            _generalResult.value = GeneralResponse.Loading
            paymentLinkUseCase.invoke(
                pref.getValue(INFLUNCER_ID_REGISTER)?:"",
                pref.getValue(PACKAGE_REGISTER)?:"",
                pref.getValue(DEVICE_REGISTER)?:"")
                .let {
                    _generalResult.emit(it)
                    Log.d("TAG" , "Paid is $it")
                }
        }
    }

}