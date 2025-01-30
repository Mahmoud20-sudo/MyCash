package com.codeIn.myCash.features.user.data.complete_data.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.Validation
import com.codeIn.myCash.features.user.data.complete_data.model.CompleteInfoState
import com.codeIn.myCash.features.user.data.complete_data.remote.ApiCompleteData
import com.codeIn.myCash.features.user.domain.complete_data.repository.CompleteInfoRepository
import javax.inject.Inject

class CompleteInfoRepositoryImpl @Inject constructor(
    private val api: ApiCompleteData,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl,
    private val validation: Validation
) : CompleteInfoRepository {

    override suspend fun completeData(
        commercialRecord: String,
        commercialRecordName: String,
        tax: String,
        taxRecord: String,
        branchName: String,
        branchAddress: String
    ): CompleteInfoState = try {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())

        validation.validateUserData(
            commercialRecord = commercialRecord,
            commercialRecordName = commercialRecordName,
            tax = tax,
            taxRecord= taxRecord,
            branchName = branchName,
            branchAddress = branchAddress
        ).let {
            if (it != InvalidInput.NONE) return CompleteInfoState.InputError(it)
        }

        val response = api.completeData(
            authorization = token,
            lang = lang,
            commercialRecord = commercialRecord,
            commercialRecordName = commercialRecordName,
            taxRecord = taxRecord,
            tax = tax,
            branchName = branchName,
            branchAddress = branchAddress
        )

        if (response.isSuccessful && response.body()?.status == 1) {
            val data = response.body()
            data?.let {
                CompleteInfoState.SuccessCompleteData(data)
            } ?: CompleteInfoState.StateError(response?.message())
        } else {
            CompleteInfoState.ServerError(errorHandler.invoke(response.code()))
        }
    } catch (throwable: Throwable) {
        Log.e("CompleteInfoRepositoryImpl", "Error creating user: ${throwable.message}", throwable)
        CompleteInfoState.ServerError(errorHandler.getError(throwable))
    }
}