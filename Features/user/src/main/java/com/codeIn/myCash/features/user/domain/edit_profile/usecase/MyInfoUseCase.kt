package com.codeIn.myCash.features.user.domain.edit_profile.usecase

import com.codeIn.myCash.features.user.domain.edit_profile.repository.MyInfoRepository
import javax.inject.Inject

class MyInfoUseCase @Inject constructor(
    private val repository: MyInfoRepository
) {
    suspend operator fun invoke() = repository.myInfo()
}