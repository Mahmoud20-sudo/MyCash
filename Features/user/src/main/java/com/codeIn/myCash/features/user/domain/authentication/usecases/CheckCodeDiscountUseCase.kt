package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountState
import com.codeIn.myCash.features.user.domain.authentication.repository.CheckCodeDiscountRepository
import javax.inject.Inject

class CheckCodeDiscountUseCase @Inject constructor(private val checkCodeDiscountRepository: CheckCodeDiscountRepository){
    suspend operator fun invoke(discount : String?, countryId : String?) : DiscountState {
        return checkCodeDiscountRepository.checkCodeDiscount(discount , countryId)
    }
}