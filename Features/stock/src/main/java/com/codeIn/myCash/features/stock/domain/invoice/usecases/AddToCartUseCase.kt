package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val cartRepository : CartRepository) {
    suspend operator fun invoke(productModel: ProductModel, productsInCart: ProductsInCart?) : ProductsInCart?{
        return cartRepository.addInCart(productModel , productsInCart)
    }
}