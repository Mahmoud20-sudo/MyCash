package com.codeIn.myCash.features.stock.domain.memorandum.usecases

import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.repository.MemorandumRepository
import javax.inject.Inject

class GetMemorandumsUseCase @Inject constructor(private val repository : MemorandumRepository){
    suspend operator fun invoke(
        invoiceId : String? ,
        type: String? ,
        limit : String?
    ): MemorandumState {
        return repository.getMemorandums(invoiceId, type , limit)
    }
}