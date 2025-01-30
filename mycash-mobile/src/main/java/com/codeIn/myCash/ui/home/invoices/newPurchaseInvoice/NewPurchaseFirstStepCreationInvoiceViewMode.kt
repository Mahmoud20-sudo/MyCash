package com.codeIn.myCash.ui.home.invoices.newPurchaseInvoice

import androidx.lifecycle.SavedStateHandle
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.ui.home.products.first_step_create_invoice.FirstStepCreationInvoiceViewModel
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CartFactoryUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.MakePurchaseInvoiceValidationUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.ValidationDiscountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NewPurchaseFirstStepCreationInvoiceViewMode @Inject constructor(state: SavedStateHandle,
                                                                      prefs : SharedPrefsModule,
                                                                      private val cartFactoryUseCase: CartFactoryUseCase,
                                                                      validationDiscountUseCase: ValidationDiscountUseCase ,
    validationUseCase: MakePurchaseInvoiceValidationUseCase

) : FirstStepCreationInvoiceViewModel(
    state ,  prefs  , cartFactoryUseCase , validationDiscountUseCase , validationUseCase
) {


}