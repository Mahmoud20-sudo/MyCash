package com.codeIn.myCash.features.user.domain.authentication.repository

import com.codeIn.common.util.GeneralResponse

interface CacheRepository {
    suspend fun saveCache( phone : String? , password : String?) : GeneralResponse
}