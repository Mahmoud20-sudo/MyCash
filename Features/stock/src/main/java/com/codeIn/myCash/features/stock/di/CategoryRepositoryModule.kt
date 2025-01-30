package com.codeIn.myCash.features.stock.di

import com.codeIn.myCash.features.stock.data.category.repository.CategoryRepositoryImpl
import com.codeIn.myCash.features.stock.domain.category.repository.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository

}