package com.codeIn.myCash.features.user.domain.complete_data.usecase

import com.codeIn.myCash.features.user.data.complete_data.model.CompleteInfoState
import com.codeIn.myCash.features.user.domain.complete_data.repository.CompleteInfoRepository
import java.io.File
import javax.inject.Inject

class CompleteInfoUseCase @Inject constructor(private val repository: CompleteInfoRepository) {
    suspend operator fun invoke(
        commercialRecord: String,
        commercialRecordName: String,
        tax: String,
        taxRecord: String,
        branchName: String,
        branchAddress: String
    ): CompleteInfoState = repository.completeData(
        commercialRecord = commercialRecord,
        commercialRecordName = commercialRecordName,
        tax = tax,
        taxRecord = taxRecord,
        branchName = branchName,
        branchAddress = branchAddress
    )
}