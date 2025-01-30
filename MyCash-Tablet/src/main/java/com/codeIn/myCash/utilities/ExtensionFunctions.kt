package com.codeIn.myCash.utilities

import android.content.Context
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.myCash.R
import com.plcoding.reports.data.enums.ReportsTypes


fun String.addSar(context: Context): String {
    return "$this ${context.getString(R.string.sar)}"
}

fun ErrorEntity.getErrorMessage(context: Context): String {
    return when (this) {
        ErrorEntity.AccessDenied -> context.getString(R.string.access_denied_403)
        ErrorEntity.Network -> context.getString(R.string.no_internet_connection)
        ErrorEntity.NotFound -> context.getString(R.string.not_found_404)
        ErrorEntity.ServerInternalError -> context.getString(R.string.server_internal_error_500)
        ErrorEntity.ServiceUnavailable -> context.getString(R.string.service_unavailable_503)
        ErrorEntity.Unknown -> context.getString(R.string.unknown_error)
    }
}

fun ReportsTypes.getReportTitle() = when (this) {
    ReportsTypes.SALES_REPORTS -> R.string.salesReports
    ReportsTypes.PURCHASING_REPORTS -> R.string.purchasingReports
    ReportsTypes.EXPENSE_REPORTS -> R.string.expenseReports
    ReportsTypes.TAX_REPORTS -> R.string.taxReports
    ReportsTypes.PRODUCT_INVENTORY_REPORTS -> R.string.inventory_reports
    else -> R.string.mainReports
}