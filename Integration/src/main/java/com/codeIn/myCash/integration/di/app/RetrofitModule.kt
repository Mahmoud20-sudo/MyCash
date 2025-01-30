package com.codeIn.myCash.integration.di.app

import com.codeIn.myCash.integration.model.network.NetworkParams.Companion.BASE_URL
import com.codeIn.myCash.integration.model.network.NetworkParams.Companion.NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TestOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @TestOkHttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient(): () -> OkHttpClient = {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    @Named(NAME)
    fun provideMyCashAppRetrofit(@TestOkHttpClient okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

}