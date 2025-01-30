package com.plcoding.reports.data.salesReport.model.state

import com.codeIn.common.domain.ErrorEntity

sealed class SalesReportState<out T> {
    data object Loading : SalesReportState<Nothing>()
    data class Success<out T>(val data: T) : SalesReportState<T>()
    data class ServerError(val error: ErrorEntity) : SalesReportState<Nothing>()
    data object UnAuthorized : SalesReportState<Nothing>()
}