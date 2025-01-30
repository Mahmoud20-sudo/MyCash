package com.codeIn.stock.inventory.domain.usecase

import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.domain.repository.InventoryDetailsRepository
import javax.inject.Inject

class DetailsInventoryUseCase @Inject constructor(private val repository: InventoryDetailsRepository) {
    suspend operator fun invoke(inventoryId: Int): InventoryResponse{
        return repository.inventoryDetails(inventoryId)
    }
}