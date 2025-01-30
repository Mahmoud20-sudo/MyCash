package com.codeIn.myCash.features.user.di

import com.codeIn.myCash.features.user.data.edit_profile.repository.EditCodeRepositoryImpl
import com.codeIn.myCash.features.user.data.edit_profile.repository.MyInfoRepositoryImpl
import com.codeIn.myCash.features.user.data.edit_profile.repository.UpdateEmailRepositoryImpl
import com.codeIn.myCash.features.user.data.edit_profile.repository.UpdateProfileRepositoryImpl
import com.codeIn.myCash.features.user.data.edit_profile.repository.UpdatePhoneRepositoryImpl
import com.codeIn.myCash.features.user.domain.edit_profile.repository.EditCodeRepository
import com.codeIn.myCash.features.user.domain.edit_profile.repository.MyInfoRepository
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdateEmailRepository
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdatePhoneRepository
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdateProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Singleton
    @Binds
    abstract fun bindUpdatePhoneRepository(repository: UpdatePhoneRepositoryImpl): UpdatePhoneRepository

    @Singleton
    @Binds
    abstract fun bindUpdateEmailRepository(repository: UpdateEmailRepositoryImpl): UpdateEmailRepository

    @Singleton
    @Binds
    abstract fun bindEditCodeRepository(repository: EditCodeRepositoryImpl): EditCodeRepository

    @Singleton
    @Binds
    abstract fun bindUpdateProfileRepository(repository: UpdateProfileRepositoryImpl): UpdateProfileRepository

    @Singleton
    @Binds
    abstract fun bindMyInfoRepository(repository: MyInfoRepositoryImpl): MyInfoRepository
}