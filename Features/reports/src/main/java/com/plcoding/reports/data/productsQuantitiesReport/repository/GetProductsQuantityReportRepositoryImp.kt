package com.plcoding.reports.data.productsQuantitiesReport.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.plcoding.reports.data.productsQuantitiesReport.remote.ProductsQuantityApiService
import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.SaleInvoices
import com.plcoding.reports.domain.productsQuantitiesReport.repository.GetProductsQuantityRepository
import javax.inject.Inject

class GetProductsQuantityReportRepositoryImp @Inject constructor(
    private val api: ProductsQuantityApiService,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
): GetProductsQuantityRepository {

     override suspend fun getProductsQuantityReport(request: ReportRequest): SaleInvoices {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            api.getProductsQuantityReport(
                lang = lang,
                token = token,
                page = request.page,
                productStatusId = request.productStatusId,
                quantities = request.quantities,
                comparisonOption = request.comparisonOptionId,
                branchId = request.branchId
            ).let { response ->

                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        SaleInvoices.SuccessProductsQuantity(response.body())
                    } else {
                        SaleInvoices.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    SaleInvoices.UnAuthorized
                } else {
                    SaleInvoices.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            SaleInvoices.ServerError(errorHandler.getError(throwable))
        }
    }
}