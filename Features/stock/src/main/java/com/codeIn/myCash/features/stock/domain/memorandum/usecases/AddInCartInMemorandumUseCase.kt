package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.repository.CartInMemorandumRepository
import javax.inject.Inject

class AddInCartInMemorandumUseCase @Inject constructor(
    private val cartInMemorandumRepository: CartInMemorandumRepository
){
    suspend operator fun invoke(productModel: ProductModel?, products : ProductInInvoiceMemorandum?) : ProductInInvoiceMemorandum? {
        return cartInMemorandumRepository.addProductInCart(productModel , products)
    }
}