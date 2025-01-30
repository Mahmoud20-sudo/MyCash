package com.codeIn.myCash.features.stock.data.invoice.repository

import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartRepository
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CalculateInvoiceUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CalculatePurchaseInvoiceUseCase
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val useCase : CalculateInvoiceUseCase,
    private val purchaseInvoiceUseCase : CalculatePurchaseInvoiceUseCase,
) : CartRepository {
    override suspend fun addInCart(
        productModel: ProductModel,
        productsInCart: ProductsInCart?
    ): ProductsInCart? {
        productsInCart?.list?.add(productModel)
        productsInCart?.count = productsInCart?.count!! + 1
        if (productsInCart.mainTypeInvoice == MainTypeInvoice.PURCHASE)
            purchaseInvoiceUseCase.invoke(productsInCart).let {
                return  it
            }
        else
            useCase.invoke(productsInCart).let {
                return it
            }

    }

    override suspend fun deleteFromCart(
        productModel: ProductModel,
        productsInCart: ProductsInCart?
    ): ProductsInCart? {
        productsInCart?.list?.remove(productModel)
        productsInCart?.count = productsInCart?.count!! - 1
        if (productsInCart.mainTypeInvoice == MainTypeInvoice.PURCHASE)
            purchaseInvoiceUseCase.invoke(productsInCart).let {
                return  it
            }
        else
            useCase.invoke(productsInCart).let {
                return it
            }
    }

    override suspend fun updateInCart(
        productModel: ProductModel,
        productsInCart: ProductsInCart?
    ): ProductsInCart? {
        productsInCart?.list?.contains(productModel).let {
            val index = productsInCart?.list?.indexOf(productModel)
            productsInCart?.list?.set(index!!, productModel)
        }
        productsInCart?.count = productModel.count!!
        if (productsInCart?.mainTypeInvoice == MainTypeInvoice.PURCHASE)
            purchaseInvoiceUseCase.invoke(productsInCart).let {
                return  it
            }
        else
            useCase.invoke(productsInCart).let {
                return it
            }
    }

    override suspend fun deleteAllFromCart(
        productModel: ProductModel,
        productsInCart: ProductsInCart?
    ): ProductsInCart? {
        productsInCart?.list = ArrayList()
        productsInCart?.count = 0
        productsInCart?.discount = "0.0"
        productsInCart?.totalAfterDiscount = "0.0"
        productsInCart?.initialTotal = "0.0"
        productsInCart?.tax= "0.0"
        productsInCart?.finalTotal= "0.0"
        return productsInCart
    }

    override suspend fun updateProductsDependOnCart(
        products: ArrayList<ProductModel>?,
        productsInCart: ProductsInCart?
    ): List<ProductModel>? {
        if (productsInCart?.list?.isNotEmpty() == true){
           productsInCart.list?.forEach {product->
               val index = products?.indexOfFirst { it.id == product.id}
               if (index != -1 )
                   products?.set(index!!, product)
           }
        }
        return products
    }

    override suspend fun initialCart(productsInCart: ProductsInCart?): ProductsInCart? {
        if (productsInCart?.mainTypeInvoice == MainTypeInvoice.PURCHASE)
            purchaseInvoiceUseCase.invoke(productsInCart).let {
                return  it
            }
        else
            useCase.invoke(productsInCart).let {
                return it
            }
    }
}