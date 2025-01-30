package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.repository.MemorandumRepository
import javax.inject.Inject

class GetSingleMemorandumUseCase @Inject constructor(private val repository : MemorandumRepository){
    suspend operator fun invoke(
        memorandumId : String?,
    ): MemorandumState {
        return repository.getSingleMemorandum(memorandumId)
    }
}