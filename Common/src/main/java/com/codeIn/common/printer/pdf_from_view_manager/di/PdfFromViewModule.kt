package com.codeIn.common.printer.pdf_from_view_manager.di

import android.app.Activity
import com.codeIn.common.printer.pdf_from_view_manager.PdfFromViewHandler
import com.codeIn.common.printer.pdf_from_view_manager.PdfFromViewManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class PdfFromViewModule {


    @Provides
    fun providePdfFromViewManager(activity: Activity): PdfFromViewHandler =
        PdfFromViewManager(activity)
}