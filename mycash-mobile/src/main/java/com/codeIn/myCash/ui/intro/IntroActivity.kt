package com.codeIn.myCash.ui.intro

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.codeIn.common.base.MyCashBaseActivity
import com.codeIn.common.data.Languages
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.ui.authentication.AuthenticationActivity
import com.codeIn.myCash.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : MyCashBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val mode = prefs.getValue(Constants.MODE)?.ifEmpty {
            val modeText =
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
                    "light"
                else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    "dark"
                else
                    "light"
            prefs.getMode(modeText)
        }
        val lang = prefs.getLanguage()

        //setting app language as the last stored one
        setLocal(Languages.from(lang))
        setMode(mode)

        if (Build.VERSION.SDK_INT >= 31) {
            window.decorView.layoutDirection = resources.configuration.layoutDirection
        }

        prefs.apply {
            startActivity(
                Intent(
                    this@IntroActivity,
                    if (!getValue(Constants.getToken()).isNullOrEmpty() && getValue(Constants.paymentStatus()) == "1")
                        MainActivity::class.java else AuthenticationActivity::class.java
                ).apply {
                    putExtra(
                        Constants.registerSubscriptionID(),
                        getValue(Constants.getToken()).isNullOrEmpty()
                    )
                }
            )
        }

        finish()
    }
}