package com.codeIn.myCash.ui.options.subscriptions.payment_method

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentPaymentMethodBinding
import com.codeIn.myCash.databinding.FragmentPaymentMethodInBinding
import com.codeIn.myCash.ui.authentication.sign_up.payment_method.PaymentMethodViewModel
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountCodeData
import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountState
import com.codeIn.myCash.features.user.data.authentication.remote.response.disocunt.DiscountState.*
import com.codeIn.myCash.features.user.data.settings.remote.response.settings.SettingsState
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass. This fragment is responsible for handling the payment method selection process.
 */
@AndroidEntryPoint
class PaymentMethodFragment : Fragment() {

    private val viewModel: PaymentMethodViewModel by viewModels()
    private val infoDialog: InfoDialog = InfoDialog()

    private var _binding: FragmentPaymentMethodInBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodInBinding.inflate(inflater, container, false)
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
                findNavController().popBackStack()
            }

            applyCouponButton.setOnClickListener {
                val code = couponCodeTextView.text.toString()
                viewModel.checkCodeDiscount(code)
            }
            nextButton.setOnClickListener {
                //*** We will open payment gateway .,..
                viewModel.getRenewPaymentLink()
            }
        }


        viewModel.apply {
            discountResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { response ->
                when (response) {
                    is Loading -> handleLoading()
                    is Idle -> handleIdle()
                    is DiscountState.InputError ->{
                        handleInputError(response.inputError)
                    }
                    is StateError -> handleError(response.message ?: getString(R.string.error))
                    is Success -> handleSuccess(response.data)
                    is ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }

            settingsResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { response ->
                when (response) {
                    is SettingsState.Loading -> handleLoading()
                    is SettingsState.Idle -> handleIdle()
                    is SettingsState.StateError -> handleError(response.message ?: getString(R.string.error))
                    is SettingsState.Success -> {
                        getPaymentState(response.data?.tax)
                    }
                    is SettingsState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
            generalResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { response ->
                when (response) {
                    is GeneralResponse.Loading -> handleLoading()
                    is GeneralResponse.Idle -> handleIdle()
                    is GeneralResponse.ResponseError -> handleError(response.message ?: getString(R.string.error))
                    is GeneralResponse.Success -> {
                        findNavController().navigate(
                            com.codeIn.myCash.ui.options.subscriptions.payment_method.PaymentMethodFragmentDirections.actionPaymentMethodFragmentToPaymentGatewayFragment(
                                response.message.toString()
                            )
                        )
                        viewModel.clearState()
                    }
                    is GeneralResponse.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
            viewState.collectOnLifecycle(viewModelScope, viewLifecycleOwner) {paymentState ->
                binding.apply {
                    initialTotalTextView.text = paymentState.initialTotal
                    discountTextView.text = paymentState.systemDiscount
                    discountCouponTextView.text = paymentState.discountValue
                    totalPriceTextView.text = paymentState.total
                }
            }
        }
    }


    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleInputError(invalidInput: InvalidInput) {
        binding.loadingLayout.root.gone()
        when (invalidInput) {
            InvalidInput.EMPTY -> {
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_fill_all_the_fields),
                    isSuccess = false
                )
            }
            else -> {
                // do nothing
            }
        }
    }

    private fun handleError(message: String) {
        handleIdle()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
        viewModel.clearState()
    }

    private fun handleSuccess(data: DiscountCodeData?) {
        viewModel.clearState()
        viewModel.updateValues(data)

    }


}