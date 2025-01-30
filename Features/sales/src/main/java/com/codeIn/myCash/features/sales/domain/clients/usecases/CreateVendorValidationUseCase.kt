package com.codeIn.myCash.features.sales.domain.clients.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.ValidateCommercialOrTaxRecordUseCase
import com.codeIn.common.domain.usecases.ValidateEmailUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import com.codeIn.common.util.Validation
import javax.inject.Inject

class CreateVendorValidationUseCase @Inject constructor(
    private val validationUseCase: Validation,
    private val emptyUseCase: ValidateEmptyUseCase,
    private val emailUseCase: ValidateEmailUseCase,
    private val commercialOrTaxRecordUseCase: ValidateCommercialOrTaxRecordUseCase
) {
    operator fun invoke(
        name: String?,
        phone: String?,
        taxNo: String?,
        commercialRecordNo: String?,
        email: String?
    ): InvalidInput {
        return when {
            emptyUseCase.invoke(name) -> InvalidInput.EMPTY
            emptyUseCase.invoke(email) -> InvalidInput.EMPTY
            !emailUseCase.invoke(email) -> InvalidInput.EMAIL
            emptyUseCase.invoke(phone) -> InvalidInput.EMPTY
            !validationUseCase.validatePhoneSaudi(phone ?: "") -> InvalidInput.PHONE_SAUDI
            emptyUseCase.invoke(taxNo) -> InvalidInput.EMPTY
            !commercialOrTaxRecordUseCase.taxRecord(taxNo) -> InvalidInput.TAX_RECORD
            emptyUseCase.invoke(commercialRecordNo) -> InvalidInput.EMPTY
            !commercialOrTaxRecordUseCase.commercial(commercialRecordNo) -> InvalidInput.COMMERCIAL_RECORD
            else -> InvalidInput.NONE
        }
    }
}