package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.PaymentType
import com.codeIn.common.domain.usecases.MoneyValidationUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class MakeInvoiceValidationUseCase @Inject constructor(
    private val emptyUseCase: ValidateEmptyUseCase,
    private val moneyValidationUseCase: MoneyValidationUseCase,
){
    operator fun invoke(
        cashPrice : String?,
        visaPrice : String?,
        paymentType :Int,
        nextData : String?,
    ) : InvalidInput {
        when(paymentType){
            PaymentType.CASH.value -> {
                if(emptyUseCase.invoke(cashPrice))
                    return InvalidInput.EMPTY
                else if (!moneyValidationUseCase.invoke(cashPrice))
                    return InvalidInput.CASH
            }
            PaymentType.CREDIT_CARD.value -> {
                if(emptyUseCase.invoke(visaPrice))
                    return InvalidInput.EMPTY
                else if (!moneyValidationUseCase.invoke(visaPrice))
                    return InvalidInput.VISA
            }
            PaymentType.CASH_AND_CREDIT_CARD.value -> {
                if(emptyUseCase.invoke(cashPrice))
                    return InvalidInput.EMPTY
                else if (!moneyValidationUseCase.invoke(cashPrice))
                    return InvalidInput.CASH

                if(emptyUseCase.invoke(visaPrice))
                    return InvalidInput.EMPTY
                else if (!moneyValidationUseCase.invoke(visaPrice))
                    return InvalidInput.VISA
            }
            PaymentType.POST_PAID.value -> {
//                if(emptyUseCase.invoke(cashPrice))
//                    return InvalidInput.EMPTY
//                else
                if (!moneyValidationUseCase.invoke(cashPrice))
                    return InvalidInput.CASH

                if (emptyUseCase.invoke(nextData))
                    return InvalidInput.EMPTY
            }
            PaymentType.POST_PAID_AND_CREDIT_CARD.value -> {
                if(emptyUseCase.invoke(cashPrice))
                    return InvalidInput.EMPTY
                else if (!moneyValidationUseCase.invoke(cashPrice))
                    return InvalidInput.CASH

                if(emptyUseCase.invoke(visaPrice))
                    return InvalidInput.EMPTY
                else if (!moneyValidationUseCase.invoke(visaPrice))
                    return InvalidInput.VISA

                if (emptyUseCase.invoke(nextData))
                    return InvalidInput.EMPTY
            }
        }
        return InvalidInput.NONE
    }
}