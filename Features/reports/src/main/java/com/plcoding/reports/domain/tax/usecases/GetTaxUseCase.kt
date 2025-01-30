package com.plcoding.reports.domain.tax.usecases

import com.plcoding.reports.data.enums.DateType
import com.plcoding.reports.data.tax.model.TaxState
import com.plcoding.reports.domain.tax.repository.GetTaxRepository
import javax.inject.Inject

class GetTaxUseCase @Inject constructor(private val getTaxRepository: GetTaxRepository) {
    suspend operator fun invoke(
        page: Int?, dateFrom: String?, dateTo: String?, dateType: DateType
    ): TaxState = getTaxRepository.getReports(
        dateFrom = dateFrom,
        dateTo = dateTo,
        page = page,
        dateType = dateType
    )
}