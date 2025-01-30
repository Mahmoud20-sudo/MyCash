package com.codeIn.myCash.ui.authentication.complete_data

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.codeIn.common.data.AppConstants.TAX
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.Languages
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.Constants.Companion.completeInoStore
import com.codeIn.common.offline.Constants.Companion.logoStore
import com.codeIn.common.offline.Constants.Companion.nameStore
import com.codeIn.common.offline.Constants.Companion.taxRecordStore
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentCompleteInfoBinding
import com.codeIn.myCash.features.user.data.complete_data.model.CompleteInfoState
import com.codeIn.myCash.ui.authentication.AuthenticationActivity
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.ui.setErrorMsg
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.bottom_sheets.WarningBottomSheet
import com.codeIn.myCash.utilities.dialogs.LanguageOptionsDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.views.fadeVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class CompleteInfoFragment : Fragment() {

    private val viewModel: CompleteInfoViewModel by viewModels()

    private var _binding: FragmentCompleteInfoBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var prefs: SharedPrefsModule

    private var moveForwardJob: Job? = null

    private val languageOptionsDialogCommunicator = object : LanguageOptionsDialog.Communicator {
        override fun onLanguageSelected(language: Languages) {
            prefs.putValue(Constants.LANG, language.value)
            val intent = Intent(activity, AuthenticationActivity::class.java)
            intent.putExtra(Constants.completeInoStore(), "0")
            startActivity(intent)
            activity?.finish()
        }
    }

    val language: String? by lazy { prefs.getLanguage() }

    private val languageOptionsDialog: LanguageOptionsDialog by lazy {
        LanguageOptionsDialog(requireContext(), language, languageOptionsDialogCommunicator)
    }

    private var warningBottomSheet: WarningBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            languageTextView.apply {
                text =
                    if (language == "en") getString(R.string.English) else getString(R.string.Arabic)
                setOnClickListener {
                    languageOptionsDialog.showLangDialog()
                }
            }

            logoutTextView.setOnClickListener {
                logout()
            }

            addButton.setOnClickListener {
                viewModel.completeData(
                    commercialRecord = commercialRegistrationNumberEditText.text,
                    commercialRecordName = commercialRegistrationNameEditText.text,
                    tax = taxVatEditText.text,
                    taxRecord = taxNumberEditText.text,
                    branchName = branchNameEdittext.text,
                    branchAddress = branchAddressEditText.text,
                )
            }

            viewModel.apply {
                completeInfoState.collectOnLifecycle(
                    lifecycleScope,
                    viewLifecycleOwner
                ) { response ->
                    when (response) {
                        is CompleteInfoState.Loading -> handleLoading()
                        is CompleteInfoState.Idle -> handleIdle()
                        is CompleteInfoState.StateError -> handleError(response.message.toString())
                        is CompleteInfoState.SuccessCompleteData -> {
                            showSuccess(response)
                        }

                        is CompleteInfoState.InputError -> handleInputError(response.inputError)
                        is CompleteInfoState.ServerError -> handleError(
                            response.error.getErrorMessage(
                                requireContext()
                            )
                        )

                        else -> {
                        }

                    }
                }

                logoutResult.collectOnLifecycle(
                    lifecycleScope,
                    viewLifecycleOwner
                ) { response ->
                    when (response) {
                        is CompleteInfoState.Loading -> handleLoading()
                        is CompleteInfoState.Idle -> handleIdle()
                        is CompleteInfoState.StateError -> handleError(response.message.toString())
                        is CompleteInfoState.SuccessLogout -> {
                            viewModel.clearState()
                            prefs.putValue(Constants.getToken(), "")
                            prefs.putValue(Constants.paymentStatus(), "0")
                            val intent = Intent(requireActivity(), IntroActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        is CompleteInfoState.InputError -> handleInputError(response.inputError)
                        is CompleteInfoState.ServerError -> handleError(
                            response.error.getErrorMessage(
                                requireContext()
                            )
                        )

                        else -> {
                        }

                    }
                }
            }
        }
    }

    private fun handleLoading() = binding.loadingCustom.root.fadeVisibility(View.VISIBLE)

    private fun handleIdle() = binding.loadingCustom.root.gone()

    private fun showSuccess(response: CompleteInfoState.SuccessCompleteData) {
        viewModel.clearState()
        binding.loadingSuccess.root.fadeVisibility(View.VISIBLE)
        val user = response.data?.data
        prefs.apply {
            putValue(logoStore(), user?.accountInfo?.logo)
            putValue(nameStore(), user?.accountInfo?.commercialRecordName)
            putValue(taxRecordStore(), user?.accountInfo?.taxRecord)
            putValue(completeInoStore(), user?.isCompleteAccountInfo)
            putValue(TAX, user?.accountInfo?.tax)
        }

        binding.loadingSuccess.animationView.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                Timber.tag("Animation").v("started")
            }

            override fun onAnimationEnd(p0: Animator) {
                Timber.tag("Animation").v("ended")

            }

            override fun onAnimationCancel(p0: Animator) {
                Timber.tag("Animation").v("canceled")
            }

            override fun onAnimationRepeat(p0: Animator) {
                if (moveForwardJob != null) return
                moveForwardJob = lifecycleScope.launch {
                    delay(2000)
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        })
    }

    private fun handleError(message: String) {
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    private fun logout() {
        warningBottomSheet?.dismiss()
        warningBottomSheet = WarningBottomSheet(
            title = getString(R.string.logout),
            message = getString(R.string.logout_message),
            cancelText = getString(R.string.continue_1),
            communicator = object : WarningBottomSheet.Communicator {
                override fun confirm() {
                    viewModel.logout()
                }
            }
        )
        warningBottomSheet?.show(childFragmentManager, WarningBottomSheet.TAG)
    }

    private fun handleInputError(invalidInput: InvalidInput) {
        viewModel.clearState()
        binding.apply {
            when (invalidInput) {
                InvalidInput.COMMERCIAL_RECORD -> {
                    binding.commercialRegistrationNumberEditText.setErrorMsg(
                        getString(R.string.please_enter_valid_commercial_record)
                    )
                    binding.commercialRegistrationNumberEditText.binding.counterTextview.gone()
                }

                InvalidInput.COMMERCIAL_NAME -> binding.commercialRegistrationNameEditText.setErrorMsg(
                    getString(R.string.required_commerical_name)
                )

                InvalidInput.TAX_RECORD -> {
                    binding.taxNumberEditText.setErrorMsg(
                        getString(R.string.please_enter_valid_tax_record)
                    )
                    binding.taxNumberEditText.binding.counterTextview.gone()
                }

                InvalidInput.TAX -> {
                    binding.taxVatEditText.setErrorMsg(
                        getString(R.string.please_enter_valid_tax)
                    )
                    binding.taxVatEditText.binding.counterTextview.gone()
                }

                InvalidInput.BRANCH_NAME -> binding.branchNameEdittext.setErrorMsg(
                    getString(R.string.required_branch_name)
                )

                InvalidInput.BRANCH_ADDRESS -> binding.branchAddressEditText.setErrorMsg(
                    getString(R.string.required_branch_address)
                )

                InvalidInput.EMPTY -> {
                    CustomToaster.show(
                        requireContext(),
                        getString(R.string.please_fill_all_the_fields),
                        isSuccess = false
                    )
                }

                else -> {}
            }
        }
    }

}