package com.codeIn.stock.inventory.domain.usecase

import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.domain.repository.DeleteInventoryRepository
import javax.inject.Inject

class DeleteInventoryUseCase @Inject constructor(private val repository: DeleteInventoryRepository){
    suspend operator fun invoke(inventoryID: Int): InventoryResponse{
        return repository.deleteInventory(inventoryID)
    }
}