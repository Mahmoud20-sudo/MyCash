package com.plcoding.reports.data.productsQuantitiesReport.model.response

import com.codeIn.common.data.Pagination
import com.plcoding.reports.data.productsQuantitiesReport.model.beans.ProductsQuantity

data class ProductsQuantityResponse(
    val status: Int,
    val message: String,
    val data: ProductQuantityData,
) {
    inner class ProductQuantityData(
        val data: List<ProductsQuantity>,
        val pagination: Pagination,
    )
}