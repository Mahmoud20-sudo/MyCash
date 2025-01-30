package com.codeIn.myCash.features.sales.di

import com.codeIn.myCash.features.sales.data.clients.repository.ClientsRepositoryImpl
import com.codeIn.myCash.features.sales.domain.clients.repository.ClientsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ClientRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindClientRepository(repository: ClientsRepositoryImpl): ClientsRepository

}