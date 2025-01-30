package com.codeIn.myCash.features.stock.di

import com.codeIn.myCash.features.stock.data.madaTransactions.repository.MadaTransactionsRepositoryImpl
import com.codeIn.myCash.features.stock.domain.madaTransactions.repository.MadaTransactionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TransactionRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTransactionRepository(repository: MadaTransactionsRepositoryImpl): MadaTransactionsRepository

}