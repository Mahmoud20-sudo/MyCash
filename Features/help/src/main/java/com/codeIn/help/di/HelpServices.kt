package com.codeIn.help.di

import com.codeIn.help.data.remote.ApiHelp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HelpServices {

    @Singleton
    @Provides
    fun provideHelp(retrofit: Retrofit):ApiHelp{
        return retrofit.create(ApiHelp::class.java)
    }
}