package com.codeIn.myCash.ui.home.invoices.invoice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentStatus
import com.codeIn.common.data.PaymentType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.printer.QR
import com.codeIn.common.printer.pdf_from_view_manager.PdfFromViewHandler
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoiceBinding
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsData
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.ui.handleInvoiceAction
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.myCash.utilities.myCashPrint
import com.codeIn.myCash.utilities.pdf_manager.PdfData
import com.codeIn.myCash.utilities.pdf_manager.PdfInvoiceInformation
import com.codeIn.myCash.utilities.pdf_manager.PdfInvoiceSummary
import com.codeIn.myCash.utilities.pdf_manager.PdfProductDetailsTotal
import com.codeIn.myCash.utilities.pdf_manager.PdfSellerInformation
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class InvoiceFragment : Fragment() {

    private val viewModel: InvoiceViewModel by viewModels()
    private var _binding: FragmentInvoiceBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var prefs: SharedPrefsModule

    @Inject
    lateinit var pdfFromViewHandler: PdfFromViewHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceBinding.inflate(inflater, container, false)
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
            printTextView.setOnClickListener {
                myCashPrint(requireContext(), binding.invoice)
            }

            shareTextView.setOnClickListener {
                pdfFromViewHandler.handleInvoiceAction(Intent.ACTION_SEND, binding.invoice)
            }

            pdfDownloadTextView.setOnClickListener {
                pdfFromViewHandler.handleInvoiceAction(Intent.ACTION_VIEW, binding.invoice)
            }

            qrCodeTextView.setOnClickListener {
                //TODO GENERATE QRCODE
            }
        }


        val adapter = ProductInInvoiceAdapter(requireContext())
        binding.products.adapter = adapter
        viewModel.apply {
            invoiceModel.observe(viewLifecycleOwner) {
                binding.loadingLayout.root.gone()
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
                    is InvoiceSettingsState.Idle -> {}
                    is InvoiceSettingsState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                    is InvoiceSettingsState.InvoiceSettingsSuccess -> {
                        binding.showFields(response.data)
                    }

                    is InvoiceSettingsState.InputError -> {}
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

                    is InvoiceState.SuccessShowSingleInvoice -> {
                        updateInvoiceModel(response.data)
                    }

                    is InvoiceState.InputError -> {}
                    else -> {}
                }
            }
        }
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        binding.loadingLayout.root.gone()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }


    private fun navigateTo(navDirections: NavDirections) {
        if (findNavController().currentDestination?.id == R.id.navigation_invoiceFragment) {
            findNavController().navigate(navDirections)
        }
    }

    private fun FragmentInvoiceBinding.bindSummaryInvoice(invoice: InvoiceModel?) {
        if (invoice?.paymentStatus == PaymentStatus.COMPLETED.value.toString()) {
            paymentStatusTextView.text = getString(R.string.payment_completed)
            paymentStatusTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_tick_circle_small,
                0,
                0,
                0
            )
            paymentStatusTextView.changeDrawableAndTextColors(
                color = getColor(
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
                color = getColor(
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

        invoice?.parentInvoice?.let { parent ->
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

        showNotesButton.isVisible = invoice?.hasInvoiceNotification == "1"
        invoiceReturnButton.isVisible =
            invoice?.isReturn != "1" && NumberHelper.persianToEnglishText(
                invoice?.totalPrice ?: "0.0"
            ).toDouble() > 0
        layoutMakeMemorandum.isVisible =
            invoiceReturnButton.isVisible && !invoice?.creditsProducts.isNullOrEmpty()
        popOver()

        findNavController().currentBackStackEntry?.savedStateHandle?.get<String>(Constants.showNewInvoiceButton())
            ?.let { newId ->
                showNewInvoice.visible()
                layoutMakeMemorandum.gone()
                viewModel.updateNewInvoiceId(newId)
            }
    }

    private fun FragmentInvoiceBinding.showFields(invoiceSetting: InvoiceSettingsData?) {

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

    private fun FragmentInvoiceBinding.handleActions(invoiceModel: InvoiceModel?) {

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

        showNotesButton.setOnClickListener {
            navigateTo(
                InvoiceFragmentDirections.actionNavigationInvoiceFragmentToNavigationCreditorDebtorNotesFragment(
                    viewModel.invoiceId.value.toString()
                )
            )
        }

        showNewInvoice.setOnClickListener {
            navigateTo(
                InvoiceFragmentDirections.actionNavigationInvoiceFragmentToInvoiceFragment(
                    viewModel.newInvoiceId.value.toString()
                )
            )
        }

        invoiceReturnButton.setOnClickListener {
            navigateTo(
                InvoiceFragmentDirections.actionNavigationInvoiceFragmentToNavigationInvoiceReturnFragment(
                    invoiceModel!!
                )
            )
        }

        creditorDebtorNoteButton.setOnClickListener {
            navigateTo(
                InvoiceFragmentDirections.actionNavigationInvoiceFragmentToNavigationCreditorDebtorNoteFragment(
                    invoiceModel!!
                )
            )
        }

        paymentStatusTextView.setOnClickListener {
            if (invoiceModel?.paymentStatus == PaymentStatus.UNCOMPLETED.value.toString())
                navigateTo(
                    InvoiceFragmentDirections.actionNavigationInvoiceFragmentToNavigationIncompletePaymentInvoiceFragment(
                        invoiceModel!!
                    )
                )
        }
        moreDetails.setOnClickListener {
            popOver()
        }

    }

    private fun FragmentInvoiceBinding.bindInfo(invoice: InvoiceModel?) {
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

    override fun onResume() {
        super.onResume()
        viewModel.getSingleInvoice()
    }

    private fun getMyPdfData(invoiceModel: InvoiceModel?): PdfData {
        val invoiceInformation = PdfInvoiceInformation(
            invoiceNo = "#${invoiceModel?.invoiceNumber}",
            dateTime = invoiceModel?.date,
            invoiceType = when (invoiceModel?.invoiceType) {
                InvoiceType.SIMPLE.value.toString() -> requireContext().getString(R.string.simple_invoice)
                InvoiceType.TAX.value.toString() -> requireContext().getString(R.string.tax_invoice)
                else -> ""
            }
        )

        val sellerInformation = PdfSellerInformation(
            sellerNameEn = invoiceModel?.cashierModel?.accountInfo?.commercialRecordName
                ?: getString(R.string.app_name),
            sellerNameAr = invoiceModel?.cashierModel?.accountInfo?.commercialRecordName
                ?: getString(R.string.app_name),
            sellerAddressEn = invoiceModel?.cashierModel?.mainBranch?.address ?: "----",
            sellerAddressAr = invoiceModel?.cashierModel?.mainBranch?.address ?: "----",
            sellerVatNoEn = invoiceModel?.cashierModel?.accountInfo?.taxRecord ?: "----",
            sellerVatNoAr = invoiceModel?.cashierModel?.accountInfo?.taxRecord ?: "----",
            crNoEn = "0201000",
            crNoAr = "0201000"
        )

        val list: List<ProductInInvoiceModel> = invoiceModel?.products!!
        val productDetailsTotal = PdfProductDetailsTotal(
            unitPriceTotal = "${invoiceModel?.productPrice} ${viewModel.currency}",
            quantityTotal = "0.0",
            discountTotal = "${invoiceModel?.discountPrice} ${viewModel.currency}",
            totalBeforeVatTotal = "0.0",
            vatTotal = "0.0",
            vatAmountTotal = "${invoiceModel?.taxPrice} ${viewModel.currency}",
            totalIncludingVatTotal = "${invoiceModel?.totalPrice} ${viewModel.currency}",
            details = list
        )

        val invoiceSummary = PdfInvoiceSummary(
            totalAmountBeforeVat = "0.0",
            totalVat = "${invoiceModel?.taxPrice} ${viewModel.currency}",
            totalAmountIncludingVat = "${invoiceModel?.totalPrice} ${viewModel.currency}",
            qrCode = "${invoiceModel?.qrZatca}"
        )

        return PdfData(
            invoiceInformation = invoiceInformation,
            sellerInformation = sellerInformation,
            productDetailsTotal = productDetailsTotal,
            invoiceSummary = invoiceSummary,
            qrCode = "https://cdn.pixabay.com/photo/2013/07/12/14/45/qr-code-148732_1280.png"
        )
    }

    private fun popOver() {
        viewModel.viewModelScope.launch {
            val balloon = Balloon.Builder(requireContext())
                .setWidthRatio(0.5f)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(getString(R.string.more_info_memorandum))
                .setTextColorResource(R.color.whiteText)
                .setTextSize(15f)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setCornerRadius(20f)
                .setMargin(16)
                .setBackgroundColor(requireContext().getColor(R.color.secondaryColor))
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .setLifecycleOwner(viewLifecycleOwner)
                .setArrowOrientation(ArrowOrientation.BOTTOM)
                .setIsVisibleArrow(true)
                .setAlpha(0.9f)
                .build()

            if (binding.layoutMakeMemorandum.visibility == View.VISIBLE && !balloon.isShowing) {
                balloon.showAlignBottom(binding.moreDetails)
            }

        }

    }

}