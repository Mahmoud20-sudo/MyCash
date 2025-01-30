package com.codeIn.myCash.features.user.domain.settings.usecases

import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.PackagesState
import com.codeIn.myCash.features.user.domain.settings.repository.PackagesRepository
import javax.inject.Inject

class GetPackagesUseCase @Inject constructor(private val packagesRepository: PackagesRepository) {
    suspend operator fun invoke(countryId : String?) : PackagesState {
        return packagesRepository.getPackages(countryId)
    }
}