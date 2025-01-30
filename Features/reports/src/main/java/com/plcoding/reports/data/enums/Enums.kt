package com.plcoding.reports.data.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.plcoding.reports.R

enum class DateType(val value: Int) {
    NONE(0),
    DAY(1),
    WEEK(2),
    MONTH(3),
    YEAR(4);

    companion object {
        val dateTypeList
            get() = DateType.entries.toTypedArray().filter { it.value != DateType.NONE.value }
                .toList()
    }
}

enum class SaleBuyType(val value: Int) {
    NONE(0),
    SALE(1),
    BUY(2)
}

enum class InvoiceType(val value: Int) {
    NONE(0),
    SIMPLE(1),
    TAX(2);

    companion object {
        val invoiceTypeList
            get() = InvoiceType.entries.reversed().toTypedArray()
                .filter { it.value != NONE.value }.toList()

    }
}

enum class ReportsTypes {
    PRODUCT_INVENTORY_REPORTS,
    MAIN_REPORTS,
    SALES_REPORTS,
    PURCHASING_REPORTS,
    EXPENSE_REPORTS,
    TAX_REPORTS;

    companion object {
        /**
        note: this will returns a list does not contains of the purchasing reports and tax reports for now
         * */
        val reportTypeList
            get() = ReportsTypes.entries.toTypedArray().toList().filter {
                it != PURCHASING_REPORTS && it != TAX_REPORTS && it != EXPENSE_REPORTS
            }
    }
}

enum class ReportsFilterTypes(
    val id: Int,
    val position: Int,
    @StringRes val filterName: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    SALES_REPORTS(
        id = 1,
        position = 0,
        filterName = R.string.sales_reports,
        selectedIcon = R.drawable.selected_sales_reports,
        unselectedIcon = R.drawable.unselected_sales_reports,
    ),
    SALES_INVOICES(
        id = 2,
        position = 1,
        filterName = R.string.sales_invoices,
        selectedIcon = R.drawable.selected_sales_invoices,
        unselectedIcon = R.drawable.unselected_sales_invoices
    ),
    RETURN_INVOICES(
        id = 3,
        position = 2,
        filterName = R.string.return_invoices,
        selectedIcon = R.drawable.selected_return_invoices,
        unselectedIcon = R.drawable.unselected_return_invoices
    ),
    PRODUCTS_QUANTITY(
        id = 4,
        position = 3,
        filterName = R.string.products_inventory,
        selectedIcon = R.drawable.selected_percentage_circle,
        unselectedIcon = R.drawable.unselected_percentage_circle
    ),
    POSTPAID(
        id = 5,
        position = 4,
        filterName = R.string.report_postpaid,
        selectedIcon = R.drawable.selected_percentage_circle,
        unselectedIcon = R.drawable.unselected_percentage_circle
    ),


}

enum class TypeOfDate {
    FROM, TO
}

enum class PerChunk(
    val id: Int,
    @StringRes val chunkName: Int
) {
    DAILY(id = 1, chunkName = R.string.daily),
    WEEKLY(id = 2, chunkName = R.string.weekly),
    MONTHLY(id = 3, chunkName = R.string.monthly),
    YEARLY(id = 4, chunkName = R.string.yearly)
}