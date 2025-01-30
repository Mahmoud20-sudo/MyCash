package com.codeIn.stock.inventory.domain.usecase

import com.codeIn.stock.inventory.data.model.response.InventoryDTO
import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.domain.repository.GetInventoryRepository
import retrofit2.Response
import javax.inject.Inject

class GetInventoryUseCase @Inject constructor(private val repository: GetInventoryRepository) {
    suspend operator fun invoke(type: Int): InventoryResponse {
        return repository.getInventory(type)
    }
}