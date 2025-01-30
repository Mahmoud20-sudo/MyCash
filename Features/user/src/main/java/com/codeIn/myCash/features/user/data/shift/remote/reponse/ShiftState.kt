package com.codeIn.myCash.features.user.data.shift.remote.reponse

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.ErrorEntity

sealed class ShiftState{
    data object Idle : ShiftState()
    data object Loading : ShiftState()
    data class StartShift(val data : ShiftData?) : ShiftState()
    data class CurrentShift(val data : ShiftData?) : ShiftState()
    data class EndShift(val data : String?) : ShiftState()
    data class Logout(val message : String?) : ShiftState()
    data class ServerError(val error : ErrorEntity) : ShiftState()
    data class InputError(val inputError : InvalidInput) : ShiftState()
    data object UnAuthorized : ShiftState()
    data class StateError(val message : String?) : ShiftState()
}