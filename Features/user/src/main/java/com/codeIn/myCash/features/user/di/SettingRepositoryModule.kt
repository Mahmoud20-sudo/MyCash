package com.codeIn.myCash.features.user.di

import com.codeIn.myCash.features.user.data.settings.repository.DevicesRepositoryImpl
import com.codeIn.myCash.features.user.data.settings.repository.PackagesRepositoryImpl
import com.codeIn.myCash.features.user.data.settings.repository.SetSubscriptionInfoRepositoryImpl
import com.codeIn.myCash.features.user.data.settings.repository.SettingsRepositoryImpl
import com.codeIn.myCash.features.user.data.settings.repository.UpdateSubscriptionInfoRepositoryImpl
import com.codeIn.myCash.features.user.domain.settings.repository.DevicesRepository
import com.codeIn.myCash.features.user.domain.settings.repository.PackagesRepository
import com.codeIn.myCash.features.user.domain.settings.repository.SetSubscriptionInfoRepository
import com.codeIn.myCash.features.user.domain.settings.repository.SettingsRepository
import com.codeIn.myCash.features.user.domain.settings.repository.UpdateSubscriptionInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindDevicesRepository(repository: DevicesRepositoryImpl): DevicesRepository

    @Singleton
    @Binds
    abstract fun bindPackagesRepository(repository: PackagesRepositoryImpl): PackagesRepository

    @Singleton
    @Binds
    abstract fun bindTermsConditionsRepository(repository: SettingsRepositoryImpl): SettingsRepository

    @Singleton
    @Binds
    abstract fun bindSetSubscriptionInfoRepository(repository: SetSubscriptionInfoRepositoryImpl): SetSubscriptionInfoRepository

    @Singleton
    @Binds
    abstract fun bindUpdateSubscriptionInfoRepository(repository: UpdateSubscriptionInfoRepositoryImpl): UpdateSubscriptionInfoRepository

}