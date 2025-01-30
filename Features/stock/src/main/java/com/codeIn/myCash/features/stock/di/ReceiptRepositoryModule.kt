package com.codeIn.myCash.features.stock.di

import com.codeIn.myCash.features.stock.data.receipt.repository.GetReceiptsRepositoryImpl
import com.codeIn.myCash.features.stock.data.receipt.repository.PayReceiptRepositoryImpl
import com.codeIn.myCash.features.stock.domain.receipt.repository.GetReceiptsRepository
import com.codeIn.myCash.features.stock.domain.receipt.repository.PayReceiptRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReceiptRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindGetReceiptsRepository(repository: GetReceiptsRepositoryImpl): GetReceiptsRepository
    @Singleton
    @Binds
    abstract fun bindPayReceiptRepository(repository: PayReceiptRepositoryImpl): PayReceiptRepository

}