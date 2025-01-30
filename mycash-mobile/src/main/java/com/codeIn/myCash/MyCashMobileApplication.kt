package com.codeIn.myCash

import com.codeIn.common.base_application.BaseApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyCashMobileApplication : BaseApplication() {


    override fun getRemoteDebuggerPort() = 8080

}