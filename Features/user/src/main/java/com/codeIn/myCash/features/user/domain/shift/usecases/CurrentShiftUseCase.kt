package com.codeIn.myCash.features.user.domain.shift.usecases

import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftState
import com.codeIn.myCash.features.user.domain.shift.repository.ShiftRepository
import javax.inject.Inject

class CurrentShiftUseCase @Inject constructor(private val shiftRepository: ShiftRepository) {
    suspend operator fun invoke() : ShiftState {
        return shiftRepository.currentShift()
    }
}