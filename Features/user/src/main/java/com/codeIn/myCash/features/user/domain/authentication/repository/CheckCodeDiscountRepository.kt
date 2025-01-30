package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountState

interface CheckCodeDiscountRepository {
    suspend fun checkCodeDiscount(
        discount : String?,
        countryId : String?
    ) : DiscountState

}