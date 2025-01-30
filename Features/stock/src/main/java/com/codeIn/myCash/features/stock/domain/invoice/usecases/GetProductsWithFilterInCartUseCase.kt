package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.product.usecases.GetProductsUseCase
import javax.inject.Inject

class GetProductsWithFilterInCartUseCase @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
){

    suspend operator fun invoke(
        sort : String?, searchText : String?, categoryId: String?, parentCategoryId: String?,
        selectedProductInCart : ProductsInCart? ,  discountType : String? , date : String?
    ) : ProductsState{
        return getProductsUseCase.invoke(sort, searchText, categoryId, parentCategoryId , discountType , date).let {response->
            when (response){
                is  ProductsState.SuccessShowProducts ->{
                    if (selectedProductInCart?.list?.isNotEmpty() == true)
                    {
                        for (i in selectedProductInCart.list!!)
                            response.data?.data?.indexOfFirst {it.id == i.id }.let {
                                if (it != null) {
                                    if (it >= 0) {
                                        response.data?.data?.get(it)?.count = i.count
                                        response.data?.data?.get(it)?.discountInInvoiceModel = i.discountInInvoiceModel
                                    }
                                }
                            }
                    }
                    return ProductsState.SuccessShowProducts(response.data)
                }
                else ->
                    return response
            }
        }
    }
}