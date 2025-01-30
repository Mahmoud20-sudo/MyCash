package com.plcoding.reports.data.returnReport.model.response

import com.codeIn.common.data.Pagination
import com.google.gson.annotations.SerializedName
import com.plcoding.reports.data.returnReport.model.beans.ReturnInvoice

data class ReturnInvoicesReportResponse(
    val status: Int,
    val message: String,
    val data: ReturnInvoicesReportBody,
) {
    data class ReturnInvoicesReportBody(
        @SerializedName("total_returned_amount")
        val totalReturnedAmount: Double,
        val invoices: Invoices,
    ){
        data class Invoices(
            val data: List<ReturnInvoice>,
            val pagination: Pagination,
        )
    }

}
