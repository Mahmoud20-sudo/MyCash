package com.codeIn.stock.inventory.data.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.stock.inventory.data.model.response.InventoryDTO
import com.codeIn.stock.inventory.data.model.response.InventoryResponse
import com.codeIn.stock.inventory.data.remote.ApiInventory
import com.codeIn.stock.inventory.domain.repository.GetInventoryRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class GetInventoryRepositoryImpl @Inject constructor(
    private val api: ApiInventory,
    private val prefs: SharedPrefsModule,
    private val handlerImpl: ErrorHandlerImpl
) : GetInventoryRepository {
    override suspend fun getInventory(type: Int): InventoryResponse {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())
        return if (!token.isNullOrEmpty()) {
            try {
                val response = api.getInventory(lang = lang, authorization = token, type = type)
                if (response.isSuccessful) {
                    val inventoryDTO = response.body()
                    if (inventoryDTO != null) {
                        InventoryResponse.Success(inventoryDTO)
                    } else {
                        InventoryResponse.ResponseError("Response body is null")
                    }
                } else {
                    val errorEntity = handlerImpl.getError(HttpException(response))
                    InventoryResponse.ServerError(errorEntity)
                }
            } catch (e: IOException) {
                InventoryResponse.ServerError(ErrorEntity.Network)
            } catch (e: Exception) {
                InventoryResponse.ResponseError(e.message)
            }
        } else {
            InventoryResponse.ResponseError("Token is null or empty")
        }
    }
}
