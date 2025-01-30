package com.codeIn.myCash.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.Languages
import com.codeIn.common.data.Shift
import com.codeIn.common.nearpay.NearPayPayment
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ActivityMainBinding
import com.codeIn.myCash.ui.authentication.AuthenticationActivity
import com.codeIn.myCash.ui.authentication.sign_up.payment_method.PaymentMethodFragmentDirections
import com.codeIn.myCash.ui.home.shifts_dialogs.EndShiftDialog
import com.codeIn.myCash.ui.home.shifts_dialogs.ShiftListener
import com.codeIn.myCash.ui.home.shifts_dialogs.StartShiftDialog
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.myCash.utilities.bottom_sheets.WarningBottomSheet
import com.codeIn.myCash.utilities.views.DrawerSlideAnimation
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftData
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftState
import com.codeIn.myCash.integration.di.app.MyCashPreferences
import com.codeIn.myCash.integration.di.app.MyCashPreferencesKeys
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var settings: MyCashPreferences

    @Inject
    lateinit var prefs: SharedPrefsModule
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setMode(mode)
        setLocal(Languages.from(lang))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)


        // enable the drawer slide animation
        DrawerSlideAnimation(this).enableDrawerSlideAnimation(
            mainLayout = binding.cardView,
            drawerLayout = binding.drawerLayout,
            blockView = binding.blockLayout,
            toolbar = null
        )


        binding.apply {
            infoButton.setOnClickListener {
                openDrawer()
            }
            logOutImageView.setOnClickListener {
                if (NFCChecker.checkNotPOSAndNFC(this@MainActivity)) {
                    handleDrawerItemClick(it, R.id.madaPaymentFragment, "mada")
                } else  {
                    CustomToaster.show(
                        this@MainActivity,
                        getString(R.string.this_device_is_not_support_nfc),
                        false
                    )
                }
            }
        }
        productsLabel?.let {
            changeProductsLabel(it)
        }

        bindDrawerData(
            storeName = getString(R.string.app_name),
            taxNumber = getString(R.string.unknown)
        )

        viewModel.apply {
            userResult.collectOnLifecycle(viewModelScope, this@MainActivity) { response ->
                when (response) {

                    is AuthenticationState.Loading -> {}

                    is AuthenticationState.Idle -> {}

                    is AuthenticationState.StateError -> {}

                    is AuthenticationState.UnAuthorized -> {}

                    is AuthenticationState.Success -> {
                        val userImage = response.data?.accountInfo?.logo
                        viewModel.pref.putValue(Constants.getCurrency() , response.data?.country?.currency)
                        viewModel.pref.putValue(Constants.logoStore() , response.data?.accountInfo?.logo)
                        viewModel.pref.putValue(Constants.nameStore() , response.data?.accountInfo?.commercialRecordName)
                        viewModel.pref.putValue(Constants.taxRecordStore() , response.data?.accountInfo?.taxRecord)
                        viewModel.pref.putValue(Constants.completeInoStore() , response.data?.isCompleteAccountInfo)
                        bindDrawerData(
                            userImage = response.data?.accountInfo?.logo,
                            storeName = response.data?.accountInfo?.commercialRecordName ?: getString(R.string.app_name),
                            taxNumber = response.data?.accountInfo?.taxRecord ?: getString(R.string.unknown)
                        )
                        updateUser(response.data)
                        saveTax(response.data?.accountInfo?.tax ?: "15")

                        val paymentStatus = response.data?.paymentStatus?:"0"
                        val expire = response.data?.subscription?.expire?:"0"
                        val days = response.data?.subscription?.daysLeft?:"0"

                        if (paymentStatus == "1"){
                            if (expire == "1")
                                viewModel.expire.value = true
                            else {
                                if (response.data?.isCompleteShitInfo.equals("0"))
                                    showStartShiftDialog(userModel = response.data)
                                if (days.toInt() <= 3){
                                    warningMessage.value = days
                                    warning.value = true
                                }
                            }
                            isPaid.value = true
                        }
                        else if(paymentStatus == "0"){
                            isPaid.value = false
                        }
                    }
                    is AuthenticationState.InputError -> {
                        // Handle InputError state if needed
                    }

                    is AuthenticationState.ServerError -> {
                        // Handle ServerError state if needed
                    }
                    // Add handling for other cases as needed
                    AuthenticationState.ResendCodeSuccess -> TODO()
                    is AuthenticationState.SuccessResendOtp -> TODO()
                }


            }

            dataResult.collectOnLifecycle(viewModelScope, this@MainActivity) { response ->
                when (response) {
                    is ShiftState.Loading -> {}
                    is ShiftState.Idle -> {}
                    is ShiftState.StateError -> {}
                    is ShiftState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(this@MainActivity, IntroActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is ShiftState.StartShift -> {
                        dismissStartShiftDialog()
                    }

                    is ShiftState.EndShift -> {
                        dismissEndShiftDialog()
                    }

                    is ShiftState.InputError -> {
                        handleInputError(response.inputError)
                    }

                    is ShiftState.ServerError -> {}
                    is ShiftState.CurrentShift -> {
                        showEndShiftDialog(shiftData = response.data, userModel = user.value)
                    }

                    else -> {}
                }
            }

            warning.observe(this@MainActivity){
                if (it){
                    CustomToaster.show(context = this@MainActivity ,
                        message = getString(R.string.hintWarning) +" "+ warningMessage.value ,
                        isSuccess = false)
                }
            }
            expire.observe(this@MainActivity){
                if (it == true){
                    warningExpireDialog()
                }
            }
            isPaid.observe(this@MainActivity){
                Log.d("TAG" , "Paid is ${isPaid.value}")
                if (!it){
                    viewModel.getPaymentLink()
                }
            }

            generalResult.collectOnLifecycle(viewModelScope, this@MainActivity) { response ->
                when (response) {
                    is GeneralResponse.Loading -> {}
                    is GeneralResponse.Idle -> {}
                    is GeneralResponse.ResponseError -> {}
                    is GeneralResponse.Success -> {
                        viewModel.pref.putValue(AppConstants.PAYMENT_LINK , response.message.toString())
                        navController.navigate(R.id.paymentGatewayFragment)
                        warningBottomSheet?.dismiss()
                       viewModel.clearState()
                    }
                    is GeneralResponse.ServerError -> {}
                    else -> {}
                }
            }
        }
    }

    private var warningBottomSheet: WarningBottomSheet? = null
    private fun warningExpireDialog() {
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = getString(R.string.terminatedAccount),
            message = getString(R.string.alertPackage)+"\n"+getString(R.string.alarmExpire),
            confirmText = getString(R.string.renew_subscription),
            cancelText = getString(R.string.sign_out) ,
            cancelable = false ,
            communicator = object : WarningBottomSheet.Communicator {
                override fun confirm() {
                    val navOptions = NavOptions.Builder()
                        .setEnterAnim(R.anim.fade_in_with_scale)
                        .setExitAnim(R.anim.fade_out)
                        .setPopEnterAnim(R.anim.fade_in)
                        .setPopExitAnim(R.anim.fade_out)
                        .build()
                    navController.navigate(R.id.navigation_subscriptionHostFragment, null, navOptions)
                    warningBottomSheet?.dismiss()
                }
                override fun cancel() {
                    viewModel.currentShift()
                }
            }
        )
        warningBottomSheet?.show(supportFragmentManager, WarningBottomSheet.TAG)
    }


    private fun handleInputError(invalidInput: InvalidInput) {
        when (viewModel.shiftStatus.value) {
            Shift.Start.value -> {
                viewModel.clearShiftState()
                viewModel.clearAuthenticationState()
                when (invalidInput) {
                    InvalidInput.EMPTY -> {
                        CustomToaster.show(
                            this,
                            getString(R.string.please_fill_all_the_fields),
                            isSuccess = false
                        )
                    }

                    else -> {
                        dialogStartShift.value.setError(invalidInput)
                    }
                }
            }

            Shift.End.value -> {
                viewModel.clearShiftState()
                viewModel.clearAuthenticationState()
                when (invalidInput) {
                    InvalidInput.EMPTY -> {
                        CustomToaster.show(
                            this,
                            getString(R.string.please_fill_all_the_fields),
                            isSuccess = false
                        )
                    }

                    else -> {
                        dialogEndShift.value.setError(invalidInput)
                    }
                }
            }
        }
    }

    private fun openDrawer() {
        if (binding.drawerLayout.isOpen)
            binding.drawerLayout.close()
        else binding.drawerLayout.open()
    }

    private val dialogStartShift = lazy {
        StartShiftDialog(
            context = this@MainActivity,
            userModel = null,
            listener = listenerStartShift
        )
    }
    private val dialogEndShift = lazy {
        EndShiftDialog(
            context = this@MainActivity,
            userModel = null,
            shiftData = null,
            listener = listenerStartShift
        )
    }

    private fun showStartShiftDialog(userModel: User?) {
        viewModel.updateShiftState(Shift.Start.value)
        dialogStartShift.value.setData(userModel = userModel)
        if (!dialogStartShift.value.isShowing)
            dialogStartShift.value.show()
        viewModel.clearAuthenticationState()
        dialogStartShift.value.setCancelable(false)
        dialogStartShift.value.setCanceledOnTouchOutside(false)
    }

    private fun dismissStartShiftDialog() {
        if (dialogStartShift.value.isShowing)
            dialogStartShift.value.dismiss()
    }

    private fun showEndShiftDialog(userModel: User?, shiftData: ShiftData?) {
        viewModel.updateShiftState(Shift.End.value)
        dialogEndShift.value.setData(shiftData = shiftData, userModel = userModel)
        if (!dialogEndShift.value.isShowing)
            dialogEndShift.value.show()
        viewModel.clearShiftState()
    }

    private fun dismissEndShiftDialog() {
        if (dialogEndShift.value.isShowing)
            dialogEndShift.value.dismiss()
        val intent = Intent(this@MainActivity, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val listenerStartShift = object : ShiftListener {
        override fun startShift(cash : String? , visa : String? , differentCash : String? , differentVisa : String?) {
            viewModel.startShift(cash = cash, visa = visa , differentCash= differentCash , differentVisa= differentVisa)
        }

        override fun endShift(cash: String?, visa: String?) {
            viewModel.logout(visa = visa, cash = cash)
        }
    }

    // duration of the animation of the bottom navigation view and the top bar layout
    private val navViewAnimationDuration = 100L


    fun hideNavBottom(navBottom: Boolean = true, topBar: Boolean = true) {
        if (navBottom)
            MyAnimator().animateTranslationYHide(
                view = binding.navView,
                duration = navViewAnimationDuration
            )

        if (topBar)
            MyAnimator().animateTranslationYHide(
                view = binding.topBarLayout,
                direction = -1,
                duration = navViewAnimationDuration
            )
    }

    fun showNavBottom(navBottom: Boolean = true, topBar: Boolean = true) {
        if (navBottom)
            MyAnimator().animateTranslationYShow(
                view = binding.navView,
                duration = navViewAnimationDuration
            )

        if (topBar)
            MyAnimator().animateTranslationYShow(
                view = binding.topBarLayout,
                direction = -1,
                duration = navViewAnimationDuration
            )
    }

    fun isNavBottomVisible(): Boolean {
        return binding.navView.visibility == View.VISIBLE
    }

    private var productsLabel: String? = null
    fun changeProductsLabel(label: String) {
        productsLabel = label
        // check if binding is initialized
        if (this::binding.isInitialized)
            binding.navView.menu.findItem(R.id.navigation_productsHostFragment).title = label
    }

    fun setOnBackPressedCallback(
        callBack: OnBackPressedCallback
    ) {
        onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callBack
        )
    }


    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Log.d("TAG" , "Wea re here in baclpreessed of main activity")

        if (binding.drawerLayout?.isOpen == true)
            binding.drawerLayout.close()
        else
            super.onBackPressed()

    }

    private var previousView: View? = null


    fun bindDrawerData(
        userImage: String? = null,
        storeName: String? = getString(R.string.app_name),
        taxNumber: String? = getString(R.string.unknown)
    ) {
        Glide.with(this@MainActivity)
            .load(userImage)
            .placeholder(R.drawable.ic_images_placeholder)
            .error(R.drawable.ic_images_placeholder)
            .into(binding.navViewDrawerLayout.userImageView)

        binding.navViewDrawerLayout.taxNumberTextView.text = taxNumber
        binding.navViewDrawerLayout.storeNameTextView.text = storeName

        val version = try {
            packageManager.getPackageInfo(this.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            getString(R.string.unknown)
        }
        val currentVersion = getString(R.string.current_version, version)
        binding.navViewDrawerLayout.apply {
            versionNumberTextView.text = currentVersion
            storeNameTextView.text = storeName
            taxNumberTextView.text = getString(R.string.tax_number, taxNumber)
            Glide.with(this@MainActivity)
                .load(userImage)
                .placeholder(R.drawable.ic_images_placeholder)
                .into(userImageView)

            userImageView.setOnClickListener {
                handleDrawerItemClick(it, R.id.navigation_profileHostFragment, "Profile")
            }
        }
        binding.apply {
            infoButton.text = storeName
        }
        // set the click listeners of the drawer menu items
        with(binding.navViewDrawerLayout) {
            val drawerItems = mapOf(
                notificationsTextView to Pair(
                    R.id.navigation_notificationsFragment,
                    "Notifications"
                ),
                draftsTextView to Pair(R.id.navigation_draftsFragment, "Drafts"),
                branchesTextView to Pair(R.id.navigation_branchesHostFragment, "Branches"),
                helpTextView to Pair(R.id.navigation_helpFragment, "Help"),
                settingsTextView to Pair(R.id.navigation_settingsHostFragment, "Settings"),
                usersTextView to Pair(R.id.navigation_usersHostFragment, "Users"),
                subscriptionsTextView to Pair(
                    R.id.navigation_subscriptionHostFragment,
                    "Subscriptions"
                ),
            )

            drawerItems.forEach { (view, value) ->
                view.setOnClickListener {
                    handleDrawerItemClick(view, value.first, value.second)
                }
            }

            logOutTextView.setOnClickListener {
                openDrawer()
                viewModel.currentShift()
            }
        }
    }


    private fun handleDrawerItemClick(view: View, destinationId: Int, message: String) {

        // build the navOptions to set the animation of the navigation component
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in_with_scale)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()

        previousView?.isSelected = false
        view.isSelected = true
        previousView = view

        navController.navigate(destinationId, null, navOptions)

        binding.drawerLayout.close()
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }


    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->

            val shouldShowNavBottom = when (destination.id) {
                R.id.navigation_productsHostFragment,
                R.id.navigation_clientsHostFragment,
                R.id.navigation_invoicesHostFragment,
                R.id.navigation_transactionsHostFragment,
                R.id.navigation_reportsHostFragment -> true

                else -> false
            }

            if (shouldShowNavBottom) {
                showNavBottom()
            } else {
                hideNavBottom()
            }
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

}