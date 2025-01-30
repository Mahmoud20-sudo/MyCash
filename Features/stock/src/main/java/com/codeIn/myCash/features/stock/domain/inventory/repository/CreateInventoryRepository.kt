package com.codeIn.stock.inventory.domain.repository

import com.codeIn.stock.inventory.data.model.response.InventoryResponse

interface CreateInventoryRepository {
    suspend fun createInventory(productID :Int,allQuantity :Int,quantity:Int,damageQuantity:Int):InventoryResponse
}