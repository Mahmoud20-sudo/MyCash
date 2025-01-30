package com.codeIn.stock.inventory.data.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.data.remote.ApiInventory
import com.codeIn.stock.inventory.domain.repository.CreateInventoryRepository
import javax.inject.Inject

class CreateInventoryRepositoryImpl @Inject constructor(
    private val api: ApiInventory,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : CreateInventoryRepository {
    override suspend fun createInventory(
        productID: Int,
        allQuantity: Int,
        quantity: Int,
        damageQuantity: Int
    ): InventoryResponse {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response = api.createInventory(
                lang = lang,
                authorization = token,
                productID = productID,
                allQuantity = allQuantity,
                quantity = quantity,
                damageQuantity = damageQuantity
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