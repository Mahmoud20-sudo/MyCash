package com.codeIn.myCash.features.sales.data.branch.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesResponse
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.data.branch.remote.ApiBranches
import com.codeIn.myCash.features.sales.domain.branch.repository.UpdateBranchRepository
import java.net.HttpURLConnection
import javax.inject.Inject

class UpdateBranchRepositoryImpl @Inject constructor(
    private val api: ApiBranches,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : UpdateBranchRepository {
    override suspend fun updateBranch(
        name: String,
        isMain: Int,
        address: String,
        branchID: Int,
        status: Int,
        additionalInfo: String,
        city:String,
        phone:String?
    ): BranchesState {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())
        return if (!token.isNullOrEmpty()) {
            val response = api.updateBranch(
                lang = lang,
                authorization = token,
                name = name,
                isMain = isMain,
                address = address,
                branchID = branchID,
                status = status,
                additionalInfo =additionalInfo,
                city= city,
                phone = phone
            )
            if (response.isSuccessful && response.body()?.status == 1) {
                val data = response.body()
                data?.let {
                    BranchesState.SuccessUpdateBranch(data)
                } ?: BranchesState.StateError(response?.message())
            } else {
                BranchesState.ServerError(errorHandler.invoke(response.code()))
            }
        } else {
            BranchesState.ServerError(errorHandler.invoke(HttpURLConnection.HTTP_SERVER_ERROR))
        }
    }
}