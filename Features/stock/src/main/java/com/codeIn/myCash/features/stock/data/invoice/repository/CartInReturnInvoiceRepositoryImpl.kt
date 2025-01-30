package com.codeIn.myCash.features.stock.data.invoice.repository

import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.CartInReturnInvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CalculateReturnInvoiceUseCase
import javax.inject.Inject

class CartInReturnInvoiceRepositoryImpl @Inject constructor(
    private val useCase : CalculateReturnInvoiceUseCase
) : CartInReturnInvoiceRepository {

    override suspend fun deleteFromInvoice(
        productModel: ProductInInvoiceModel?,
        productsInReturnInvoice: ProductInReturnInvoice?
    ): ProductInReturnInvoice? {
        productsInReturnInvoice?.list?.remove(productModel)
        useCase.invoke(productsInReturnInvoice).let {
            return it
        }
    }

    override suspend fun updateInInvoice(
        productModel: ProductInInvoiceModel?,
        productsInReturnInvoice: ProductInReturnInvoice?
    ): ProductInReturnInvoice? {
        if ( productModel != null )
        {
            val index = productsInReturnInvoice?.list?.indexOfFirst { it.id == productModel.id}
            if (index != -1 )
                productsInReturnInvoice?.list?.set(index!!, productModel)
        }
        useCase.invoke(productsInReturnInvoice).let {
            return it
        }
    }

}