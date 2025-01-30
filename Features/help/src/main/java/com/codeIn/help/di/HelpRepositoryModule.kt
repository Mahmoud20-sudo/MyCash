package com.codeIn.help.di

import com.codeIn.help.data.repository.HelpRepositoryImpl
import com.codeIn.help.data.repository.HowWorkRepositoryImpl
import com.codeIn.help.domain.repository.HelpRepository
import com.codeIn.help.domain.repository.HowWorkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HelpRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindHelpRepository(repositoryImpl: HelpRepositoryImpl):HelpRepository

    @Singleton
    @Binds
    abstract fun bindHowWorkRepository(repositoryImpl: HowWorkRepositoryImpl):HowWorkRepository

}