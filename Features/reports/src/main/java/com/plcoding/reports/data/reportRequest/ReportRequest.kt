package com.plcoding.reports.data.reportRequest

import com.plcoding.reports.data.enums.PerChunk

data class ReportRequest(
    val typeId: Int = -1,
    val branchPosition: Int = 0,
    val branchId: Int,
    val page: Int? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val invoiceNum: String? = null,
    val receiptNum: String? = null,
    val receiptStatusId: Int? = null,
    val productStatusId: Int? = null,
    val interval: PerChunk? = null,
    val categoryId: Int? = null,
    val comparisonOptionId: Int? = null,
    val quantities: Int? = null,
)
