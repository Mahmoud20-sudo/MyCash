package com.codeIn.myCash.ui.home.reports.reports_fragment.dialog.repoorts_borrom_sheet.di

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import com.codeIn.myCash.ui.home.reports.reports_fragment.dialog.repoorts_borrom_sheet.ReportsBottomSheet

@Module
@InstallIn(ActivityComponent::class)
object ReportsBottomSheetModule {

    @Provides
    fun provideReportsBottomSheet(activity: Activity) = ReportsBottomSheet(activity)
}