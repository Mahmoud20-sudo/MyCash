package com.codeIn.myCash.features.sales.di

import com.codeIn.myCash.features.sales.data.expenses.repository.ExpenseRepositoryImpl
import com.codeIn.myCash.features.sales.domain.expenses.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SalesRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindExpensesRepository(repository: ExpenseRepositoryImpl): ExpenseRepository


}