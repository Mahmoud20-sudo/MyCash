package com.codeIn.myCash.features.stock.domain.invoice.repository

import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart

interface CartRepository {

    suspend fun addInCart(productModel: ProductModel , productsInCart: ProductsInCart?): ProductsInCart?
    suspend fun deleteFromCart(productModel: ProductModel , productsInCart: ProductsInCart?): ProductsInCart?
    suspend fun updateInCart(productModel: ProductModel , productsInCart: ProductsInCart?): ProductsInCart?
    suspend fun deleteAllFromCart(productModel: ProductModel , productsInCart: ProductsInCart?): ProductsInCart?
    suspend fun updateProductsDependOnCart(products: ArrayList<ProductModel>?, productsInCart: ProductsInCart?): List<ProductModel>?
    suspend fun initialCart( productsInCart: ProductsInCart?):ProductsInCart?
}