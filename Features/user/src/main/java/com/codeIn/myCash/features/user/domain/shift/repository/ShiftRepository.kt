package com.codeIn.myCash.features.user.domain.shift.repository

import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftState

interface ShiftRepository {

    suspend fun currentShift():ShiftState
    suspend fun startShift(cash : String? , visa : String? , differentCash : String? , differentVisa : String?):ShiftState
    suspend fun endShift(visa : String? , cash : String?):ShiftState

}