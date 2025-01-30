package com.codeIn.common.base

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.codeIn.common.data.Languages
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.isTablet
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
open class MyCashBaseActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPrefsModule

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation =
            if (isTablet()) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
    }

    open fun setOnBackPressedCallback(
        callBack: OnBackPressedCallback
    ) {
        onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callBack
        )
    }


    fun setMode(mode: String?) {
        if (mode == "light") AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        prefs.putValue(Constants.MODE, mode.toString())
    }

    fun setLocal(lang: Languages) {
        val locale = Locale(lang.value)
        Locale.setDefault(locale)
        val resources: Resources = resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        prefs.putValue(Constants.LANG, lang.value)
    }
}