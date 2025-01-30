package com.codeIn.stock.inventory.domain.usecase

import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.domain.repository.UpdateInventoryRepository
import javax.inject.Inject

class UpdateInventoryUseCase @Inject constructor(private val repository: UpdateInventoryRepository){
    suspend operator fun invoke(inventoryID:Int,allQuantity :Int,quantity:Int,damageQuantity:Int,productID :Int) :InventoryResponse{
        return repository.updateInventory(inventoryID, allQuantity, quantity, damageQuantity, productID)
    }
}