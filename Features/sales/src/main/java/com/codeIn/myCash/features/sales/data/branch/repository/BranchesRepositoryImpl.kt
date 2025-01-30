package com.codeIn.myCash.features.sales.data.branch.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesState
import com.codeIn.myCash.features.sales.data.branch.remote.ApiBranches
import com.codeIn.myCash.features.sales.domain.branch.repository.BranchesRepository
import java.net.HttpURLConnection
import javax.inject.Inject

class BranchesRepositoryImpl @Inject constructor(
    private val api: ApiBranches,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) :
    BranchesRepository {

    override suspend fun getBranches(searchText: String?): BranchesState {
        return try {

            val token = prefs.getValue(Constants.getToken())
            val response = api.getBranches(token, searchText)

            if (!token.isNullOrEmpty()) {
                if (response.isSuccessful && response.body()?.status == 1) {
                    val data = response.body()
                    data?.let {
                        BranchesState.SuccessRetrieveBranches(data)
                    } ?: BranchesState.StateError(response.message())
                } else {
                    BranchesState.ServerError(errorHandler.invoke(response.code()))
                }
            } else {
                BranchesState.ServerError(errorHandler.invoke(response.code()))
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            BranchesState.ServerError(errorHandler.getError(throwable))
        }
    }
}