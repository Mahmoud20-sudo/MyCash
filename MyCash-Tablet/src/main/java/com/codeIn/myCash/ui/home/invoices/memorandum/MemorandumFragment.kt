
package com.codeIn.myCash.ui.home.invoices.memorandum

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MemorandumType
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentStatus
import com.codeIn.common.data.PaymentType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.printer.QR
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoiceBinding
import com.codeIn.myCash.databinding.FragmentPaymentSummaryBinding
import com.codeIn.myCash.ui.home.invoices.invoice.InvoiceFragmentDirections
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumModel
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsData
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MemorandumFragment : Fragment() {
    private val viewModel: MemorandumViewModel by viewModels()
    private var _binding: FragmentPaymentSummaryBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentSummaryBinding.inflate(inflater, container, false)
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
        }

        viewModel.apply {
            val adapter = ProductInInvoiceAdapter(requireContext())
            binding.products.adapter = adapter

            memorandumId.observe(viewLifecycleOwner){
                if (it != null){
                    getSingleMemorandum()
                }
            }

            memorandumModel.observe(viewLifecycleOwner) {
                binding.bindSummaryInvoice(it)
                binding.handleActions(it)
                binding.bindInfo(it)
                if (it?.products?.isNotEmpty() == true){
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
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
//                    is InvoiceSettingsState.StateError -> handleError(response.message.toString())
                    is InvoiceSettingsState.InvoiceSettingsSuccess -> {
                        binding.showFields(response.data)
                    }
                    is InvoiceSettingsState.InputError -> {}
//                    is InvoiceSettingsState.ServerError ->  handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }

            memorandumDataResult.collectOnLifecycle(
                viewModelScope,
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is MemorandumState.Loading -> handleLoading()
                    is MemorandumState.Idle -> handleIdle()
                    is MemorandumState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
//                    is MemorandumState.StateError -> handleError(response.message.toString())
                    is MemorandumState.SuccessShowSingleMemorandum -> {
                        updateMemorandumModel(response.data)
                    }
                    is MemorandumState.InputError -> {}
//                    is MemorandumState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
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

    private fun FragmentPaymentSummaryBinding.bindSummaryInvoice(memorandumModel: MemorandumModel?){

        paymentStatusTextView.text = getString(R.string.payment_completed)
        paymentStatusTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_tick_circle_small,
            0,
            0,
            0
        )
        paymentStatusTextView.changeDrawableAndTextColors(color = ContextCompat.getColor(
            requireContext(),
            R.color.secondaryColor))

        invoiceNumber.text = "#${memorandumModel?.id}"
        invoiceNumberSummary.text = "#${memorandumModel?.id}"

        initialTotal.text = "${memorandumModel?.price} ${viewModel.currency}"
        initialTotalSummary.text = "${memorandumModel?.price} ${viewModel.currency}"

        addedTaxView.text = "${getString(R.string.added_tax)} (${memorandumModel?.invoiceModel?._tax}%)"
        addedTaxSummaryView.text = "${getString(R.string.added_tax)} (${memorandumModel?.invoiceModel?._tax}%)"

        taxValue.text = "${memorandumModel?.taxPrice} ${viewModel.currency}"
        taxValueSummary.text = "${memorandumModel?.taxPrice} ${viewModel.currency}"


        when(memorandumModel?.paymentType){
            PaymentType.CASH.value.toString() -> {
                paymentMethod.text = getString(R.string.cash)
                paymentMethodSummary.text = getString(R.string.cash)
            }
            PaymentType.CREDIT_CARD.value.toString() -> {
                paymentMethod.text = getString(R.string.credit_card)
                paymentMethodSummary.text = getString(R.string.credit_card)
            }
            PaymentType.CASH_AND_CREDIT_CARD.value.toString() -> {
                paymentMethod.text = getString(R.string.cash_and_credit_card)
                paymentMethodSummary.text = getString(R.string.cash_and_credit_card)

                firstPaymentContainer.visible()
                firstPaymentContainerSummary.visible()
                secondPaymentContainer.visible()
                secondPaymentContainerSummary.visible()

                valueFirstPayment.text = "${memorandumModel?.cash} ${viewModel.currency}"
                valueFirstPaymentSummary.text = "${memorandumModel?.cash} ${viewModel.currency}"

                valueSecondPayment.text = "${memorandumModel?.visa} ${viewModel.currency}"
                valueSecondPaymentSummary.text = "${memorandumModel?.visa} ${viewModel.currency}"
            }
        }

        finalTotal.text = "${memorandumModel?.totalPrice} ${viewModel.currency}"
        total.text = "${memorandumModel?.totalPrice} ${viewModel.currency}"
        totalSummary.text = "${memorandumModel?.totalPrice} ${viewModel.currency}"

    }

    private fun FragmentPaymentSummaryBinding.showFields(invoiceSetting: InvoiceSettingsData?){

        if (invoiceSetting?.name == "1")
            nameStore.visible()

        if (invoiceSetting?.taxRecord == "1")
            taxNumberContainer.visible()


        if (invoiceSetting?.tax == "1") {
            taxContainer.visible()
            taxContainerSummary.visible()
        }

        when(invoiceSetting?.active){
            InvoiceType.SIMPLE.value ->{
                if (invoiceSetting?.simpleInvoice?.client == "1")
                    clientInfoContainer.visible()

                if (invoiceSetting?.simpleInvoice?.cashier == "1")
                    cashierContainer.visible()

                if (invoiceSetting?.simpleInvoice?.zatcaQr == "1")
                    zatcaQrCodeContainer.visible()

                if (invoiceSetting?.simpleInvoice?.myCashQr == "1")
                    myCashQrCodeContainer.visible()

                footer.text = invoiceSetting?.simpleInvoice?.footerText ?:"----"
            }

            InvoiceType.TAX.value ->{
                if (invoiceSetting?.taxInvoice?.client == "1")
                    clientInfoContainer.visible()

                if (invoiceSetting?.taxInvoice?.cashier == "1")
                    cashierContainer.visible()

                if (invoiceSetting?.taxInvoice?.zatcaQr == "1")
                    zatcaQrCodeContainer.visible()

                if (invoiceSetting?.taxInvoice?.myCashQr == "1")
                    myCashQrCodeContainer.visible()

                footer.text = invoiceSetting?.taxInvoice?.footerText ?:"----"
            }
        }
    }

    private fun FragmentPaymentSummaryBinding.handleActions(memorandumModel: MemorandumModel?){

        invoice.setOnClickListener {
            if (invoiceSummaryLayout.isVisible){
                MyAnimator().animateTranslationYHide(
                    view = invoiceSummaryLayout,
                    duration = 100L
                )

                MyAnimator().animateTranslationYShow(
                    view = actionsLayout,
                    duration = 100L
                )
            }
            else if (actionsLayout.isVisible){
                MyAnimator().animateTranslationYHide(
                    view = actionsLayout,
                    duration = 100L
                )

                MyAnimator().animateTranslationYShow(
                    view = invoiceSummaryLayout,
                    duration = 100L
                )
            }

            invoiceContainer.scrollTo(0 , 0)
        }
    }

    private fun FragmentPaymentSummaryBinding.bindInfo(memorandumModel: MemorandumModel?){
        when(memorandumModel?.type){
            MemorandumType.CREDITOR.value -> typeInvoice.text = getString(R.string.creditor_note)
            MemorandumType.DEBTOR.value -> typeInvoice.text = getString(R.string.debtor_note)
        }

        val dateLength = memorandumModel?.date?.length?:0
        if (dateLength >= 11)
        {
            date.text = memorandumModel?.date?.substring(0 , 10)
            time.text = memorandumModel?.date?.substring(11 , memorandumModel.date?.length?:0)
        }


        Glide.with(requireContext())
            .load(memorandumModel?.invoiceModel?.cashierModel?.accountInfo?.logo)
            .error(R.drawable.icon_app)
            .into(logoStore)


        taxNumber.text = memorandumModel?.invoiceModel?.cashierModel?.accountInfo?.taxRecord?:"----"
        deviceNumber.text = memorandumModel?.invoiceModel?.cashierModel?.subscription?.device?.device?.name?:"----"
        nameStore.text = memorandumModel?.invoiceModel?.cashierModel?.accountInfo?.commercialRecordName?:getString(R.string.app_name)
        branchName.text = memorandumModel?.invoiceModel?.shift?.branch?.name?:"----"
        branchAddress.text = memorandumModel?.invoiceModel?.shift?.branch?.address?:"----"
        clientName.text = memorandumModel?.invoiceModel?.client?.name?:"----"
        cashierName.text = memorandumModel?.invoiceModel?.cashierModel?.name?:"----"
        thanks.text = "${getString(R.string.thanks)} ${nameStore.text.toString()}"

        if (!memorandumModel?.invoiceModel?.qrZatca.isNullOrEmpty())
        {
            val bitmapZatcaQr = QR.generateQrCode(memorandumModel?.invoiceModel?.qrZatca?:"" , requireContext())
            zatcaQrCode.setImageBitmap(bitmapZatcaQr)
        }

        val bitmapMyCash = QR.generateQrCode(memorandumModel?.id.toString(), requireContext())
        myCashQrCode.setImageBitmap(bitmapMyCash)
    }

}