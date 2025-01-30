package com.codeIn.myCash.features.stock.di

import com.codeIn.myCash.features.sales.data.clients.repository.ClientsRepositoryImpl
import com.codeIn.myCash.features.stock.data.product.repository.ProductRepositoryImpl
import com.codeIn.myCash.features.sales.domain.clients.repository.ClientsRepository
import com.codeIn.myCash.features.stock.domain.product.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindProductRepository(repository: ProductRepositoryImpl): ProductRepository
}