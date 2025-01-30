package com.codeIn.myCash.ui.authentication

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codeIn.common.data.Languages
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ActivityAuthenticationBinding
import com.codeIn.myCash.integration.di.app.MyCashPreferences
import com.codeIn.myCash.integration.di.app.MyCashPreferencesKeys

import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    private lateinit var controller: NavController

    @Inject
    lateinit var settings: MyCashPreferences

    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getting stored language and if there is no previous one set it as Default language
        val lang = settings.getString(MyCashPreferencesKeys.LANG, Locale.getDefault().language)
        val mode = settings.getString(MyCashPreferencesKeys.MODE).ifEmpty {
            val modeText = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
                "light"
            else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                "dark"
            else
                "light"
            settings.getString(MyCashPreferencesKeys.MODE , modeText)
        }

        //setting app language as the last stored one
        setLocal(Languages.from(lang))
        setMode(mode)

        if (Build.VERSION.SDK_INT >= 31) {
            window.decorView.layoutDirection = resources.configuration.layoutDirection
        }

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller =
            Navigation.findNavController(findViewById(R.id.nav_host_fragment_activity_authentication))

        onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            onBackPressedCallback
        )

    }

    fun changeLanguage(lang: Languages) {
        setLocal(lang)
        startActivity(intent)
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



    private val onBackPressedCallback = object : OnBackPressedCallback(
        true // default to enabled
    ) {
        override fun handleOnBackPressed() {
            if (controller.popBackStack()) {
                // Fragment popped successfully
                Log.d("TAG", "Fragment popped successfully: ")
            } else
                if (isEnabled) {
                    isEnabled = false
                    this@AuthenticationActivity.onBackPressed()
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        onBackPressedCallback.isEnabled = true
    }

    private fun setMode(mode : String?){
        Log.d("TAG" , "Mode is $mode")
        if (mode == "light"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        settings.getString(MyCashPreferencesKeys.MODE , mode.toString())
    }
}