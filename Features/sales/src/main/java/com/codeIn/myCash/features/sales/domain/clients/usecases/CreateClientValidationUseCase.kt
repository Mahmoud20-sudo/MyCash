package com.codeIn.myCash.features.sales.domain.clients.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateCommercialOrTaxRecordUseCase
import com.codeIn.common.util.Validation
import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import javax.inject.Inject

class CreateClientValidationUseCase @Inject constructor(
    private val validationUseCase: Validation,
    private val commercialOrTaxRecordUseCase: ValidateCommercialOrTaxRecordUseCase
) {
    operator fun invoke(request: AddClientRequest): InvalidInput {
        return when {
            request.phone.isEmpty() -> InvalidInput.EMPTY
            request.name.isEmpty() -> InvalidInput.EMPTY
            validationUseCase.validatePhoneSaudi(request.phone).not() -> InvalidInput.PHONE_SAUDI
            request.email.isNullOrEmpty().not() && validationUseCase.validateEmail(
                request.email ?: ""
            ).not() -> InvalidInput.EMAIL

            request.taxNo.isNullOrEmpty()
                .not() && commercialOrTaxRecordUseCase.taxRecord(request.taxNo)
                .not() -> InvalidInput.TAX_RECORD

            request.commercialRecordNo.isNullOrEmpty()
                .not() && commercialOrTaxRecordUseCase.commercial(request.commercialRecordNo)
                .not() -> InvalidInput.COMMERCIAL_RECORD

            else -> InvalidInput.NONE
        }
    }
}