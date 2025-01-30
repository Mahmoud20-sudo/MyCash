package com.codeIn.stock.inventory.domain.repository

import com.codeIn.stock.inventory.data.model.response.InventoryDTO
import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import retrofit2.Response

interface GetInventoryRepository {
   suspend fun getInventory(type: Int): InventoryResponse
}