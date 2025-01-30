package com.codeIn.myCash.features.user.data.authentication.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountState
import com.codeIn.myCash.features.user.domain.authentication.repository.CheckCodeDiscountRepository
import com.codeIn.myCash.features.user.domain.authentication.usecases.CheckCodeValidationUseCase
import javax.inject.Inject

class CheckCodeDiscountRepositoryImpl @Inject constructor (private var authentication: Authentication,
                                                           private val prefs : SharedPrefsModule,
                                                           private val errorHandler: ErrorHandlerImpl,
                                                           private val checkCodeValidationUseCase: CheckCodeValidationUseCase
) : CheckCodeDiscountRepository {
    override suspend fun checkCodeDiscount(discount: String?, countryId: String?): DiscountState {
        return try{

            val lang  = prefs.getValue(Constants.getLang())
//            val token  = prefs.getValue(Constants.registerToken())

            checkCodeValidationUseCase.invoke(discount).let {
                if (it != InvalidInput.NONE) return DiscountState.InputError(it)
            }
            authentication.checkCodeDiscount(
                lang= lang ,
                code= discount ,
                countryId = countryId ,
            ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        DiscountState.Success(response.body()?.data)
                    }
                    else {
                        DiscountState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    DiscountState.UnAuthorized
                }
                else{
                    DiscountState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        }catch (throwable : Throwable){
            DiscountState.ServerError(errorHandler.getError(throwable))
        }
    }

}