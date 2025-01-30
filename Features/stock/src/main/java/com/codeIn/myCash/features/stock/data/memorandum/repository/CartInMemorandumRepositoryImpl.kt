package com.codeIn.myCash.features.stock.data.memorandum.repository

import android.util.Log
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.repository.CartInMemorandumRepository
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.CalculateMemorandumInvoiceUseCase
import javax.inject.Inject

class CartInMemorandumRepositoryImpl @Inject constructor(
    private val useCase: CalculateMemorandumInvoiceUseCase
) :  CartInMemorandumRepository{
    override suspend fun addProductInCart(
        productModel: ProductModel?,
        products: ProductInInvoiceMemorandum?
    ): ProductInInvoiceMemorandum? {
        if (productModel != null )
            if (productModel.count > 0)
                products?.list?.add(productModel)
        useCase.invoke(products).let {
            return it
        }
    }

    override suspend fun updateProductFromCart(
        productModel: ProductModel?,
        products: ProductInInvoiceMemorandum?
    ): ProductInInvoiceMemorandum? {
        if (productModel != null )
        {
            val index = products?.list?.indexOfFirst { it.id == productModel.id}
            if (index != -1 )
                products?.list?.set(index!!, productModel)
        }
        useCase.invoke(products).let {
            return it
        }
    }

    override suspend fun deleteProductFromCart(
        productModel: ProductModel?,
        products: ProductInInvoiceMemorandum?
    ): ProductInInvoiceMemorandum? {
        if (productModel != null )
        {
            val index = products?.list?.indexOfFirst { it.id == productModel.id}
            if (index != -1 )
                products?.list?.removeAt(index!!)
        }
        useCase.invoke(products).let {
            return it
        }
    }

    override suspend fun getSelectedProducts(products: ProductInInvoiceMemorandum?): ProductInInvoiceMemorandum? {
        val data = ProductInInvoiceMemorandum()
        data.list = ArrayList()
        products?.list?.forEach {
            if (it.count > 0)
                data.list?.add(it)
        }
        return data
    }

    override suspend fun getSelectedProducts(
        oldProducts: ProductInInvoiceMemorandum?,
        newProducts: ProductInInvoiceMemorandum?
    ): ProductInInvoiceMemorandum? {
        val data = ProductInInvoiceMemorandum()
        data.list = ArrayList()
        oldProducts?.list?.forEach {
            if (it.count > 0)
                data.list?.add(it)
        }
        newProducts?.list?.forEach{product->
            val index = data.list?.indexOfFirst { it.id == product.id}
            if (index != -1 )
                data.list?.set(index!!, product)
            else
                data.list?.add(product)
        }
        useCase.invoke(data).let {
            return it
        }
    }
}