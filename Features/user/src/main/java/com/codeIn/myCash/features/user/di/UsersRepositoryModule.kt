package com.codeIn.myCash.features.user.di

import com.codeIn.myCash.features.user.data.users.repository.CreateUserRepositoryImpl
import com.codeIn.myCash.features.user.data.users.repository.DeleteAllEmployeeRepositoryImpl
import com.codeIn.myCash.features.user.data.users.repository.DeleteUserRepositoryImpl
import com.codeIn.myCash.features.user.data.users.repository.RefreshUserRepositoryImpl
import com.codeIn.myCash.features.user.data.users.repository.UpdateUsersRepositoryImpl
import com.codeIn.myCash.features.user.data.users.repository.UserDetailsRepositoryImpl
import com.codeIn.myCash.features.user.data.users.repository.UserRepositoryImpl
import com.codeIn.myCash.features.user.domain.users.repository.CreateUsersRepository
import com.codeIn.myCash.features.user.domain.users.repository.DeleteAllEmployeeRepository
import com.codeIn.myCash.features.user.domain.users.repository.DeleteUserRepository
import com.codeIn.myCash.features.user.domain.users.repository.RefreshRepository
import com.codeIn.myCash.features.user.domain.users.repository.UpdateUsersRepository
import com.codeIn.myCash.features.user.domain.users.repository.UserDetailsRepository
import com.codeIn.myCash.features.user.domain.users.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UsersRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUsersRepository(repository: UserRepositoryImpl):UsersRepository

    @Singleton
    @Binds
    abstract fun bindCreateUsersRepository(repository: CreateUserRepositoryImpl):CreateUsersRepository

    @Singleton
    @Binds
    abstract fun bindUsersDetailsRepository(repository: UserDetailsRepositoryImpl):UserDetailsRepository

    @Singleton
    @Binds
    abstract fun bindUpdateRepository(repository : UpdateUsersRepositoryImpl): UpdateUsersRepository

    @Singleton
    @Binds
    abstract fun bindDeleteRepository(repository : DeleteUserRepositoryImpl): DeleteUserRepository

    @Singleton
    @Binds
    abstract fun bindDeleteAllRepository(repository : DeleteAllEmployeeRepositoryImpl): DeleteAllEmployeeRepository

    @Singleton
    @Binds
    abstract fun bindRefreshRepository(repository : RefreshUserRepositoryImpl): RefreshRepository
}