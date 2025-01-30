package com.codeIn.myCash.features.stock.domain.product.repository

import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import java.io.File


interface ProductRepository {

    suspend fun createProduct(
        name : String? , barcode : String? , description : String? , price : String? ,
        quantity : String? , discountType : Int , discount : String? , taxType: Int, taxable: Int  ,
        category : String? , hasDiscount : Int , parentCategoryId : String? , categoryId : String? , buyPrice : String? ,
        imageFile : File? , buyTaxAvailable : Int , buyTaxType : Int, branch: String?
    ) : CategoryState
    suspend fun updateProduct(
        productId : String? ,
        name : String? , barcode : String? , description : String? , price : String? ,
        quantity : String? , discountType : Int , discount : String? , taxType: Int, taxable: Int  ,
        category : String? , hasDiscount : Int , parentCategoryId : String? , categoryId : String? , buyPrice : String? ,
        imageFile : File? , buyTaxAvailable : Int , buyTaxType : Int, branch: String?
    ) : CategoryState

    suspend fun getProducts(
        sort : String? , searchText : String? , categoryId: String? ,
        parentCategoryId: String? , discountType : String? , date : String?
    ): ProductsState


    suspend fun deleteProduct(productId : String?): ProductsState


    suspend fun getSingleProduct(productId : String?): ProductsState
}