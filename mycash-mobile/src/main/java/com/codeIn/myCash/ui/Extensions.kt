package com.codeIn.myCash.ui

import android.content.Context
import android.view.View
import com.codeIn.common.data.ReceiptStatusFilter
import com.codeIn.common.printer.pdf_from_view_manager.PdfFromViewHandler
import com.codeIn.myCash.BuildConfig
import com.codeIn.myCash.R
import com.plcoding.reports.data.enums.ReportsTypes

infix fun <T> T.isSame(item: T): Boolean = this == item


fun ReportsTypes.getReportTitle() = when (this) {
    ReportsTypes.SALES_REPORTS -> R.string.salesReports
    ReportsTypes.PURCHASING_REPORTS -> R.string.purchasingReports
    ReportsTypes.EXPENSE_REPORTS -> R.string.expenseReports
    ReportsTypes.TAX_REPORTS -> R.string.taxReports
    ReportsTypes.PRODUCT_INVENTORY_REPORTS -> R.string.inventory_reports
    else -> R.string.mainReports
}

fun PdfFromViewHandler.handleInvoiceAction(action: String, view: View) =
    setInvoiceActions(action, BuildConfig.APPLICATION_ID, view)

infix fun <T> String?.then(action: () -> T): T? {
    return if (this == "1")
        action.invoke()
    else null
}

infix fun <T> T?.elze(action: () -> T): T? {
    return if (this != "1")
        action.invoke()
    else this
}

fun ReceiptStatusFilter.getInvoiceFilterDisplayName(context: Context): String {
    return when (this) {
        ReceiptStatusFilter.PAYMENT_COMPLETED -> context.getString(R.string.payment_completed)
        ReceiptStatusFilter.PAYMENT_NOT_COMPLETED -> context.getString(R.string.payment_uncompleted)
        ReceiptStatusFilter.ALL -> context.getString(R.string.all)
    }
}