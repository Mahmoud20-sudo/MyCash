package com.codeIn.myCash.features.stock.domain.madaTransactions.usecases

import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionState
import com.codeIn.myCash.features.stock.domain.madaTransactions.repository.MadaTransactionsRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(private val madaTransactionsRepository: MadaTransactionsRepository){

    suspend operator fun invoke(): TransactionState {
        return madaTransactionsRepository.getTransactions()
    }
}