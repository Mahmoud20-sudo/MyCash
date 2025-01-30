package com.plcoding.reports.data.tax.model

import com.codeIn.common.domain.ErrorEntity

sealed class TaxState{
    data object Idle : TaxState()
    data object Loading : TaxState()
    data class Success(val data : TaxReportData?) : TaxState()
    data class ServerError(val error : ErrorEntity) : TaxState()

    data class StateError(val message : String?) : TaxState()
    data object UnAuthorized : TaxState()
}