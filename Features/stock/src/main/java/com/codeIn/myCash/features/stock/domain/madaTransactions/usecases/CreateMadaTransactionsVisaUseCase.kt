package com.codeIn.myCash.features.stock.domain.madaTransactions.usecases

import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionState
import com.codeIn.myCash.features.stock.domain.madaTransactions.repository.MadaTransactionsRepository
import javax.inject.Inject

class CreateMadaTransactionsVisaUseCase @Inject constructor(private val madaTransactionsRepository: MadaTransactionsRepository){

    suspend operator fun invoke(
        amount : String?,
        runRefund : String?,
        codeRefund : String?,
        dateRefund : String?,
        type : String?,
    ): TransactionState {
        return madaTransactionsRepository.createTransaction(
            amount , runRefund , codeRefund , dateRefund , type)
    }
}