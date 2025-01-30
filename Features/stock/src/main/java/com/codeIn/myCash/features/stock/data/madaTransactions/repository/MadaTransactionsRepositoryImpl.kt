package com.codeIn.myCash.features.stock.data.madaTransactions.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.MadaTransactions
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionState
import com.codeIn.myCash.features.stock.domain.madaTransactions.repository.MadaTransactionsRepository
import javax.inject.Inject

class MadaTransactionsRepositoryImpl @Inject constructor(
    private val mada: MadaTransactions,
    private val prefs : SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl,
    ) : MadaTransactionsRepository {
    override suspend fun createTransaction(
        amount: String?,
        runRefund: String?,
        codeRefund: String?,
        dateRefund: String?,
        type: String?
    ): TransactionState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            mada.createTransactions(
                lang = lang ,
                authorization = token ,
                amount= amount ,
                runRefund= runRefund ,
                codeRefund =  codeRefund ,
                dateRefund = dateRefund,
                type = type
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        TransactionState.Sucess(response.body()?.message)
                    } else {
                        TransactionState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    TransactionState.UnAuthorized
                } else {
                    TransactionState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable: Throwable) {
            TransactionState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getTransactions(): TransactionState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            mada.getTransactions(
                lang = lang ,
                authorization = token ,
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        TransactionState.SuccessShowTransactions(response.body()?.data)
                    } else {
                        TransactionState.StateError(response.body()?.message.toString())
                    }
                }else if (response.code() == 401){
                    TransactionState.UnAuthorized
                } else {
                    TransactionState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }catch (throwable: Throwable) {
            TransactionState.ServerError(errorHandler.getError(throwable))
        }
    }
}