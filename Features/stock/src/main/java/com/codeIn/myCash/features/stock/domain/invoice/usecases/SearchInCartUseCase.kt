package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import javax.inject.Inject

class SearchInCartUseCase @Inject constructor() {
    suspend operator fun invoke(productModel: ProductModel, productsInCart: ProductsInCart?): Boolean{
        return productsInCart?.list?.contains(productModel)!!
    }
}