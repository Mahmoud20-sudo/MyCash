package com.codeIn.myCash.features.user.di

import com.codeIn.myCash.features.user.data.shift.repository.ShiftRepositoryImpl
import com.codeIn.myCash.features.user.domain.shift.repository.ShiftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ShiftModule {

    @Singleton
    @Binds
    abstract fun bindShiftRepository(repository: ShiftRepositoryImpl): ShiftRepository

}