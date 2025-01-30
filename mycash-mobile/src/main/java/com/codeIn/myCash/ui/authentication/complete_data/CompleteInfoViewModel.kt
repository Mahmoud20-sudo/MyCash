package com.codeIn.myCash.ui.authentication.complete_data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.complete_data.model.CompleteInfoState
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftState
import com.codeIn.myCash.features.user.domain.authentication.usecases.LoginUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.SaveCacheUseCase
import com.codeIn.myCash.features.user.domain.complete_data.usecase.CompleteInfoUseCase
import com.codeIn.myCash.features.user.domain.complete_data.usecase.LogoutUseCase
import com.codeIn.myCash.features.user.domain.shift.usecases.EndShiftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CompleteInfoViewModel @Inject constructor(
    private val completeInfoUseCase: CompleteInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _completeInfoState = MutableStateFlow<CompleteInfoState>(CompleteInfoState.Idle)
    val completeInfoState = _completeInfoState.asStateFlow()

    private val _logoutResult = MutableStateFlow<CompleteInfoState>(CompleteInfoState.Idle)
    val logoutResult = _logoutResult.asStateFlow()

    fun completeData(
        commercialRecord: String,
        commercialRecordName: String,
        tax: String,
        taxRecord: String,
        branchName: String,
        branchAddress: String
    ) {
        launchIO {
            _completeInfoState.emit(CompleteInfoState.Loading)
            completeInfoUseCase.invoke(
                commercialRecord = commercialRecord,
                commercialRecordName = commercialRecordName,
                tax = tax,
                taxRecord = taxRecord,
                branchName = branchName,
                branchAddress = branchAddress,
            ).let {
                _completeInfoState.emit(it)
            }
        }
    }

    fun logout() {
        launchIO {
            logoutUseCase.invoke().let {
                _logoutResult.emit(it)
            }
        }
    }

    fun clearState() {
        if (_completeInfoState.value != CompleteInfoState.Idle)
            _completeInfoState.value = CompleteInfoState.Idle

        if(_logoutResult.value != CompleteInfoState.Idle)
            _logoutResult.value = CompleteInfoState.Idle
    }
}