package com.codeIn.myCash.features.sales.di

import com.codeIn.myCash.features.sales.data.branch.repository.DeleteBranchByIDRepositoryImpl
import com.codeIn.myCash.features.sales.domain.branch.repository.DeleteBranchesByIDRepository
import com.codeIn.myCash.features.sales.data.branch.repository.BranchDetailsRepositoryImpl
import com.codeIn.myCash.features.sales.data.branch.repository.BranchesRepositoryImpl
import com.codeIn.myCash.features.sales.data.branch.repository.CreateBranchesRepositoryImpl
import com.codeIn.myCash.features.sales.data.branch.repository.DeleteBranchesRepositoryImpl
import com.codeIn.myCash.features.sales.data.branch.repository.UpdateBranchRepositoryImpl
import com.codeIn.myCash.features.sales.domain.branch.repository.BranchDetailsRepository
import com.codeIn.myCash.features.sales.domain.branch.repository.BranchesRepository
import com.codeIn.myCash.features.sales.domain.branch.repository.CreateBranchesRepository
import com.codeIn.myCash.features.sales.domain.branch.repository.DeleteBranchesRepository
import com.codeIn.myCash.features.sales.domain.branch.repository.UpdateBranchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BranchesRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindBranchesRepository(repositoryImpl: BranchesRepositoryImpl) : BranchesRepository

    @Singleton
    @Binds
    abstract fun bindCreateBranchesRepository(repositoryImpl: CreateBranchesRepositoryImpl) : CreateBranchesRepository

    @Singleton
    @Binds
    abstract fun bindDeleteBranchesRepository(repositoryImpl: DeleteBranchesRepositoryImpl): DeleteBranchesRepository

    @Singleton
    @Binds
    abstract fun bindDeleteBranchesIDRepository(repositoryImpl: DeleteBranchByIDRepositoryImpl): DeleteBranchesByIDRepository

    @Singleton
    @Binds
    abstract fun bindUpdateBranchRepository(repositoryImpl: UpdateBranchRepositoryImpl): UpdateBranchRepository

    @Singleton
    @Binds
    abstract fun bindBranchDetailsRepository(repositoryImpl: BranchDetailsRepositoryImpl) : BranchDetailsRepository
}