package com.codeIn.myCash.features.stock.domain.product.usecases

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.domain.product.repository.ProductRepository
import java.io.File
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(private val productRepository: ProductRepository){

    suspend operator fun invoke(
        productId : String? , name : String? , barcode : String? , description : String? , price : String? ,
        quantity : String? , discountType : Int , discount : String? , taxType: Int, taxable: Int  ,
        category : String? , hasDiscount : Int , parentCategoryId : String? , categoryId : String? ,  imageFile : File?,
        buyPrice : String? , buyTaxAvailable : Int , buyTaxType : Int, branch: String?
    ): CategoryState {
        return productRepository.updateProduct(
            productId ,
            name, barcode, description, price, quantity, discountType, discount, taxType, taxable,
            category, hasDiscount, parentCategoryId, categoryId, buyPrice ,  imageFile , buyTaxAvailable , buyTaxType, branch
        )
    }
}