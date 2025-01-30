package com.plcoding.reports.domain.productsQuantitiesReport.useCases

import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.domain.productsQuantitiesReport.repository.GetProductsQuantityRepository
import javax.inject.Inject

class GetProductQuantitiesUseCase @Inject constructor(
    private val getProductsQuantityRepository: GetProductsQuantityRepository
) {

    suspend operator fun invoke(request: ReportRequest) =
        getProductsQuantityRepository.getProductsQuantityReport(request)
}