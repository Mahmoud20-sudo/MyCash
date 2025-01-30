package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.common.util.GeneralResponse

interface PaymentLinkRepository {
    suspend fun getPaymentLink(influenceId : String?, packageId : String?, deviceId : String?) : GeneralResponse
    suspend fun getRenewPaymentLink(influenceId : String?, packageId : String?, deviceId : String?) : GeneralResponse
}