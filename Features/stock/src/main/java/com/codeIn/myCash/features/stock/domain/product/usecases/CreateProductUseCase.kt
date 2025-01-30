package com.codeIn.myCash.features.stock.domain.product.usecases

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.domain.product.repository.ProductRepository
import java.io.File
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(private val productRepository: ProductRepository){

    suspend operator fun invoke(
        name : String? , barcode : String? , description : String? , price : String? ,
        quantity : String? , discountType : Int , discount : String? , taxType: Int, taxable: Int  ,
        category : String? , hasDiscount : Int , parentCategoryId : String? , categoryId : String? ,  imageFile : File?,
        buyPrice : String? , buyTaxAvailable : Int , buyTaxType : Int, branch: String?
    ): CategoryState {
        return productRepository.createProduct(
            name, barcode, description, price, quantity, discountType,
            discount, taxType, taxable, category, hasDiscount, parentCategoryId,
            categoryId, buyPrice , imageFile , buyTaxAvailable , buyTaxType, branch
        )
    }
}