package com.codeIn.myCash.features.user.domain.shift.usecases

import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftState
import com.codeIn.myCash.features.user.domain.shift.repository.ShiftRepository
import javax.inject.Inject

class StartShiftUseCase @Inject constructor(private val shiftRepository: ShiftRepository) {
    suspend operator fun invoke(cash : String? , visa : String? , differentCash : String? , differentVisa : String?) : ShiftState {
        return shiftRepository.startShift(cash = cash, visa = visa, differentCash= differentCash , differentVisa= differentVisa)
    }
}