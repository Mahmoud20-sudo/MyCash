package com.codeIn.myCash.features.user.di

import com.codeIn.myCash.features.user.data.complete_data.repository.CompleteInfoRepositoryImpl
import com.codeIn.myCash.features.user.data.complete_data.repository.LogoutRepositoryImpl
import com.codeIn.myCash.features.user.domain.complete_data.repository.CompleteInfoRepository
import com.codeIn.myCash.features.user.domain.complete_data.repository.LogoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CompleteDataModule {

    @Singleton
    @Binds
    abstract fun bindCompleteInfoRepository(repository: CompleteInfoRepositoryImpl): CompleteInfoRepository

    @Singleton
    @Binds
    abstract fun bindLogoutRepository(repository: LogoutRepositoryImpl): LogoutRepository

}