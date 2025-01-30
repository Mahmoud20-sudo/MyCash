package com.codeIn.myCash.di.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {
    @Provides
    @Named("settings")
    fun provideSettingsSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    }

    @Provides
    @Named("user")
    fun provideUserSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("user", Context.MODE_PRIVATE)
    }
}