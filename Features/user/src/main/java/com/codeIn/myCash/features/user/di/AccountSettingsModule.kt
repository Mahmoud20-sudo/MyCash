package com.codeIn.myCash.features.user.di

import com.codeIn.myCash.features.user.data.accountSettings.repository.ValuesInvoiceSettingsRepositoryImpl
import com.codeIn.myCash.features.user.domain.accountSettings.repository.ValuesInvoiceSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountSettingsModule {

    @Singleton
    @Binds
    abstract fun bindValuesInvoiceSettingsRepository(repository: ValuesInvoiceSettingsRepositoryImpl): ValuesInvoiceSettingsRepository

}