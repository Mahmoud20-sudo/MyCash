package com.codeIn.stock.inventory.data.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.data.remote.ApiInventory
import com.codeIn.stock.inventory.domain.repository.UpdateInventoryRepository
import javax.inject.Inject

class UpdateInventoryRepositoryImpl @Inject constructor(
    private val api: ApiInventory,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
):UpdateInventoryRepository{
    override suspend fun updateInventory(
        inventoryID: Int,
        allQuantity: Int,
        quantity: Int,
        damageQuantity: Int,
        productID: Int
    ): InventoryResponse {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response = api.updateInventory(
                lang = lang,
                authorization = token,
                inventoryID =inventoryID,
                allQuantity = allQuantity,
                quantity = quantity,
                damageQuantity = damageQuantity,
                productID = productID,
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