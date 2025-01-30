package com.codeIn.myCash.features.stock.domain.product.model

import com.codeIn.common.data.ProductFilterType

data class ProductFilter(
    val date: String? = null,
    val type: ProductFilterType = ProductFilterType.NONE,
)