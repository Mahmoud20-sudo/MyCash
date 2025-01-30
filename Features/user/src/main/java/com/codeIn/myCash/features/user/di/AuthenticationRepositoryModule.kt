package com.codeIn.myCash.features.user.di

import com.codeIn.myCash.features.user.data.authentication.repository.CacheRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.CheckCodeDiscountRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.CheckCodeRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.ForgetPasswordRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.LoginRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.PaymentLinkRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.ProfileRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.RegisterRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.ResendCodeRepositoryImpl
import com.codeIn.myCash.features.user.data.authentication.repository.ResetPasswordRepositoryImpl
import com.codeIn.myCash.features.user.domain.authentication.repository.CacheRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.CheckCodeDiscountRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.CheckCodeRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.ForgetPasswordRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.LoginRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.PaymentLinkRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.ProfileRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.RegisterRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.ResendCodeRepository
import com.codeIn.myCash.features.user.domain.authentication.repository.ResetPasswordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindLoginRepository(repository: LoginRepositoryImpl): LoginRepository

    @Singleton
    @Binds
    abstract fun bindRegisterRepository(repository: RegisterRepositoryImpl): RegisterRepository

    @Singleton
    @Binds
    abstract fun bindCheckCodeRepository(repository: CheckCodeRepositoryImpl): CheckCodeRepository

    @Singleton
    @Binds
    abstract fun bindResendCodeRepository(repository: ResendCodeRepositoryImpl): ResendCodeRepository

    @Singleton
    @Binds
    abstract fun bindCheckCodeDiscountRepository(repository: CheckCodeDiscountRepositoryImpl): CheckCodeDiscountRepository

    @Singleton
    @Binds
    abstract fun bindForgetPasswordRepository(repository: ForgetPasswordRepositoryImpl): ForgetPasswordRepository

    @Singleton
    @Binds
    abstract fun bindResetPasswordRepository(repository: ResetPasswordRepositoryImpl): ResetPasswordRepository

    @Singleton
    @Binds
    abstract fun bindPaymentLinkRepository(repository: PaymentLinkRepositoryImpl): PaymentLinkRepository

    @Singleton
    @Binds
    abstract fun bindProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository

    @Singleton
    @Binds
    abstract fun bindCacheRepository(repository: CacheRepositoryImpl): CacheRepository

}