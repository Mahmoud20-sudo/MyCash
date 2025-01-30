package com.codeIn.myCash.features.stock.domain.invoice.usecases

import com.codeIn.common.data.Cart
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import javax.inject.Inject

class CartFactoryUseCase @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val searchInCartUseCase: SearchInCartUseCase,
    private val deleteFromCartUseCase: DeleteFromCartUseCase,
    private val deleteAllCartUseCase: DeleteAllCartUseCase,
    private val updateInCartUseCase: UpdateInCartUseCase,
    private val initialCartUseCase: InitialCartUseCase
){
    suspend operator fun invoke(productModel: ProductModel?, productsInCart: ProductsInCart?, cart: Cart) : ProductsInCart? {
        return if (cart == Cart.ADD && searchInCartUseCase.invoke(productModel!!, productsInCart)){
            updateInCartUseCase.invoke(productModel , productsInCart)
        } else if (cart == Cart.ADD && !searchInCartUseCase.invoke(productModel!! , productsInCart)){
            addToCartUseCase.invoke(productModel , productsInCart)
        } else if (cart == Cart.DELETE){
            deleteFromCartUseCase.invoke(productModel!!, productsInCart)
        } else if (cart == Cart.DELETE_ALL){
            deleteAllCartUseCase.invoke(productModel!!, productsInCart)
        } else if (cart == Cart.INITIAL){
            initialCartUseCase.invoke(productsInCart)
        }else
            null
    }
}