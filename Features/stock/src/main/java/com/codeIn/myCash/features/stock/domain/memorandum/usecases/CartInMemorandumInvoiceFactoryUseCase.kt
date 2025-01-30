package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.common.data.Cart
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import javax.inject.Inject

class CartInMemorandumInvoiceFactoryUseCase @Inject constructor(
    private val deleteFromCartInMemorandumUseCase: DeleteFromCartInMemorandumUseCase,
    private val updateInCartInMemorandumUseCase: UpdateInCartInMemorandumUseCase,
    private val addInCartInMemorandumUseCase: AddInCartInMemorandumUseCase ,
    private val calculateMemorandumInvoiceUseCase: CalculateMemorandumInvoiceUseCase ,
    private val searchInCartInMemorandumInvoiceUseCase: SearchInCartInMemorandumInvoiceUseCase
){
    suspend operator fun invoke(productModel: ProductModel?, products : ProductInInvoiceMemorandum?, cart: Cart) : ProductInInvoiceMemorandum? {
        return if (cart == Cart.ADD && searchInCartInMemorandumInvoiceUseCase.invoke(productModel , products)){
            updateInCartInMemorandumUseCase.invoke(productModel , products)
        } else if (cart == Cart.ADD && !searchInCartInMemorandumInvoiceUseCase.invoke(productModel , products)){
            addInCartInMemorandumUseCase.invoke(productModel , products)
        } else if (cart == Cart.DELETE){
            deleteFromCartInMemorandumUseCase.invoke(productModel, products)
        } else if (cart == Cart.INITIAL){
            calculateMemorandumInvoiceUseCase.invoke(products)
        }else
            null
    }
}