package com.codeIn.myCash.features.stock.domain.inventory.usecase

import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.domain.repository.CreateInventoryRepository
import javax.inject.Inject

class CreateInventoryUseCase @Inject constructor(private val repository : CreateInventoryRepository){
    suspend operator fun invoke(productID :Int,allQuantity :Int,quantity:Int,damageQuantity:Int) : InventoryResponse{
        return repository.createInventory(productID, allQuantity, quantity, damageQuantity)
    }
}