package com.codeIn.myCash.ui.options.settings.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.Languages
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentSettingsBinding
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.dialogs.LanguageOptionsDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val viewModel: SettingsViewModel by viewModels()
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs: SharedPrefsModule

    private val languageOptionsDialogCommunicator = object : LanguageOptionsDialog.Communicator {

        override fun onLanguageSelected(language: Languages) {
            prefs.putValue(Constants.LANG, language.value)

            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private val languageOptionsDialog by lazy { LanguageOptionsDialog(
        requireContext(), prefs.getLanguage(), languageOptionsDialogCommunicator)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            backArrow.setOnClickListener {
                activity?.onBackPressed()
            }

            invoiceSettingsTextView.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.navigation_settingsFragment)
                    findNavController().navigate(com.codeIn.myCash.ui.options.settings.settings.SettingsFragmentDirections.actionNavigationSettingsFragmentToNavigationInvoiceSettingsFragment())
            }

            deviceInfoTextView.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.navigation_settingsFragment)
                    findNavController().navigate(com.codeIn.myCash.ui.options.settings.settings.SettingsFragmentDirections.actionNavigationSettingsFragmentToDeviceInfoFragment())

            }
            nightModeSwitch.setOnClickListener {
                val mode = if (nightModeSwitch.isChecked)
                    "dark"
                else
                    "light"
                setMode(mode)
            }
            languageTextView.setOnClickListener {
                languageOptionsDialog?.show()
            }

            draftsSwitch.setOnClickListener {
                setValues()
            }

            notificationsAndAlertsSwitch.setOnClickListener {
                setValues()
            }
            quickInvoiceModeSwitch.setOnClickListener {
                setValues()
            }

        }

        viewModel.apply {
            dataResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is AuthenticationState.Loading -> {}
                    is AuthenticationState.Idle -> {}
                    is AuthenticationState.StateError -> {}
                    is AuthenticationState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                    is AuthenticationState.Success -> {
                        if (initial.value == 0) {
                            binding.quickInvoiceModeSwitch.isChecked =
                                response.data?.accountInfo?.quickInvoice == "1"
                            binding.draftsSwitch.isChecked =
                                response.data?.accountInfo?.drafts == "1"
                            binding.notificationsAndAlertsSwitch.isChecked =
                                response.data?.accountInfo?.notification == "1"
                            viewModel.initialized()

                            val mode = prefs.getMode()
                            binding.nightModeSwitch.isChecked = mode == "dark"
                            // this line make a bug in the app
//                            settings.putBoolean(MyCashPreferencesKeys.QUICK_INVOICE_MODE, binding.quickInvoiceModeSwitch.isChecked)
                        }
                        viewModel.clearState()
                    }

                    is AuthenticationState.InputError -> {}
                    is AuthenticationState.ServerError -> {}
                    else -> {}
                }
            }
        }


    }

    private fun setValues() {
        val drafts = if (binding.draftsSwitch.isChecked)
            "1"
        else
            "0"
        val notifications = if (binding.notificationsAndAlertsSwitch.isChecked)
            "1"
        else
            "0"
        val quickInvoiceMode = if (binding.quickInvoiceModeSwitch.isChecked)
            "1"
        else
            "0"
        viewModel.enableFeatures(drafts, notifications, quickInvoiceMode)
    }

    private fun setMode(mode: String?) {
        prefs.putValue(Constants.MODE, mode.toString())
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()

    }
}