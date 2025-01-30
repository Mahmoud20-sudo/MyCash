package com.codeIn.myCash.features.user.domain.authentication.usecases

import com.codeIn.common.util.GeneralResponse
import com.codeIn.myCash.features.user.domain.authentication.repository.CacheRepository
import javax.inject.Inject

class SaveCacheUseCase @Inject constructor(private val repository: CacheRepository){
    suspend operator fun invoke(phone : String?, password : String?) : GeneralResponse {
        return repository.saveCache(phone  , password)
    }
}