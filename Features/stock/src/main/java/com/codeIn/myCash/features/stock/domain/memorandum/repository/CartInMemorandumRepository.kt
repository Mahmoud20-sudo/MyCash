package com.codeIn.myCash.features.stock.domain.memorandum.repository

import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum

interface CartInMemorandumRepository {

    suspend fun addProductInCart(productModel: ProductModel?, products : ProductInInvoiceMemorandum?): ProductInInvoiceMemorandum?

    suspend fun updateProductFromCart(productModel: ProductModel? , products : ProductInInvoiceMemorandum?): ProductInInvoiceMemorandum?

    suspend fun deleteProductFromCart(productModel: ProductModel? , products : ProductInInvoiceMemorandum?): ProductInInvoiceMemorandum?

    suspend fun getSelectedProducts(products : ProductInInvoiceMemorandum?) : ProductInInvoiceMemorandum?

    suspend fun getSelectedProducts(oldProducts : ProductInInvoiceMemorandum? , newProducts : ProductInInvoiceMemorandum?) : ProductInInvoiceMemorandum?

}