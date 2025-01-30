package com.codeIn.myCash.features.user.data.accountSettings.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.accountSettings.remote.AccountSettings
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.domain.accountSettings.repository.ValuesInvoiceSettingsRepository
import com.codeIn.myCash.features.user.domain.authentication.usecases.ValidateNotEmptyUseCase
import javax.inject.Inject

class ValuesInvoiceSettingsRepositoryImpl  @Inject constructor (private var accountSettings: AccountSettings,
                                                                private val prefs : SharedPrefsModule,
                                                                private val errorHandler: ErrorHandlerImpl,
                                                                private val validateEmptyUseCase: ValidateNotEmptyUseCase
): ValuesInvoiceSettingsRepository {
    override suspend fun getValues(): InvoiceSettingsState {
        return try{

            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            accountSettings.invoiceSettings(lang , token).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        InvoiceSettingsState.InvoiceSettingsSuccess(response.body()?.data)
                    }
                    else {
                        InvoiceSettingsState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceSettingsState.UnAuthorized
                }
                else{
                    InvoiceSettingsState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        }catch (throwable : Throwable){
            InvoiceSettingsState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun setValues(
        productDesc: String?, footerText: String?,
        client: String?, cashier: String?,
        type: String?, myCashQr: String?,
        zatcaQr: String?, active: String?
    ): InvoiceSettingsState {
        return try{

            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

//            validateEmptyUseCase.invoke(footerText).let {
//                if (!it) return InvoiceSettingsState.InputError(InvalidInput.EMPTY)
//            }

            accountSettings.updateInvoiceSettingValue(
                lang = lang , authorization = token , productDesc = productDesc ,
                footerText = footerText , client = client , cashier = cashier ,
                type = type , myCashQr= myCashQr , zatcaQr = zatcaQr , active = active
            ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        InvoiceSettingsState.InvoiceTypeSuccess(response.body()?.message)
                    }
                    else {
                        InvoiceSettingsState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceSettingsState.UnAuthorized
                }
                else{
                    InvoiceSettingsState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            InvoiceSettingsState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun setMainValuesInvoiceSettings(
        commercialName: String?,
        commercialNumber: String?,
        taxRegistrationNumber: String?,
        tax: String?
    ): InvoiceSettingsState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            accountSettings.updateInvoiceSettings(
                lang = lang , authorization = token ,
                name = commercialName ,
                taxRecord = taxRegistrationNumber,
                commercialRecord = commercialNumber ,
                tax = tax
            ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        InvoiceSettingsState.InvoiceSettingsSuccess(response.body()?.data)
                    }
                    else {
                        InvoiceSettingsState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceSettingsState.UnAuthorized
                }
                else{
                    InvoiceSettingsState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            InvoiceSettingsState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun startSaleInvoiceOrderNo(orderNo: String?): InvoiceSettingsState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            validateEmptyUseCase.invoke(orderNo).let {
                if (!it) return InvoiceSettingsState.InputError(InvalidInput.EMPTY)
            }

            accountSettings.startSaleInvoiceOrderNo(
                lang = lang , authorization = token , currentInvoiceOrder = orderNo
            ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        InvoiceSettingsState.OrderNoInvoiceSuccess(response.body()?.message)
                    }
                    else {
                        InvoiceSettingsState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceSettingsState.UnAuthorized
                }
                else{
                    InvoiceSettingsState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            InvoiceSettingsState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun startPurchaseInvoiceOrderNo(orderNo: String?): InvoiceSettingsState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            validateEmptyUseCase.invoke(orderNo).let {
                if (!it) return InvoiceSettingsState.InputError(InvalidInput.EMPTY)
            }

            accountSettings.startPurchaseInvoiceOrderNo(
                lang = lang , authorization = token , currentBuyInvoiceOrder = orderNo
            ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        InvoiceSettingsState.OrderNoInvoiceSuccess(response.body()?.message)
                    }
                    else {
                        InvoiceSettingsState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceSettingsState.UnAuthorized
                }
                else{
                    InvoiceSettingsState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable : Throwable){
            InvoiceSettingsState.ServerError(errorHandler.getError(throwable))
        }
    }

}