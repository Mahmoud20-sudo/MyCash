package com.codeIn.myCash.features.user.domain.complete_data.usecase

import com.codeIn.myCash.features.user.data.complete_data.model.CompleteInfoState
import com.codeIn.myCash.features.user.domain.complete_data.repository.LogoutRepository
import javax.inject.Inject


class LogoutUseCase @Inject constructor(private val logoutRepository: LogoutRepository) {
    suspend operator fun invoke() : CompleteInfoState = logoutRepository.logout()

}