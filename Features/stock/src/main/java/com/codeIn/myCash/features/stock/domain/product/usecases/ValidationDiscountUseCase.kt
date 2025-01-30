package com.codeIn.myCash.features.stock.domain.product.usecases

import android.util.Log
import com.codeIn.common.data.Discount
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.domain.usecases.MoneyValidationUseCase
import com.codeIn.common.domain.usecases.ValidateEmptyUseCase
import javax.inject.Inject

class ValidationDiscountUseCase @Inject constructor(
    private val moneyValidationUseCase: MoneyValidationUseCase,
    private val valueValidationUseCase: ValueValidationUseCase ,
    private val percentageValidationUseCase: PercentageValidationUseCase,
    private val emptyUseCase: ValidateEmptyUseCase
) {
    operator fun invoke(discountType : Int , discount : String?, finalPrice : String?) : InvalidInput{
        if (discountType == Discount.Value.value)
        {
            return if (!moneyValidationUseCase.invoke(discount)){
                Log.d("TAG" , "Discount 2")
                InvalidInput.EMPTY
            }

            else if (!valueValidationUseCase.invoke(discount , finalPrice))
            {
                Log.d("TAG" , "Discount 3")
                InvalidInput.DISCOUNT_VALUE
            }
            else {
                Log.d("TAG" , "Discount 1")
                InvalidInput.NONE
            }
        }
        else if (discountType == Discount.Percentage.value)
        {
            return if (emptyUseCase.invoke(discount))
                InvalidInput.EMPTY
            else if (!percentageValidationUseCase.invoke(discount ))
                InvalidInput.DISCOUNT_PERCENTAGE
            else
                InvalidInput.NONE
        }

        return InvalidInput.NONE
    }
}