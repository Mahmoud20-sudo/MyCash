package com.codeIn.myCash.features.user.domain.accountSettings.usecases

import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.domain.accountSettings.repository.ValuesInvoiceSettingsRepository
import javax.inject.Inject

class GetInvoiceSettingsUseCase @Inject constructor(private val repository: ValuesInvoiceSettingsRepository){
    suspend operator fun invoke() : InvoiceSettingsState {
        return repository.getValues()
    }
}