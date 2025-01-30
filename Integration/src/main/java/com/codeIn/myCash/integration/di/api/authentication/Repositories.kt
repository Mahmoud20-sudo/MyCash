package com.codeIn.myCash.integration.di.api.authentication

import com.codeIn.myCash.integration.domain.authentication.login.ForgotPasswordRepository
import com.codeIn.myCash.integration.domain.authentication.login.LogInRepository
import com.codeIn.myCash.integration.domain.authentication.login.ResetPasswordRepository
import com.codeIn.myCash.integration.domain.authentication.signUp.DevicesRepository
import com.codeIn.myCash.integration.domain.authentication.signUp.OtpRepository
import com.codeIn.myCash.integration.domain.authentication.signUp.PackagesRepository
import com.codeIn.myCash.integration.domain.authentication.signUp.RegisterRepository
import com.codeIn.myCash.integration.model.repository.authentication.login.ForgotPasswordRepositoryImpl
import com.codeIn.myCash.integration.model.repository.authentication.login.LogInRepositoryImpl
import com.codeIn.myCash.integration.model.repository.authentication.signUp.AuthGeneralDataRepository
import com.codeIn.myCash.integration.model.repository.authentication.signUp.OtpRepositoryImpl
import com.codeIn.myCash.integration.model.repository.authentication.signUp.RegisterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Singleton
    @Binds
    abstract fun bindPackagesRepository(repository: AuthGeneralDataRepository): PackagesRepository

    @Singleton
    @Binds
    abstract fun bindDevicesRepository(repository: AuthGeneralDataRepository): DevicesRepository

    @Singleton
    @Binds
    abstract fun bindRegisterRepository(repository: RegisterRepositoryImpl): RegisterRepository

    @Singleton
    @Binds
    abstract fun bindOtpRepository(repository: OtpRepositoryImpl): OtpRepository

    @Singleton
    @Binds
    abstract fun bindForgotPasswordRepository(repository: ForgotPasswordRepositoryImpl): ForgotPasswordRepository

    @Singleton
    @Binds
    abstract fun bindResetPasswordRepository(repository: ForgotPasswordRepositoryImpl): ResetPasswordRepository

    @Singleton
    @Binds
    abstract fun bindLogInRepository(repository: LogInRepositoryImpl): LogInRepository
}
