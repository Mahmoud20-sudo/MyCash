package com.codeIn.stock.inventory.domain.repository

import com.codeIn.stock.inventory.data.model.response.InventoryResponse

interface DeleteInventoryRepository {
    suspend fun deleteInventory(inventoryID: Int):InventoryResponse
}