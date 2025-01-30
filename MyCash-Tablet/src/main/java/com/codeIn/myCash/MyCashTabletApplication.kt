package com.codeIn.myCash

import com.codeIn.common.base_application.BaseApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyCashTabletApplication : BaseApplication() {


    override fun getRemoteDebuggerPort() = 8081

}