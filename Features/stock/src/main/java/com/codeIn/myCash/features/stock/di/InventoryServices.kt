package com.codeIn.myCash.features.stock.di

import com.codeIn.stock.inventory.data.remote.ApiInventory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InventoryServices {
    @Singleton
    @Provides
    fun provideInventory(retrofit : Retrofit): ApiInventory{
        return retrofit.create(ApiInventory::class.java)
    }
}