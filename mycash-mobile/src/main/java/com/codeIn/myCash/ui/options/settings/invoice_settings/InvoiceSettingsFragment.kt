package com.codeIn.myCash.ui.options.settings.invoice_settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.Shift
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoiceSettingsBinding
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettings
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsData
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsState
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.ui.home.shifts_dialogs.EndShiftDialog
import com.codeIn.myCash.ui.home.shifts_dialogs.ShiftListener
import com.codeIn.myCash.ui.home.shifts_dialogs.StartShiftDialog
import com.codeIn.myCash.ui.options.settings.invoice_settings.dialog.OrderNoListener
import com.codeIn.myCash.ui.options.settings.invoice_settings.dialog.StartPurchaseInvoiceOrderNoDialog
import com.codeIn.myCash.ui.options.settings.invoice_settings.dialog.StartSaleInvoiceOrderNoDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class InvoiceSettingsFragment : Fragment() {

    private val viewModel: InvoiceSettingsViewModel by viewModels()

    private var _binding: FragmentInvoiceSettingsBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()
    private var invoiceSettings: InvoiceSettings? = null

    @Inject
    lateinit var prefs: SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.apply {
            simpleInvoiceTextView.setOnClickListener {
                invoiceSettings = viewModel.dataSimpleInvoiceSettings.value
                viewModel.updateInvoiceType(
                    InvoiceType.SIMPLE.value,
                    simple = viewModel.dataSimpleInvoiceSettings.value,
                    tax = viewModel.dataTaxInvoiceSettings.value
                )
                updateInvoiceType(true)
            }

            taxInvoiceTextView.setOnClickListener {
                invoiceSettings = viewModel.dataTaxInvoiceSettings.value
                viewModel.updateInvoiceType(
                    InvoiceType.TAX.value,
                    simple = viewModel.dataSimpleInvoiceSettings.value,
                    tax = viewModel.dataTaxInvoiceSettings.value
                )
                updateInvoiceType(false)
            }

            saveButton.setOnClickListener {
                val productDescription =
                    if (binding.productDescriptionSwitch.isChecked) "1" else "0"
                val clientsInfo = if (binding.clientsInfoSwitch.isChecked) "1" else "0"
                val cashierInfo = if (binding.cashierInfoSwitch.isChecked) "1" else "0"
                val zatcaQr = if (binding.zatcaQrSwitch.isChecked) "1" else "0"
                val myCashQr = if (binding.myCashQrSwitch.isChecked) "1" else "0"

                val nameCommercial =
                    if (binding.commercialRegistryNameSwitch.isChecked) "1" else "0"
                val numberCommercial =
                    if (binding.commercialRegistrationNoSwitch.isChecked) "1" else "0"
                val taxRegistration =
                    if (binding.taxRegistrationNumberSwitch.isChecked) "1" else "0"
                val tax = if (binding.vat15Switch.isChecked) "1" else "0"

                viewModel.updateInvoiceSettings(
                    commercialName = nameCommercial,
                    commercialNumber = numberCommercial,
                    taxRegistrationNumber = taxRegistration,
                    tax = tax,
                    footer = binding.invoiceFooterMessageEditText.text.toString(),
                    productDescription = productDescription,
                    clients = clientsInfo,
                    cashier = cashierInfo,
                    zatcaQr = zatcaQr,
                    myCashQr = myCashQr,
                    type = viewModel.activeInvoiceType.value.toString(),
                    active = "1"
                )

            }

            startSaleInvoiceOrderNo.setOnClickListener {
                showStartSaleInvoiceDialog()
            }
            startPurchaseInvoiceOrderNo.setOnClickListener {
                showStartPurchaseInvoiceDialog()
            }

            productDescriptionSwitch.setOnCheckedChangeListener { _, b ->
                invoiceSettings?.productDesc = if (b) "1" else "0"
            }

            clientsInfoSwitch.setOnCheckedChangeListener { _, b ->
                invoiceSettings?.client = if (b) "1" else "0"
            }

            cashierInfoSwitch.setOnCheckedChangeListener { _, b ->
                invoiceSettings?.cashier = if (b) "1" else "0"
            }

            zatcaQrSwitch.setOnCheckedChangeListener { _, b ->
                invoiceSettings?.zatcaQr = if (b) "1" else "0"
            }

            myCashQrSwitch.setOnCheckedChangeListener { _, b ->
                invoiceSettings?.myCashQr = if (b) "1" else "0"
            }

            invoiceFooterMessageEditText.doAfterTextChanged {
                invoiceSettings?.footerText = it.toString()
            }
        }


        viewModel.apply {
            dataResult.observe(
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is InvoiceSettingsState.Loading -> handleLoading()
                    is InvoiceSettingsState.Idle -> handleIdle()
                    is InvoiceSettingsState.StateError -> {}
                    is InvoiceSettingsState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                    is InvoiceSettingsState.InvoiceSettingsSuccess -> {
                        val type = response.data?.active == 1
                        var result = 2
                        if (type)
                            result = 1
                        viewModel.updateInvoiceType(
                            result,
                            response.data?.simpleInvoice,
                            response.data?.taxInvoice
                        )
                        binding.bindSystemSettings(response.data)
                        updateInvoiceType(type)
                        clearState()
                    }

                    is InvoiceSettingsState.InvoiceTypeSuccess -> {
                        handleSuccess(response?.message)
                    }

                    is InvoiceSettingsState.InputError -> {
                        if (response.inputError == InvalidInput.EMPTY) {
                            CustomToaster.show(
                                requireContext(),
                                getString(R.string.please_fill_all_the_fields),
                                isSuccess = false
                            )
                        }
                    }

                    is InvoiceSettingsState.ServerError -> { handleIdle() }
                    is InvoiceSettingsState.OrderNoInvoiceSuccess -> {
                        if (dialogStartSaleInvoiceOrderNoDialog.isInitialized()) {
                            if (dialogStartSaleInvoiceOrderNoDialog.value.isShowing)
                                dismissStartSaleInvoiceDialog()
                        }
                        if (dialogPurchaseInvoiceOrderNoDialog.isInitialized()) {
                            if (dialogPurchaseInvoiceOrderNoDialog.value.isShowing)
                                dismissStartPurchaseInvoiceDialog()
                        }
                        viewModel.clearState()
                    }

                    else -> {}
                }
            }

            dataActiveInvoiceSettings.observe(viewLifecycleOwner) {
                binding.bindSettings(it)
            }
        }
    }

    private fun FragmentInvoiceSettingsBinding.bindSystemSettings(settings: InvoiceSettingsData?) {
        commercialRegistryNameSwitch.isChecked = settings?.name == "1"
        commercialRegistrationNoSwitch.isChecked = settings?.commercialRecord == "1"
        taxRegistrationNumberSwitch.isChecked = settings?.taxRecord == "1"
        vat15Switch.isChecked = settings?.tax == "1"
        commercialRegistryNameTextView.text = settings?.accountInfo?.commercialRecordName
        commercialRegistrationNoTextView.text = settings?.accountInfo?.commercialRecord
        taxRegistrationNumberTextView.text = settings?.accountInfo?.taxRecord
        vat15TextView.text = settings?.accountInfo?.tax + "%"
    }

    private fun FragmentInvoiceSettingsBinding.bindSettings(settings: InvoiceSettings?) {
        invoiceFooterMessageEditText.setText(invoiceSettings?.footerText ?: settings?.footerText)
        productDescriptionSwitch.isChecked =
            (invoiceSettings?.productDesc ?: settings?.productDesc) == "1"
        clientsInfoSwitch.isChecked = (invoiceSettings?.client ?: settings?.client) == "1"
        cashierInfoSwitch.isChecked = (invoiceSettings?.cashier ?: settings?.cashier) == "1"
        zatcaQrSwitch.isChecked = (invoiceSettings?.zatcaQr ?: settings?.zatcaQr) == "1"
        myCashQrSwitch.isChecked = (invoiceSettings?.myCashQr ?: settings?.myCashQr) == "1"
    }

    private fun updateInvoiceType(isSimpleInvoiceType: Boolean) {
        // get the background and text colors from the resources
        val primaryBackground = R.drawable.bg_white_r100_s1_secondary_ripple
        val secondaryBackground = R.drawable.bg_white_r100_s1_stroke_ripple

        val primaryTextColor = ContextCompat.getColor(requireContext(), R.color.primaryColor)
        val secondaryTextColor = ContextCompat.getColor(requireContext(), R.color.mainBlack)

        // update the ui based on the invoice type
        if (isSimpleInvoiceType) {
            binding.simpleInvoiceTextView.apply {
                setBackgroundResource(primaryBackground)
                setTextColor(primaryTextColor)
            }

            binding.taxInvoiceTextView.apply {
                setBackgroundResource(secondaryBackground)
                setTextColor(secondaryTextColor)
            }
        } else {
            binding.taxInvoiceTextView.apply {
                setBackgroundResource(primaryBackground)
                setTextColor(primaryTextColor)
            }

            binding.simpleInvoiceTextView.apply {
                setBackgroundResource(secondaryBackground)
                setTextColor(secondaryTextColor)
            }
        }
    }

    private fun handleSuccess(message: String?) {
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = message ?: getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().popBackStack()
    }

    private fun showStartSaleInvoiceDialog() {
        if (!dialogStartSaleInvoiceOrderNoDialog.value.isShowing)
            dialogStartSaleInvoiceOrderNoDialog.value.show()
        dialogStartSaleInvoiceOrderNoDialog.value.setCancelable(true)
        dialogStartSaleInvoiceOrderNoDialog.value.setCanceledOnTouchOutside(true)
    }

    private fun dismissStartSaleInvoiceDialog() {
        if (dialogStartSaleInvoiceOrderNoDialog.value.isShowing)
            dialogStartSaleInvoiceOrderNoDialog.value.dismiss()

        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.successful_operation),
            isSuccess = true
        )
    }

    private fun showStartPurchaseInvoiceDialog() {
        if (!dialogPurchaseInvoiceOrderNoDialog.value.isShowing)
            dialogPurchaseInvoiceOrderNoDialog.value.show()
        dialogPurchaseInvoiceOrderNoDialog.value.setCancelable(true)
        dialogPurchaseInvoiceOrderNoDialog.value.setCanceledOnTouchOutside(true)
    }

    private fun dismissStartPurchaseInvoiceDialog() {
        if (dialogPurchaseInvoiceOrderNoDialog.value.isShowing)
            dialogPurchaseInvoiceOrderNoDialog.value.dismiss()

        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.successful_operation),
            isSuccess = true
        )
    }

    private val dialogStartSaleInvoiceOrderNoDialog = lazy {
        StartSaleInvoiceOrderNoDialog(
            context = requireContext(),
            listener = listenerStartOrderNo
        )
    }
    private val dialogPurchaseInvoiceOrderNoDialog = lazy {
        StartPurchaseInvoiceOrderNoDialog(
            context = requireContext(),
            listener = listenerStartOrderNo
        )
    }
    private val listenerStartOrderNo = object : OrderNoListener {

        override fun startSaleInvoiceOrder(orderNo: String?) {
            viewModel.startSaleInvoiceOrderNo(orderNo)
        }

        override fun startPurchaseInvoiceOrder(orderNo: String?) {
            viewModel.startPurchaseInvoiceOrderNo(orderNo)
        }
    }

    private fun handleLoading() = binding.animationView.visible()

    private fun handleIdle() = binding.animationView.gone()
}

