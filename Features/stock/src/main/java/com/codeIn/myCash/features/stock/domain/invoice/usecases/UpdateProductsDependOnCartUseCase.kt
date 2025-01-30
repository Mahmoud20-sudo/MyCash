package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartRepository
import javax.inject.Inject

class UpdateProductsDependOnCartUseCase @Inject constructor(private val cartRepository : CartRepository){

    suspend operator fun invoke(products: ArrayList<ProductModel>?, productsInCart: ProductsInCart?): List<ProductModel>?{
        return cartRepository.updateProductsDependOnCart(products, productsInCart)
    }
}