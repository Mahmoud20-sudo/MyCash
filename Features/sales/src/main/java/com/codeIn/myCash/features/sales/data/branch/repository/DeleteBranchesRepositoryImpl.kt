package com.codeIn.myCash.features.sales.data.branch.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.data.branch.model.response.DeleteDTO
import com.codeIn.myCash.features.sales.data.branch.remote.ApiBranches
import com.codeIn.myCash.features.sales.domain.branch.repository.DeleteBranchesRepository
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class DeleteBranchesRepositoryImpl @Inject constructor(
    private val api: ApiBranches,
    private val errorHandler: ErrorHandlerImpl,
    private val prefs : SharedPrefsModule
) : DeleteBranchesRepository {

    override suspend fun deleteBranches( all: Int?): BranchesState {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())
        return if (!token.isNullOrEmpty()) {
            val response = api.deleteBranches(lang, token,  all)
            if (response.isSuccessful && response.body()?.status == 1) {
                val data = response.body()
                data?.let {
                    BranchesState.SuccessDeleteAllBranches(data)
                } ?: BranchesState.StateError(response?.message())
            } else {
                BranchesState.ServerError(errorHandler.invoke(response.code()))
            }
        } else {
            BranchesState.ServerError(errorHandler.invoke(HttpURLConnection.HTTP_SERVER_ERROR))
        }
    }
}

