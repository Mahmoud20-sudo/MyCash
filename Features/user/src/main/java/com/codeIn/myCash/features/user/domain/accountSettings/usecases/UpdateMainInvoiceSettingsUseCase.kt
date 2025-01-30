package com.codeIn.myCash.features.user.domain.accountSettings.usecases

import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.domain.accountSettings.repository.ValuesInvoiceSettingsRepository
import javax.inject.Inject

class UpdateMainInvoiceSettingsUseCase @Inject constructor(private val repository: ValuesInvoiceSettingsRepository){
    suspend operator fun invoke(commercialName : String? ,
                                commercialNumber : String? ,
                                taxRegistrationNumber : String? ,
                                tax : String?) : InvoiceSettingsState {
        return repository.setMainValuesInvoiceSettings(
            commercialName = commercialName , commercialNumber = commercialNumber ,
            taxRegistrationNumber = taxRegistrationNumber , tax = tax)
    }
}