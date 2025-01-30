package com.codeIn.myCash.ui.home.products.summaryinvoice

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentStatus
import com.codeIn.common.data.PaymentType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker.Companion.checkIfPOS
import com.codeIn.common.printer.QR
import com.codeIn.common.printer.pdf_from_view_manager.PdfFromViewHandler
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.BuildConfig
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoiceBinding
import com.codeIn.myCash.databinding.FragmentSummaryInvoiceBinding
import com.codeIn.myCash.ui.home.invoices.invoice.InvoiceViewModel
import com.codeIn.myCash.ui.home.invoices.invoice.ProductInInvoiceAdapter
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsData
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codein.common.printer.PrintUtils.Companion.getBitmapFromView
import com.codein.common.printer.marvel.ConnectionWithUsb
import com.pax.api.PrintManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SummaryInvoiceFragment : Fragment() {
    private val viewModel: InvoiceViewModel by viewModels()
    private var _binding: FragmentSummaryInvoiceBinding? = null
    private val infoDialog: InfoDialog = InfoDialog()
    private val binding get() = _binding!!

    @Inject
    lateinit var prefs: SharedPrefsModule

    @Inject
    lateinit var pdfFromViewHandler: PdfFromViewHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        requireActivity().onBackPressedDispatcher.addCallback() {
//            findNavController().previousBackStackEntry?.savedStateHandle?.set(
//                "products_in_cart",
//                null
//            )
//            findNavController().popBackStack()
//            findNavController().popBackStack()
//            findNavController().popBackStack()
//        }
        binding.apply {
            backArrow.setOnClickListener {
                handleBackNavigation()
            }
            anotherOperationsButton.setOnClickListener {
                handleBackNavigation()
            }
            printTextView.setOnClickListener {
                if (checkIfPOS()) {
                    kotlin.runCatching {
                        val printManager = PrintManager.getInstance(requireContext())
                        printManager.prnInit();
                        val bitmap = getBitmapFromView(binding.invoice)
                        printManager.prnBitmap(bitmap);
                        printManager.prnStep(400);
                        printManager.prnStart()
                    }.getOrElse { Timber.tag("Printing Status").e("FAILED") }
                    return@setOnClickListener
                }
                val printer = ConnectionWithUsb()
                printer.print(requireContext(), binding.invoice)

            }


            shareTextView.setOnClickListener {
                handleInvoiceAction(Intent.ACTION_SEND)
            }

            pdfDownloadTextView.setOnClickListener {
                handleInvoiceAction(Intent.ACTION_VIEW)
            }

            qrCodeTextView.setOnClickListener {
                //TODO GENERATE QRCODE
            }
        }

        val adapter = ProductInInvoiceAdapter(requireContext())
        binding.products.adapter = adapter


        viewModel.apply {

            invoiceId.observe(viewLifecycleOwner) {
                getSingleInvoice()
            }
            invoiceModel.observe(viewLifecycleOwner) {
                binding.bindSummaryInvoice(it)
                binding.handleActions(it)
                binding.bindInfo(it)
                if (it?.products?.isNotEmpty() == true) {
                    binding.products.visible()
                    adapter.submitList(it?.products)
                }
            }

            invoiceSettingsDataResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is InvoiceSettingsState.Loading -> handleLoading()
                    is InvoiceSettingsState.Idle -> handleIdle()
                    is InvoiceSettingsState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

//                    is InvoiceSettingsState.StateError -> handleError(response.message.toString())
                    is InvoiceSettingsState.InvoiceSettingsSuccess -> {
                        binding.showFields(response.data)
                        viewModel.clearState()
                    }

                    is InvoiceSettingsState.InputError -> {}
//                    is InvoiceSettingsState.ServerError -> handleError(
//                        response.error.getErrorMessage(
//                            requireContext()
//                        )
//                    )

                    else -> {}
                }
            }

            invoiceDataResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is InvoiceState.Loading -> handleLoading()
                    is InvoiceState.Idle -> handleIdle()
                    is InvoiceState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is InvoiceState.SuccessShowSingleInvoice -> updateInvoiceModel(response.data)

                    is InvoiceState.InputError -> {}
                    is InvoiceState.ServerError -> handleError(
                        response.error.getErrorMessage(
                            requireContext()
                        )
                    )
                    is InvoiceState.StateError -> handleError(response.message.toString())

                    else -> {}
                }
            }
        }
    }


    private fun handleInvoiceAction(action: String) =
        pdfFromViewHandler.setInvoiceActions( action, BuildConfig.APPLICATION_ID,binding.invoice )

    private fun handleBackNavigation() = findNavController().apply {
        previousBackStackEntry?.savedStateHandle?.set("products_in_cart", null)
        navigate(R.id.products_host_navigation,null, NavOptions.Builder().setPopUpTo(findNavController().graph.startDestinationId, true).build())
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        binding.loadingLayout.root.gone()
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    private fun FragmentSummaryInvoiceBinding.bindSummaryInvoice(invoice: InvoiceModel?) {
        if (invoice?.paymentStatus == PaymentStatus.COMPLETED.value.toString()) {
            paymentStatusTextView.text = getString(R.string.payment_completed)
            paymentStatusTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_tick_circle_small,
                0,
                0,
                0
            )
            paymentStatusTextView.changeDrawableAndTextColors(
                color = ContextCompat.getColor(
                    requireContext(),
                    R.color.secondaryColor
                )
            )
        } else {
            paymentStatusTextView.text = getString(R.string.payment_uncompleted)
            paymentStatusTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_arrow_circle,
                0
            )
            paymentStatusTextView.changeDrawableAndTextColors(
                color = ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        }

        invoiceNumber.text = "#${invoice?.invoiceNumber}"
        invoiceNumberSummary.text = "#${invoice?.invoiceNumber}"

        initialTotal.text = "${invoice?.productPrice} ${viewModel.currency}"
        initialTotalSummary.text = "${invoice?.productPrice} ${viewModel.currency}"

        discount.text = "${invoice?.discountPrice} ${viewModel.currency}"
        discountSummary.text = "${invoice?.discountPrice} ${viewModel.currency}"

        addedTaxView.text = "${getString(R.string.added_tax)} (${invoice?._tax}%)"
        addedTaxSummaryView.text = "${getString(R.string.added_tax)} (${invoice?._tax}%)"

        taxValue.text = "${invoice?.taxPrice} ${viewModel.currency}"
        taxValueSummary.text = "${invoice?.taxPrice} ${viewModel.currency}"

        orderNo.text = "${invoice?.invoiceOrder}"
        orderNoSummary.text = "${invoice?.invoiceOrder}"


        when (invoice?.paymentType) {
            PaymentType.CASH.value.toString() -> {
                paymentMethod.text = getString(R.string.cash)
                paymentMethodSummary.text = getString(R.string.cash)
            }

            PaymentType.CREDIT_CARD.value.toString() -> {
                paymentMethod.text = getString(R.string.credit_card)
                paymentMethodSummary.text = getString(R.string.credit_card)
            }

            PaymentType.POST_PAID.value.toString() -> {
                paymentMethod.text = getString(R.string.postpaid)
                paymentMethodSummary.text = getString(R.string.postpaid)

                firstPaymentContainer.visible()
                firstPaymentContainerSummary.visible()
                remainingContainer.visible()
                remainingContainerSummary.visible()

                valueFirstPayment.text = "${invoice.cashPrice} ${viewModel.currency}"
                valueFirstPaymentSummary.text = "${invoice.cashPrice} ${viewModel.currency}"

                remaining.text = "${invoice.rammingPrice} ${viewModel.currency}"
                remainingAmountSummary.text = "${invoice.rammingPrice} ${viewModel.currency}"

            }

            PaymentType.CASH_AND_CREDIT_CARD.value.toString() -> {
                paymentMethod.text = getString(R.string.cash_and_credit_card)
                paymentMethodSummary.text = getString(R.string.cash_and_credit_card)

                firstPaymentContainer.visible()
                firstPaymentContainerSummary.visible()
                secondPaymentContainer.visible()
                secondPaymentContainerSummary.visible()

                valueFirstPayment.text = "${invoice.cashPrice} ${viewModel.currency}"
                valueFirstPaymentSummary.text = "${invoice.cashPrice} ${viewModel.currency}"

                valueSecondPayment.text = "${invoice.visaPrice} ${viewModel.currency}"
                valueSecondPaymentSummary.text = "${invoice.visaPrice} ${viewModel.currency}"
            }

            PaymentType.POST_PAID_AND_CREDIT_CARD.value.toString() -> {
                paymentMethod.text = getString(R.string.postpaid_and_credit_card)
                paymentMethodSummary.text = getString(R.string.postpaid_and_credit_card)

                firstPaymentContainer.visible()
                firstPaymentContainerSummary.visible()
                secondPaymentContainer.visible()
                secondPaymentContainerSummary.visible()
                remainingContainer.visible()
                remainingContainerSummary.visible()

                valueFirstPayment.text = "${invoice.cashPrice} ${viewModel.currency}"
                valueFirstPaymentSummary.text = "${invoice.cashPrice} ${viewModel.currency}"

                valueSecondPayment.text = "${invoice.visaPrice} ${viewModel.currency}"
                valueSecondPaymentSummary.text = "${invoice.visaPrice} ${viewModel.currency}"

                remaining.text = "${invoice.rammingPrice} ${viewModel.currency}"
                remainingAmountSummary.text = "${invoice.rammingPrice} ${viewModel.currency}"
            }
        }

        invoice?.runRefund?.let {
            rnnContainer.visible()
            rnn.text = it
        }

        invoice?.parentInvoice?.let { parent->
            returnContainer.visible()
            returnedInvoiceNoContainer.visible()
            returnTv.text = "#${parent.invoiceNumber.toString()}"
            returnedInvoiceNo.text = "#${parent.invoiceNumber.toString()}"
            parent.returnedAmount?.let {
                returnedAmountContainer.visible()
                returnedAmount.text = "$it ${viewModel.currency}"
            }
        }

        finalTotal.text = "${invoice?.totalPrice} ${viewModel.currency}"
        total.text = "${invoice?.totalPrice} ${viewModel.currency}"
        totalSummary.text = "${invoice?.totalPrice} ${viewModel.currency}"
    }

    private fun FragmentSummaryInvoiceBinding.showFields(invoiceSetting: InvoiceSettingsData?) {

        if (invoiceSetting?.name == "1")
            nameStore.visible()

        if (invoiceSetting?.taxRecord == "1")
            taxNumberContainer.visible()


        if (invoiceSetting?.tax == "1") {
            taxContainer.visible()
            taxContainerSummary.visible()
        }

        when (invoiceSetting?.active) {
            InvoiceType.SIMPLE.value -> {
                if (invoiceSetting?.simpleInvoice?.client == "1")
                    clientInfoContainer.visible()

                if (invoiceSetting?.simpleInvoice?.cashier == "1")
                    cashierContainer.visible()

                if (invoiceSetting?.simpleInvoice?.zatcaQr == "1")
                    zatcaQrCodeContainer.visible()

                if (invoiceSetting?.simpleInvoice?.myCashQr == "1")
                    myCashQrCodeContainer.visible()

                footer.text = invoiceSetting?.simpleInvoice?.footerText ?: "----"
            }

            InvoiceType.TAX.value -> {
                if (invoiceSetting?.taxInvoice?.client == "1")
                    clientInfoContainer.visible()

                if (invoiceSetting?.taxInvoice?.cashier == "1")
                    cashierContainer.visible()

                if (invoiceSetting?.taxInvoice?.zatcaQr == "1")
                    zatcaQrCodeContainer.visible()

                if (invoiceSetting?.taxInvoice?.myCashQr == "1")
                    myCashQrCodeContainer.visible()

                footer.text = invoiceSetting?.taxInvoice?.footerText ?: "----"
            }
        }
    }

    private fun FragmentSummaryInvoiceBinding.handleActions(invoiceModel: InvoiceModel?) {

        invoice.setOnClickListener {
            if (invoiceSummaryLayout.isVisible) {
                MyAnimator().animateTranslationYHide(
                    view = invoiceSummaryLayout,
                    duration = 100L
                )

                MyAnimator().animateTranslationYShow(
                    view = actionsLayout,
                    duration = 100L
                )
            } else if (actionsLayout.isVisible) {
                MyAnimator().animateTranslationYHide(
                    view = actionsLayout,
                    duration = 100L
                )

                MyAnimator().animateTranslationYShow(
                    view = invoiceSummaryLayout,
                    duration = 100L
                )
            }

            invoiceContainer.scrollTo(0, 0)
        }

        paymentStatusTextView.setOnClickListener {
            if (invoiceModel?.paymentStatus == PaymentStatus.UNCOMPLETED.value.toString())
                navigateTo(
                    com.codeIn.myCash.ui.home.products.summaryinvoice.SummaryInvoiceFragmentDirections.actionSummaryInvoiceFragmentToNavigationIncompletePaymentInvoiceFragment(
                        invoiceModel!!
                    )
                )
        }

    }

    private fun navigateTo(navDirections: NavDirections) {
        if (findNavController().currentDestination?.id == R.id.summaryInvoiceFragment) {
            findNavController().navigate(navDirections)
        }
    }

    private fun FragmentSummaryInvoiceBinding.bindInfo(invoice: InvoiceModel?) {
        when (invoice?.invoiceType) {
            InvoiceType.SIMPLE.value.toString() -> typeInvoice.text =
                getString(R.string.simple_invoice)

            InvoiceType.TAX.value.toString() -> typeInvoice.text = getString(R.string.tax_invoice)
        }

        val dateLength = invoice?.date?.length ?: 0
        if (dateLength >= 11) {
            date.text = invoice?.date?.substring(0, 10)
            time.text = invoice?.date?.substring(11, invoice.date?.length ?: 0)
        }

        //todo load valid company logo
//        Glide.with(requireContext())
//            .load(invoice?.cashierModel?.accountInfo?.logo)
//            .error(R.drawable.icon_app)
//            .into(logoStore)


        taxNumber.text = invoice?.cashierModel?.accountInfo?.taxRecord ?: "----"
        deviceNumber.text = invoice?.cashierModel?.subscription?.device?.device?.name ?: "----"
        nameStore.text =
            invoice?.cashierModel?.accountInfo?.commercialRecordName ?: getString(R.string.app_name)
        branchName.text = invoice?.shift?.branch?.name ?: "----"
        branchAddress.text = invoice?.shift?.branch?.address ?: "----"
        clientName.text = invoice?.client?.name ?: "----"
        cashierName.text = invoice?.cashierModel?.name ?: "----"
        thanks.text = "${getString(R.string.thanks)} ${nameStore.text.toString()}"

        if (!invoice?.qrZatca.isNullOrEmpty()) {
            val bitmapZatcaQr = QR.generateQrCode(invoice?.qrZatca ?: "", requireContext())
            zatcaQrCode.setImageBitmap(bitmapZatcaQr)
        }

        val bitmapMyCash = QR.generateQrCode(invoice?.id.toString(), requireContext())
        myCashQrCode.setImageBitmap(bitmapMyCash)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}