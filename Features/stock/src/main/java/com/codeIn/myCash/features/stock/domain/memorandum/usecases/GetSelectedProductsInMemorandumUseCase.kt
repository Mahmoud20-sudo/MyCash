package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.repository.CartInMemorandumRepository
import javax.inject.Inject

class GetSelectedProductsInMemorandumUseCase @Inject constructor(
    private val cartInMemorandumRepository: CartInMemorandumRepository
){
    suspend operator fun invoke(products : ProductInInvoiceMemorandum?) : ProductInInvoiceMemorandum? {
        return cartInMemorandumRepository.getSelectedProducts( products)
    }
}