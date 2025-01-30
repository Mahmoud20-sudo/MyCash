package com.codeIn.myCash.features.stock.domain.product.usecases

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.domain.product.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(private val productRepository: ProductRepository){

    suspend operator fun invoke(productId : String?): ProductsState {
        return productRepository.deleteProduct(productId)
    }
}