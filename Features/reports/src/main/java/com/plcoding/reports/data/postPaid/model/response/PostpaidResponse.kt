package com.plcoding.reports.data.postPaid.model.response

import com.codeIn.common.data.Pagination
import com.plcoding.reports.data.postPaid.model.beans.Postpaid

data class PostpaidResponse(
    val status: Int,
    val message: String,
    val data: PostpaidBody,
){
    inner class PostpaidBody(
        val data: List<Postpaid>,
        val pagination: Pagination,
    )
}