package com.codeIn.myCash.features.stock.di

import com.codeIn.stock.inventory.data.repository.CreateInventoryRepositoryImpl
import com.codeIn.stock.inventory.data.repository.DeleteInventoryRepositoryImpl
import com.codeIn.stock.inventory.data.repository.DetailsInventoryRepositoryImpl
import com.codeIn.stock.inventory.data.repository.GetInventoryRepositoryImpl
import com.codeIn.stock.inventory.data.repository.UpdateInventoryRepositoryImpl
import com.codeIn.stock.inventory.domain.repository.CreateInventoryRepository
import com.codeIn.stock.inventory.domain.repository.DeleteInventoryRepository
import com.codeIn.stock.inventory.domain.repository.GetInventoryRepository
import com.codeIn.stock.inventory.domain.repository.InventoryDetailsRepository
import com.codeIn.stock.inventory.domain.repository.UpdateInventoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InventoryRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindInventoryRepository(repositoryImpl: GetInventoryRepositoryImpl): GetInventoryRepository
    @Singleton
    @Binds
    abstract fun bindInventorySingleRepository(repositoryImpl: DetailsInventoryRepositoryImpl): InventoryDetailsRepository
    @Singleton
    @Binds
    abstract fun bindDeleteInventoryRepository(repositoryImpl: DeleteInventoryRepositoryImpl): DeleteInventoryRepository
    @Singleton
    @Binds
    abstract fun bindUpdateInventoryRepository(repositoryImpl: UpdateInventoryRepositoryImpl): UpdateInventoryRepository
    @Singleton
    @Binds
    abstract fun bindCreateInventoryRepository(repositoryImpl: CreateInventoryRepositoryImpl): CreateInventoryRepository
}