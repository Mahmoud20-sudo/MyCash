package com.codeIn.myCash.features.user.domain.accountSettings.usecases

import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.domain.accountSettings.repository.ValuesInvoiceSettingsRepository
import javax.inject.Inject

class UpdateInvoiceSettingsUseCase @Inject constructor(private val repository: ValuesInvoiceSettingsRepository){
    suspend operator fun invoke( productDesc : String?, footerText : String?,
                                 client : String?, cashier : String?,
                                 type : String?,//1=>simple , 2=>tax
                                 myCashQr : String?, zatcaQr : String?,
                                 active : String?) : InvoiceSettingsState {
        return repository.setValues(
            productDesc, footerText , client , cashier, type,//1=>simple , 2=>tax
            myCashQr, zatcaQr, active)
    }
}