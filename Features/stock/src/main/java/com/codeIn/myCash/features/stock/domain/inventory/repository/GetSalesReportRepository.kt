package com.codeIn.stock.inventory.domain.repository

import com.codeIn.stock.inventory.data.model.response.SalesReportDTO
import retrofit2.Response

interface GetSalesReportRepository {
    suspend fun getSalesReport(): Response<SalesReportDTO>
}