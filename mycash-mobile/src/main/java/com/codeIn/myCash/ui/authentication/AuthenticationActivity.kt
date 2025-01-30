package com.codeIn.myCash.ui.authentication

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.codeIn.common.base.MyCashBaseActivity
import com.codeIn.common.data.Languages
import com.codeIn.common.offline.Constants
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthenticationActivity : MyCashBaseActivity() {

    private lateinit var controller: NavController

    private lateinit var binding: ActivityAuthenticationBinding
    private lateinit var navOptions: NavOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getting stored language and if there is no previous one set it as Default language
        val lang = prefs.getLanguage()
        val mode = prefs.getValue(Constants.MODE)?.ifEmpty {
            val modeText =
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) "light"
                else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) "dark"
                else "light"

            prefs.getMode(modeText)
        }

        //setting app language as the last stored one
        setLocal(Languages.from(lang))
        mode?.let { setMode(it) }

        if (Build.VERSION.SDK_INT >= 31) {
            window.decorView.layoutDirection = resources.configuration.layoutDirection
        }

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller =
            Navigation.findNavController(findViewById(R.id.nav_host_fragment_activity_authentication))

        // IN CASE OF LOGGED IN BUT NOT COMPLETE HIS DATA
        if (intent?.hasExtra(Constants.completeInoStore()) == true) {
            val inflater = controller.navInflater
            val graph = inflater.inflate(R.navigation.authentication_navigation)
            graph.setStartDestination(R.id.completeInfoFragment)
            controller.graph = graph
        }

        // IN CASE OF LOGGED IN BUT NOT COMPLETE HIS SUBSCRIPTION
        if (intent?.getBooleanExtra(Constants.registerSubscriptionID(), true) == false) {
            val inflater = controller.navInflater
            val graph = inflater.inflate(R.navigation.authentication_navigation)
            graph.setStartDestination(R.id.navigationSubscriptionsFragment)
            controller.graph = graph
        }


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

}