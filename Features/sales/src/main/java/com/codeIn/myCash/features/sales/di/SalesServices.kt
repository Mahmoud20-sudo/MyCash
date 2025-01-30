package com.codeIn.myCash.features.sales.di

import com.codeIn.myCash.features.sales.data.clients.remote.Client
import com.codeIn.myCash.features.sales.data.expenses.remote.Expenses
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SalesServices {

    @Singleton
    @Provides
    fun provideExpenses(retrofit: Retrofit) : Expenses{
        return retrofit.create(Expenses::class.java)
    }
    @Singleton
    @Provides
    fun provideClient(retrofit: Retrofit) : Client {
        return retrofit.create(Client::class.java)
    }
}