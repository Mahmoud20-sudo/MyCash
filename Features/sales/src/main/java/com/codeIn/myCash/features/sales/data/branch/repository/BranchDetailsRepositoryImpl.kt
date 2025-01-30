package com.codeIn.myCash.features.sales.data.branch.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.data.branch.remote.ApiBranches
import com.codeIn.myCash.features.sales.domain.branch.repository.BranchDetailsRepository
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class BranchDetailsRepositoryImpl @Inject constructor(
    private val api: ApiBranches,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : BranchDetailsRepository {
    override suspend fun getBranchDetails(branchID: Int): BranchesState {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())
        return if (!token.isNullOrEmpty()) {
            val response = api.getBranchDetails(
                lang = lang,
                authorization = token,
                branchID = branchID
            )
            if (response.isSuccessful && response.body()?.status == 1) {
                val data = response.body()
                data?.let {
                    BranchesState.SuccessRetrieveSingleBranch(data)
                } ?: BranchesState.StateError(response?.message())
            } else {
                BranchesState.ServerError(errorHandler.invoke(response.code()))
            }
        } else {
            BranchesState.ServerError(errorHandler.invoke(HttpURLConnection.HTTP_SERVER_ERROR))
        }
    }
}