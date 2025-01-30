package com.plcoding.reports.data.salesReport.model.response

import com.codeIn.common.base.BaseResponse
import com.plcoding.reports.data.salesReport.model.beans.SalesReport

data class SalesReportResponse(val data: List<SalesReport>) : BaseResponse()
