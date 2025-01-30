package com.codeIn.myCash.ui.home.products.second_step_create_invoice

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentType
import com.codeIn.common.nearpay.NearPayPayment
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker.Companion.checkNFC
import com.codeIn.common.payment.NFCChecker.Companion.checkNotPOSAndNFC
import com.codeIn.common.payment.NFCChecker.Companion.checkPhoneNotSupportNFC
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.myCash.databinding.FragmentPaymentProcessBinding
import com.codeIn.myCash.ui.ViewStrokes
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.common.util.gone
import com.codeIn.common.util.isTablet
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.ui.showError
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.model.ClientRequest
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import dagger.hilt.android.AndroidEntryPoint
import io.nearpay.sdk.data.models.ReconciliationReceipt
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.ReconcileFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SecondStepCreationInvoiceFragment : Fragment() {

    private val viewModel: SecondStepCreationInvoiceViewModel by viewModels()
    private val datePicker = DatePicker()
    private var _binding: FragmentPaymentProcessBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs: SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentProcessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        activity?.onBackPressedDispatcher?.addCallback() {
//            lifecycleScope.launchWhenResumed {
//                findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_cart", viewModel.selectedProductsInCart.value)
//                findNavController().popBackStack()
//            }
//        }

        binding.apply {
            backArrow.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "products_in_cart",
                    viewModel.selectedProductsInCart.value
                )
                findNavController().popBackStack()
            }

            backButton.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "products_in_cart",
                    viewModel.selectedProductsInCart.value
                )
                findNavController().popBackStack()
            }
            cashTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.CASH)
            }
            creditCardTextView.setOnClickListener {
                if (checkNotPOSAndNFC(requireContext()))
                    viewModel.updatePaymentType(PaymentType.CREDIT_CARD)
                else if (checkPhoneNotSupportNFC(requireContext())) {
                    CustomToaster.show(
                        requireContext(),
                        getString(R.string.this_device_is_not_support_nfc),
                        false
                    )
                }
                else if(checkNFC(requireContext()) == 2){
                    CustomToaster.show(
                        requireContext(),
                        getString(R.string.this_device_nfc_disable),
                        false
                    )
                }
            }
            postpaidTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.POST_PAID)
            }
            cashAndCreditCardTextView.setOnClickListener {
                if (checkNotPOSAndNFC(requireContext())) {
                    viewModel.updatePaymentType(PaymentType.CASH_AND_CREDIT_CARD)
                } else if (checkPhoneNotSupportNFC(requireContext())) {
                    CustomToaster.show(
                        requireContext(),
                        getString(R.string.this_device_is_not_support_nfc),
                        false
                    )
                }
            }
            postpaidAndCreditCardTextView.setOnClickListener {
                if (checkNotPOSAndNFC(requireContext())) {
                    viewModel.updatePaymentType(PaymentType.POST_PAID_AND_CREDIT_CARD)
                } else if (checkPhoneNotSupportNFC(requireContext())) {
                    CustomToaster.show(
                        requireContext(),
                        getString(R.string.this_device_is_not_support_nfc),
                        false
                    )
                }
            }
            installmentPaymentTextView.setOnClickListener {
//                viewModel.updatePaymentType(PaymentType.INSTALLMENT)
            }

            invoicePaymentDateEditText.setOnClickListener {
                datePicker.showDatePicker(
                    childFragmentManager = parentFragmentManager,
                    editText = invoicePaymentDateEditText,
                    preventOldBates = true
                )
            }

            addNewClient?.setOnClickListener {
                showAddClientToInvoiceDialog()
            }

            payInvoiceButton.setOnClickListener {
                var cashValue = cashPaidAmountEditText.text.toString()
                var visaValue = creditCardPaidAmountEditText.text.toString()
                if (viewModel.paymentType.value == PaymentType.CASH)
                    cashValue = viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                if (viewModel.paymentType.value == PaymentType.CREDIT_CARD)
                    visaValue = viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                viewModel.makeInvoice(
                    cashPrice = cashValue,
                    visaPrice = visaValue,
                    date = invoicePaymentDateEditText.text.toString()
                )

            }

            arrayAdapter =
                SearchClientsAutoCompleteTextViewAdapter(requireContext(), ArrayList<ClientModel>())
            binding.newUserInput?.setAdapter(arrayAdapter)

            binding.newUserInput?.setDropDownBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.autocomplete_dropdown,
                    null
                )
            )

            binding.newUserInput?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    cooCommunicator.searchClient(s.toString())
                }

                override fun afterTextChanged(s: Editable) {

                }
            })

            binding.newUserInput?.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                    val autoCompleteText = adapterView.getItemAtPosition(i) as ClientModel
                    binding.invoiceClientsLayout?.apply {
                        root.visible()
                        clientNameTextView.text = autoCompleteText.name
                        clientPhoneTextView.text = autoCompleteText.phone
                    }
                    viewModel.updateClientIdInInvoiceData(autoCompleteText.id)
                    hideKeyboard(requireContext(), binding.newUserInput)
                    arrayAdapter.clearData()
                    binding.newUserInput?.apply {
                        clearFocus()
                        binding.newUserInput?.setText("")
                    }
                }

            binding.invoiceClientsLayout?.deleteClient?.setOnClickListener {
                binding.invoiceClientsLayout?.root?.gone()
                viewModel.selectedProductsInCart.value?.clientRequest = null
                viewModel.selectedProductsInCart.value?.clientId = null
            }
        }

        viewModel.apply {
            paymentType.observe(viewLifecycleOwner) { type ->
                this@SecondStepCreationInvoiceFragment.updatePaymentType(type = type)
                when (type) {
                    PaymentType.POST_PAID -> handlePostPaid()
                    PaymentType.CASH_AND_CREDIT_CARD -> handleCashAndCard()
                    PaymentType.POST_PAID_AND_CREDIT_CARD -> handlePostPaidAndCard()
                    else -> {}
                }
            }

            selectedProductsInCart.observe(viewLifecycleOwner) {
                showPricesSummary(it)
            }

            clientDataResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is ClientState.Loading -> handleLoading()
                    is ClientState.Idle -> handleIdle()
                    is ClientState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                    is ClientState.StateError -> handleError(response.message.toString())
                    is ClientState.SuccessShowSingleClient -> handleClientSuccess(response.data)
                    is ClientState.ServerError -> handleError(
                        response.error.getErrorMessage(
                            requireContext()
                        )
                    )

                    is ClientState.SuccessShowClients -> {
                        response.data?.data?.let {
                            arrayAdapter.addAll(it)
                            clientInfoDialog?.setAutoCompleteData(it)
                            binding.newUserInput?.showDropDown()
                        }

                    }

                    else -> {}
                }
            }
            invoiceDataResult.collectOnLifecycle(viewModelScope, viewLifecycleOwner) { response ->
                when (response) {
                    is InvoiceState.Loading -> handleLoading()
                    is InvoiceState.Idle -> handleIdle()
                    is InvoiceState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                    is InvoiceState.StateError -> handleError(response.message.toString())
                    is InvoiceState.SuccessShowSingleInvoice -> {
                        Log.d("TAG", "Hello in nearpay $response")
                        binding.loadingLayout.root.gone()
                        if (paymentType.value == PaymentType.CASH_AND_CREDIT_CARD ||
                            paymentType.value == PaymentType.POST_PAID_AND_CREDIT_CARD ||
                            paymentType.value == PaymentType.CREDIT_CARD
                        ) {
                            val value =
                                NumberHelper.persianToEnglishText(response.data?.visaPrice ?: "0.0")
                            if (value.toDouble() > 0) {
                                if (checkNotPOSAndNFC(requireContext())) {
                                    invoiceId.value = response.data?.id.toString()
                                    NearPayPayment.purchaseNearPay(
                                        view, value,
                                        "9ace70b7-977d-4094-b7f4-4ecb17de6753",
                                        "",
                                        nearPayListener
                                    )

                                } else
                                    handleInvoiceSuccess(response.data)
                            }
                            clearStateInvoice()
                        } else
                            handleInvoiceSuccess(response.data)
                    }

                    is InvoiceState.ServerError -> handleError(
                        response.error.getErrorMessage(
                            requireContext()
                        )
                    )

                    is InvoiceState.InputError -> handleInputError(response.inputError)
                    is InvoiceState.SuccessConfirmVisa -> handleInvoiceSuccess(response.data)
                    else -> {}
                }
            }


            viewModelScope.launch {
                searchText
                    .debounce(AppConstants.DELAY_TIME_SEARCH) // Debounce for 500 milliseconds
                    .distinctUntilChanged() // Only emit distinct values
                    .collect { query ->
                        viewModel.getClients(query)
                    }
            }
        }
    }

    private fun showPricesSummary(productsInCart: ProductsInCart?) {
        binding.apply {
            initialTotalTextView.text = productsInCart?.initialTotal ?: "0.0"
            vat15TextView.text = productsInCart?.tax ?: "0.0"
            discountTextView.text = productsInCart?.discount ?: "0.0"
            totalPriceTextView.text = productsInCart?.finalTotal ?: "0.0"
            totalAfterDiscountTextView.text = productsInCart?.totalAfterDiscount ?: "0.0"
            vatLabel.text = "${getString(R.string.tax)} (${viewModel.tax}%)"

            remainingTextView.text = NumberHelper.persianToEnglishText(
                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
            )
        }
    }

    private val nearPayListener = object : NearPayPayment.Companion.Listener {


        override fun onPurchaseProcess(
            code: Int,
            transactionData: TransactionData?,
            purchaseFailure: PurchaseFailure?,
            cashPrice: String?,
            visaPrice: String?
        ) {
            if (code == 0) {
                viewModel.confirmVisa(
                    viewModel.invoiceId.value,
                    transactionData?.receipts?.get(0)?.transaction_uuid,
                    transactionData?.receipts?.get(0)?.approval_code?.value.toString(),
                    transactionData?.receipts?.get(0)?.created_at,
                )
            } else {
                when (code) {
                    1 -> {
                        CustomToaster.show(
                            requireContext(),
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }

                    2 -> {
                        CustomToaster.show(
                            requireContext(),
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }

                    3 -> {
                        CustomToaster.show(
                            requireContext(),
                            getString(R.string.authentication_failed),
                            false
                        )
                    }

                    4 -> {
                        CustomToaster.show(
                            requireContext(),
                            getString(R.string.general_error),
                            false
                        )
                    }

                    5 -> {
                        CustomToaster.show(
                            requireContext(),
                            getString(R.string.general_error),
                            false
                        )
                    }
                }
            }
        }

        override fun onRefundProcess(
            code: Int,
            transaction: TransactionData?,
            purchaseFailure: RefundFailure?,
            cashPrice: String?,
            visaPrice: String?,
            paymentType: Int
        ) {
        }

        override fun onReconciliationProcess(
            code: Int,
            receipt: ReconciliationReceipt?,
            reconcileFailure: ReconcileFailure?
        ) {
            TODO("Not yet implemented")
        }

    }

    private fun updatePaymentType(type: PaymentType) {
        val context = requireContext()
        val binding = binding

        val viewsToStyle = listOf(
            binding.cashTextView,
            binding.creditCardTextView,
            binding.postpaidTextView,
            binding.cashAndCreditCardTextView,
            binding.postpaidAndCreditCardTextView,
            binding.installmentPaymentTextView
        )

        binding.apply {
            when (type) {
                PaymentType.POST_PAID -> {
                    cashPaidAmountEditText.visible()
                    remainingLayout.visible()
                    invoicePaymentDateEditText.visible()
                    creditCardPaidAmountEditText.gone()
                    cashPaidAmountEditText.binding.editText.setText("")
                    creditCardPaidAmountEditText.setText("")
                    remainingTextView.text = NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                    )
                }

                PaymentType.CASH_AND_CREDIT_CARD -> {
                    cashPaidAmountEditText.visible()
                    creditCardPaidAmountEditText.visible()
                    remainingLayout.gone()
                    invoicePaymentDateEditText.gone()
                    cashPaidAmountEditText.binding.editText.setText("")
                    remainingTextView.text = ""
                    creditCardPaidAmountEditText.isEnabled = false
                    creditCardPaidAmountEditText.setText(
                        NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                        )
                    )
                }

                PaymentType.POST_PAID_AND_CREDIT_CARD -> {
                    cashPaidAmountEditText.visible()
                    creditCardPaidAmountEditText.visible()
                    remainingLayout.visible()
                    invoicePaymentDateEditText.visible()
                    cashPaidAmountEditText.binding.editText.setText("")
                    creditCardPaidAmountEditText.isEnabled = true
                    creditCardTextView.text = ""
                    remainingTextView.text = NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                    )

                }

                else -> {
                    cashPaidAmountEditText.gone()
                    creditCardPaidAmountEditText.gone()
                    remainingLayout.gone()
                    invoicePaymentDateEditText.gone()
                    cashPaidAmountEditText.binding.editText.setText("")
                    creditCardPaidAmountEditText.setText("")
                }
            }
        }
        val selectedView = when (type) {
            PaymentType.CASH -> binding.cashTextView
            PaymentType.CREDIT_CARD -> binding.creditCardTextView
            PaymentType.POST_PAID -> binding.postpaidTextView
            PaymentType.CASH_AND_CREDIT_CARD -> binding.cashAndCreditCardTextView
            PaymentType.POST_PAID_AND_CREDIT_CARD -> binding.postpaidAndCreditCardTextView
            PaymentType.INSTALLMENT -> binding.installmentPaymentTextView
        }

        updateSectionsViews(
            context = context,
            viewsToStyle = viewsToStyle,
            selectedView = selectedView,
            stroke = ViewStrokes.R12_S5
        )

        hideKeyboard(requireContext(), binding.root)
    }

    private var clientInfoDialog: ClientInfoDialog? = null
    private lateinit var arrayAdapter: SearchClientsAutoCompleteTextViewAdapter

    private fun showAddClientToInvoiceDialog() {
        if (clientInfoDialog?.isShowing == true)
            clientInfoDialog?.dismiss()
        clientInfoDialog = ClientInfoDialog(
            context = requireContext(),
            createClientValidationUseCase = viewModel.createClientValidationUseCase,
            vendorValidationUseCase = viewModel.createVendorValidationUseCase,
            invoiceType = viewModel.selectedProductsInCart.value?.mainTypeInvoice
                ?: MainTypeInvoice.PURCHASE,
            clientRequest = viewModel.selectedProductsInCart.value?.clientRequest,
            arrayAdapter = arrayAdapter,
            communicator = cooCommunicator
        )
        clientInfoDialog?.show()
    }

    private val cooCommunicator = object : ClientInfoDialog.Communicator {
        override fun addClient(clientRequest: ClientRequest?) {
            if (clientRequest != null)
                viewModel.updateClientRequest(clientRequest)
            if (clientRequest?.id == null) viewModel.createClient()
             else viewModel.updateClientIdInInvoiceData(clientRequest.id.toString())

            binding.invoiceClientsLayout.apply {
                root.visible()
                clientNameTextView.text = clientRequest?.name
                clientPhoneTextView.text = clientRequest?.phone
            }
        }

        override fun searchClient(search: String?) {
            viewModel.updateSearch(search)
        }

    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        viewModel.clearStateClient()
        viewModel.clearStateInvoice()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    private fun handleClientSuccess(clientModel: ClientModel?) {
        viewModel.clearStateClient()
        viewModel.clearStateInvoice()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        viewModel.updateClientIdInInvoiceData(clientModel?.id.toString())
        binding.invoiceClientsLayout.apply {
            root.visible()
            clientNameTextView.text = clientModel?.name
            clientPhoneTextView.text = clientModel?.phone
        }
    }

    private fun handleInvoiceSuccess(invoiceModel: InvoiceModel?) {
        viewModel.clearStateClient()
        viewModel.clearStateInvoice()

        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().navigate(
            com.codeIn.myCash.ui.home.products.second_step_create_invoice.SecondStepCreationInvoiceFragmentDirections.actionNavigationSecondStepCreationInvoiceFragmentToSummaryInvoiceFragment(
                "${invoiceModel?.id ?: 0}"
            )
        )
//        findNavController().previousBackStackEntry?.savedStateHandle?.set(
//            "products_in_cart",
//            null)
//        findNavController().popBackStack()
//        findNavController().popBackStack()
    }

    private fun handleInputError(invalidInput: InvalidInput) {
        binding.loadingLayout.root.gone()
        when (invalidInput) {
            InvalidInput.CASH -> {
                binding.cashPaidAmountEditText.binding.editText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_cash_money),
                    isSuccess = false
                )
            }

            InvalidInput.VISA -> {
                binding.creditCardPaidAmountEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_visa_money),
                    isSuccess = false
                )

            }

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

    private fun handlePostPaid() {
        binding.apply {
            cashPaidAmountEditText.binding.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i2: Int,
                    i3: Int
                ) {
                    remainingTextView.text = NumberHelper.persianToEnglishText(
                        viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                    )
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    if (charSequence.isNotEmpty()) {
                        if (NumberHelper.persianToEnglishText(charSequence.toString() ?: "0.0")
                                .toDouble() <=
                            NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            ).toDouble()
                        ) {
                            remainingTextView.text =
                                NumberHelper.persianToEnglishText(
                                    (NumberHelper.persianToEnglishText(
                                        viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                                    ).toDouble()
                                            - NumberHelper.persianToEnglishText(
                                        charSequence.toString() ?: "0.0"
                                    ).toDouble()).toString()
                                )
                        } else {
                            remainingTextView.text = NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            )
                            cashPaidAmountEditText.binding.editText.setText("")
                        }
                    } else {
                        remainingTextView.text = NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                        )
                    }
                }

                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }

    private fun handleCashAndCard() {
        binding.apply {
            cashPaidAmountEditText.binding.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i2: Int,
                    i3: Int
                ) {
                    creditCardPaidAmountEditText.setText(
                        NumberHelper.persianToEnglishText(
                            viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                        )
                    )
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    if (charSequence.isNotEmpty()) {
                        if (NumberHelper.persianToEnglishText(charSequence.toString() ?: "0.0")
                                .toDouble() <=
                            NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            ).toDouble()
                        ) {
                            creditCardPaidAmountEditText.setText(
                                NumberHelper.persianToEnglishText(
                                    (NumberHelper.persianToEnglishText(
                                        viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                                    ).toDouble()
                                            - NumberHelper.persianToEnglishText(
                                        charSequence.toString() ?: "0.0"
                                    ).toDouble()).toString()
                                )
                            )
                        } else {
                            creditCardPaidAmountEditText.setText(
                                NumberHelper.persianToEnglishText(
                                    viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                                )
                            )
                            cashPaidAmountEditText.binding.editText.setText("")
                        }
                    } else {
                        creditCardPaidAmountEditText.setText(
                            NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            )
                        )
                    }
                }

                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }

    private fun handlePostPaidAndCard() {
        binding.apply {
            cashPaidAmountEditText.binding.editText.addTextChangedListener(object : TextWatcher {
                var visaValue = "0.0"
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i2: Int,
                    i3: Int
                ) {
                    if (creditCardPaidAmountEditText.text.isNotEmpty())
                        visaValue = creditCardPaidAmountEditText.text.toString()

                    remainingTextView.text =
                        NumberHelper.persianToEnglishText(
                            (NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            ).toDouble()
                                    - NumberHelper.persianToEnglishText(visaValue)
                                .toDouble()).toString()
                        )
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    Log.d("TAG", " trerrerererrere $charSequence")
                    if (charSequence.isNotEmpty()) {
                        if ((NumberHelper.persianToEnglishText(charSequence.toString() ?: "0.0")
                                .toDouble()) + visaValue.toDouble() <=
                            NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            ).toDouble()
                        ) {
                            remainingTextView.text =
                                NumberHelper.persianToEnglishText(
                                    (NumberHelper.persianToEnglishText(
                                        viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                                    ).toDouble()
                                            - (NumberHelper.persianToEnglishText(
                                        charSequence.toString() ?: "0.0"
                                    ).toDouble()
                                            + visaValue.toDouble())).toString()
                                )
                        } else {
                            remainingTextView.text = NumberHelper.persianToEnglishText(
                                (NumberHelper.persianToEnglishText(
                                    viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                                ).toDouble() - visaValue.toDouble()).toString()
                            )
                            cashPaidAmountEditText.binding.editText.setText("")
                        }
                    } else {
                        remainingTextView.text = NumberHelper.persianToEnglishText(
                            (NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            ).toDouble() - visaValue.toDouble()).toString()
                        )

                    }
                }

                override fun afterTextChanged(editable: Editable) {

                }
            })

            creditCardPaidAmountEditText.addTextChangedListener(object : TextWatcher {
                var cashValue = "0.0"
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i2: Int,
                    i3: Int
                ) {
                    if (cashPaidAmountEditText.text.isNotEmpty())
                        cashValue = cashPaidAmountEditText.text.toString()
                    else
                        remainingTextView.text =
                            NumberHelper.persianToEnglishText(
                                (NumberHelper.persianToEnglishText(
                                    viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                                ).toDouble()
                                        - NumberHelper.persianToEnglishText(cashValue)
                                    .toDouble()).toString()
                            )
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    if (charSequence.isNotEmpty()) {
                        if ((NumberHelper.persianToEnglishText(charSequence.toString() ?: "0.0")
                                .toDouble()) + cashValue.toDouble() <=
                            NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            ).toDouble()
                        ) {
                            remainingTextView.text =
                                NumberHelper.persianToEnglishText(
                                    (NumberHelper.persianToEnglishText(
                                        viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                                    ).toDouble()
                                            - (NumberHelper.persianToEnglishText(
                                        charSequence.toString() ?: "0.0"
                                    ).toDouble()
                                            + cashValue.toDouble())).toString()
                                )
                        } else {
                            remainingTextView.text = NumberHelper.persianToEnglishText(
                                (NumberHelper.persianToEnglishText(
                                    viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                                ).toDouble() - cashValue.toDouble()).toString()
                            )
                            creditCardPaidAmountEditText.setText("")
                        }
                    } else {
                        remainingTextView.text = NumberHelper.persianToEnglishText(
                            (NumberHelper.persianToEnglishText(
                                viewModel.selectedProductsInCart.value?.finalTotal ?: "0.0"
                            ).toDouble() - cashValue.toDouble()).toString()
                        )

                    }
                }

                override fun afterTextChanged(editable: Editable) {
//                    if (NumberHelper.persianToEnglishText(remainingTextView.text.toString()?:"0.0").toDouble() ==0.0
//                        && ){
//                        cash.setText("0.0")
//                    }
                }
            })

        }

    }

}