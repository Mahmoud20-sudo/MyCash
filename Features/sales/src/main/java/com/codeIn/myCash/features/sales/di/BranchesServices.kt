package com.codeIn.myCash.features.sales.di

import com.codeIn.myCash.features.sales.data.branch.remote.ApiBranches
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
 class BranchesServices {

    @Singleton
    @Provides
     fun provideBranches(retrofit: Retrofit): ApiBranches {
         return retrofit.create(ApiBranches::class.java)
     }
}