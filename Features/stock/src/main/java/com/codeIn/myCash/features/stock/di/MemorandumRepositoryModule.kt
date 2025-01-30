package com.codeIn.myCash.features.stock.di

import com.codeIn.myCash.features.stock.data.memorandum.repository.CartInMemorandumRepositoryImpl
import com.codeIn.myCash.features.stock.data.memorandum.repository.MemorandumCalculatorRepositoryImpl
import com.codeIn.myCash.features.stock.data.memorandum.repository.MemorandumRepositoryImpl
import com.codeIn.myCash.features.stock.domain.memorandum.repository.CartInMemorandumRepository
import com.codeIn.myCash.features.stock.domain.memorandum.repository.MemorandumCalculatorRepository
import com.codeIn.myCash.features.stock.domain.memorandum.repository.MemorandumRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MemorandumRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMemorandumRepository(repository: MemorandumRepositoryImpl): MemorandumRepository

    @Singleton
    @Binds
    abstract fun bindMemorandumCalculatorRepository(repository: MemorandumCalculatorRepositoryImpl): MemorandumCalculatorRepository

    @Singleton
    @Binds
    abstract fun bindCartInMemorandumRepository(repository: CartInMemorandumRepositoryImpl): CartInMemorandumRepository

}