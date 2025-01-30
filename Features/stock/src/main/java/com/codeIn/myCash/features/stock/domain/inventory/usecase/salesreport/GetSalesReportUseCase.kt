package com.codeIn.stock.inventory.domain.usecase.salesreport

import com.codeIn.stock.inventory.data.model.response.SalesReportDTO
import com.codeIn.stock.inventory.domain.repository.GetSalesReportRepository
import retrofit2.Response
import javax.inject.Inject

class GetSalesReportUseCase @Inject constructor(private val repository : GetSalesReportRepository){
    suspend operator fun invoke():Response<SalesReportDTO>{
        return repository.getSalesReport()
    }
}