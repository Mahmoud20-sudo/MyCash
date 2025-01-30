package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.repository.CartInMemorandumRepository
import javax.inject.Inject

class GetSelectedCommonProductsInMemorandumUseCase @Inject constructor(
    private val cartInMemorandumRepository: CartInMemorandumRepository
){
    suspend operator fun invoke(oldProducts : ProductInInvoiceMemorandum? , newProducts : ProductInInvoiceMemorandum?) : ProductInInvoiceMemorandum? {
        return cartInMemorandumRepository.getSelectedProducts( oldProducts , newProducts)
    }
}