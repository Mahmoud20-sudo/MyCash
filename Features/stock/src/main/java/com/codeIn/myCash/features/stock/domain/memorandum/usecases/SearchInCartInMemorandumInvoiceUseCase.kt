package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import javax.inject.Inject

class SearchInCartInMemorandumInvoiceUseCase @Inject constructor(){
    suspend operator fun invoke(productModel: ProductModel?, products : ProductInInvoiceMemorandum?): Boolean{
        return if (productModel != null )
            products?.list?.contains(productModel)!!
        else
            false
    }
}