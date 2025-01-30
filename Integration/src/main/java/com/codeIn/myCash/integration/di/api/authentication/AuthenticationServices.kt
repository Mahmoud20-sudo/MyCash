package com.codeIn.myCash.integration.di.api.authentication

import com.codeIn.myCash.integration.model.network.NetworkParams
import com.codeIn.myCash.integration.model.network.authentication.login.ForgotPasswordApiServices
import com.codeIn.myCash.integration.model.network.authentication.login.SignInApiServices
import com.codeIn.myCash.integration.model.network.authentication.signUp.AuthGeneralDataApiServices
import com.codeIn.myCash.integration.model.network.authentication.signUp.OtpApiServices
import com.codeIn.myCash.integration.model.network.authentication.signUp.RegisterApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthenticationServices {

    @Singleton
    @Provides
    fun provideAuthGeneralDataApiServices(@Named(NetworkParams.NAME) retrofit: Retrofit): AuthGeneralDataApiServices =
        retrofit.create(AuthGeneralDataApiServices::class.java)


    @Singleton
    @Provides
    fun provideRegisterApiServices(@Named(NetworkParams.NAME) retrofit: Retrofit): RegisterApiServices =
        retrofit.create(RegisterApiServices::class.java)


    @Singleton
    @Provides
    fun provideOtpApiServices(@Named(NetworkParams.NAME) retrofit: Retrofit): OtpApiServices =
        retrofit.create(OtpApiServices::class.java)

    @Singleton
    @Provides
    fun provideForgotPasswordApiServices(@Named(NetworkParams.NAME) retrofit: Retrofit): ForgotPasswordApiServices =
        retrofit.create(ForgotPasswordApiServices::class.java)

    @Singleton
    @Provides
    fun provideLogInApiServices(@Named(NetworkParams.NAME) retrofit: Retrofit): SignInApiServices =
        retrofit.create(SignInApiServices::class.java)
}