package com.codeIn.myCash.features.user.domain.complete_data.repository

import com.codeIn.myCash.features.user.data.complete_data.model.CompleteInfoState
import java.io.File

interface LogoutRepository {

    suspend fun logout(): CompleteInfoState

}