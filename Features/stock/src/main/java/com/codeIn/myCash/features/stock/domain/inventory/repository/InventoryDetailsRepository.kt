package com.codeIn.stock.inventory.domain.repository

import com.codeIn.stock.inventory.data.model.response.InventoryResponse

interface InventoryDetailsRepository {
    suspend fun inventoryDetails(inventoryId: Int):InventoryResponse
}