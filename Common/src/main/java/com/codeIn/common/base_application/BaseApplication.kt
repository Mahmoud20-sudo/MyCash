package com.codeIn.common.base_application

import android.app.Application
import com.codeIn.common.BuildConfig
import com.pluto.Pluto
import com.pluto.plugins.network.PlutoNetworkPlugin
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import zerobranch.androidremotedebugger.AndroidRemoteDebugger



abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        plantTimberTrees()
        initRemoteDebugger()
        initPluto()
    }


    private fun plantTimberTrees() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.plant(RemoteDebuggerTree())
        }
    }


    private fun initRemoteDebugger() {
        if (!BuildConfig.DEBUG) return
        val remoteDebugger = AndroidRemoteDebugger.Builder(applicationContext)
            .disableInternalLogging()
            .port(getRemoteDebuggerPort())
            .build()
        AndroidRemoteDebugger.init(remoteDebugger)
    }

    abstract fun getRemoteDebuggerPort(): Int


    private inner class RemoteDebuggerTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            AndroidRemoteDebugger.Log.log(priority, tag, message, t)
        }
    }


    private fun initPluto() = Pluto.Installer(this).apply {
        getPlutoPlugins().forEach { addPlugin(it) }
    }.install()


    open fun getPlutoPlugins() = listOf(PlutoNetworkPlugin("network"))
}