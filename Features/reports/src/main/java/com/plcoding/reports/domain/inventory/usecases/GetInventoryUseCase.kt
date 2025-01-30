package com.plcoding.reports.domain.inventory.usecases

import com.plcoding.reports.data.expense.model.ExpenseState
import com.plcoding.reports.data.inventory.model.InventoryState
import com.plcoding.reports.domain.inventory.repository.GetInventoryRepository
import javax.inject.Inject

class GetInventoryUseCase @Inject constructor(private val getInventoryRepository: GetInventoryRepository) {
    suspend operator fun invoke(
        type: Int,
        page: Int,
        productId: Int? = null
    ): InventoryState =
        getInventoryRepository.getReports(productId = productId, type = type, page = page)
}