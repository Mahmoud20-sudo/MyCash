package com.codeIn.stock.inventory.domain.repository

import com.codeIn.stock.inventory.data.model.response.InventoryResponse

interface UpdateInventoryRepository {
    suspend fun updateInventory(inventoryID:Int,allQuantity :Int,quantity:Int,damageQuantity:Int,productID :Int): InventoryResponse
}