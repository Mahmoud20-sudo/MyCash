package com.plcoding.reports.data.inventory.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.plcoding.reports.data.inventory.model.InventoryState
import com.plcoding.reports.data.inventory.remote.ApiInventoryReports
import com.plcoding.reports.domain.inventory.repository.GetInventoryRepository
import javax.inject.Inject

class GetInventoryRepositoryImpl @Inject constructor(
    private val inventoryApi: ApiInventoryReports,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : GetInventoryRepository {

    override suspend fun getReports(type: Int, page: Int, productId: Int?): InventoryState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            inventoryApi.getInventory(
                lang = lang,
                authorization = token,
                type = type,
                page = page,
                productId = productId
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        InventoryState.SuccessShowReports(response.body()?.data)
                    } else {
                        InventoryState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401) {
                    InventoryState.UnAuthorized
                } else {
                    InventoryState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            InventoryState.ServerError(errorHandler.getError(throwable))
        }
    }
}