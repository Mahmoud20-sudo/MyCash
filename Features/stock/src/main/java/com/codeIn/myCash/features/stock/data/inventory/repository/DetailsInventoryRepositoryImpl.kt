package com.codeIn.stock.inventory.data.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.data.remote.ApiInventory
import com.codeIn.stock.inventory.domain.repository.InventoryDetailsRepository
import javax.inject.Inject

class DetailsInventoryRepositoryImpl @Inject constructor(
    private val api: ApiInventory,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
):InventoryDetailsRepository{
    override suspend fun inventoryDetails(inventoryId: Int): InventoryResponse {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response = api.getSingleInventory(
                lang = lang,
                authorization = token,
                inventoryId = inventoryId
            )

            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    InventoryResponse.Success(data)
                } else {
                    InventoryResponse.ResponseError(response.body()?.message.toString())
                }
            } else {
                InventoryResponse.ServerError(errorHandler.invoke(response.code()))
            }
        } catch (throwable: Throwable) {
            InventoryResponse.ServerError(errorHandler.getError(throwable))
        }
    }
}