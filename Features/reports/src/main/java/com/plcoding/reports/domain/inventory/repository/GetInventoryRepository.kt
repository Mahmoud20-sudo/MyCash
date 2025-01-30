package com.plcoding.reports.domain.inventory.repository

import com.plcoding.reports.data.inventory.model.InventoryState

interface GetInventoryRepository {
    suspend fun getReports(
        type: Int,
        page: Int,
        productId: Int? = null
    ): InventoryState
}