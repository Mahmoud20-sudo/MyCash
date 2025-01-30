package com.codeIn.myCash.features.user.di

import com.codeIn.myCash.features.user.data.accountSettings.remote.AccountSettings
import com.codeIn.myCash.features.user.data.authentication.remote.Authentication
import com.codeIn.myCash.features.user.data.complete_data.remote.ApiCompleteData
import com.codeIn.myCash.features.user.data.edit_profile.remote.ApiEditProfile
import com.codeIn.myCash.features.user.data.shift.remote.Shift
import com.codeIn.myCash.features.user.data.settings.remote.GeneralData
import com.codeIn.myCash.features.user.data.users.remote.ApiUsers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserServices {

    @Singleton
    @Provides
    fun provideAuthentication (retrofit: Retrofit) : Authentication{
        return retrofit.create(Authentication::class.java)
    }

    @Singleton
    @Provides
    fun provideGeneralData (retrofit: Retrofit) : GeneralData {
        return retrofit.create(GeneralData::class.java)
    }

    @Singleton
    @Provides
    fun provideShift(retrofit: Retrofit) : Shift {
        return retrofit.create(Shift::class.java)
    }
    @Singleton
    @Provides
    fun provideAccountSetting(retrofit: Retrofit) : AccountSettings{
        return retrofit.create(AccountSettings::class.java)
    }
    @Singleton
    @Provides
    fun provideUser (retrofit: Retrofit): ApiUsers {
        return retrofit.create(ApiUsers::class.java)
    }
    @Singleton
    @Provides
    fun provideEditProfile (retrofit: Retrofit): ApiEditProfile {
        return retrofit.create(ApiEditProfile::class.java)
    }
    @Singleton
    @Provides
    fun provideApiCompleteData (retrofit: Retrofit): ApiCompleteData {
        return retrofit.create(ApiCompleteData::class.java)
    }
}