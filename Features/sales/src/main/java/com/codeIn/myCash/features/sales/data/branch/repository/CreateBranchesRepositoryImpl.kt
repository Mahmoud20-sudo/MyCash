package com.codeIn.myCash.features.sales.data.branch.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesResponse
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.data.branch.remote.ApiBranches
import com.codeIn.myCash.features.sales.domain.branch.repository.CreateBranchesRepository
import java.net.HttpURLConnection
import javax.inject.Inject

class CreateBranchesRepositoryImpl @Inject constructor(
    private val api: ApiBranches,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : CreateBranchesRepository {

    override suspend fun createBranches(
        name: String,
        address: String,
        isMain: Int,
        employee_id: Int,
        status: Int,
        city:String,
        additionalInfo: String,
        phone:String?
    ): BranchesState {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())
        return if (!token.isNullOrEmpty()) {
            val response = api.createBranches(
                lang = lang,
                authorization = token,
                name = name,
                address = address,
                isMain = isMain,
                employee_id = employee_id,
                status = status,
                city= city,
                additionalInfo = additionalInfo,
                phone = phone
            )
            if (response.isSuccessful && response.body()?.status == 1) {
                val data = response.body()
                data?.let {
                    BranchesState.SuccessCreateBranch(data)
                } ?: BranchesState.StateError(response?.message())
            } else {
                BranchesState.ServerError(errorHandler.invoke(response.code()))
            }
        } else {
            BranchesState.ServerError(errorHandler.invoke(HttpURLConnection.HTTP_SERVER_ERROR))
        }
    }
}


