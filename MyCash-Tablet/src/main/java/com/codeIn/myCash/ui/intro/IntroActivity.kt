package com.codeIn.myCash.ui.intro

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.codeIn.common.data.Languages
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.ui.authentication.AuthenticationActivity
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.integration.di.app.MyCashPreferences
import com.codeIn.myCash.integration.di.app.MyCashPreferencesKeys
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    @Inject
    lateinit var settings: MyCashPreferences


    @Inject
    lateinit var prefs : SharedPrefsModule

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val mode = settings.getString(MyCashPreferencesKeys.MODE).ifEmpty {
            val modeText = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
                "light"
            else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                "dark"
            else
                "light"
            settings.getString(MyCashPreferencesKeys.MODE , modeText)
        }

        val lang = settings.getString(MyCashPreferencesKeys.LANG).ifEmpty {
            settings.getString(MyCashPreferencesKeys.LANG, Locale.getDefault().language)
        }

        //setting app language as the last stored one
        setLocal(Languages.from(lang))
        setMode(mode)

        if (Build.VERSION.SDK_INT >= 31) {
            window.decorView.layoutDirection = resources.configuration.layoutDirection
        }

        if (prefs.getValue(Constants.getToken()).isNullOrEmpty())
        {
            startActivity(Intent(this, AuthenticationActivity::class.java))
        }
        else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }


    private fun setLocal(lang: Languages) {
        val locale = Locale(lang.value)
        Locale.setDefault(locale)
        val resources: Resources = resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        settings.putString(MyCashPreferencesKeys.LANG, lang.value)
    }


    private fun setMode(mode : String?){
        if (mode == "light"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        settings.getString(MyCashPreferencesKeys.MODE , mode.toString())
    }
}