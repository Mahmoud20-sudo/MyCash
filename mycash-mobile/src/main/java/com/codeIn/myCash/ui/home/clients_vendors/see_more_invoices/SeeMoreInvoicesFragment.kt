package com.codeIn.myCash.ui.home.clients_vendors.see_more_invoices

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.InvoiceFilter.*
import com.codeIn.common.data.Request
import com.codeIn.common.nearpay.NearPayPayment
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker
import com.codeIn.common.scanner.QRCodeScannerActivity
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.gone
import com.codeIn.common.util.resetScrollState
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoicesBinding
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.ui.home.invoices.invoices.InvoicesAdapter
import com.codeIn.myCash.ui.home.invoices.invoices.InvoicesFragmentDirections
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.pickers.DatePickerBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import io.nearpay.sdk.data.models.ReconciliationReceipt
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.ReconcileFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData
import javax.inject.Inject


@AndroidEntryPoint
class SeeMoreInvoicesFragment : Fragment(), View.OnClickListener{
    private val viewModel: SeeMoreInvoicesViewModel by viewModels()
    private var _binding: FragmentInvoicesBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()
    private val checker = PermissionChecker()
    private lateinit var adapter: InvoicesAdapter

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = InvoicesAdapter(requireContext(), invoicesCommunicator, viewModel.currency)

        //set the click listeners for the main section and filter
        binding.apply {
            header.visible()
            allTextView.tag = ALL
            taxInvoiceTextView.tag = TAX_INVOICE
            simpleInvoiceTextView.tag = SIMPLE_INVOICE
            instantInvoicesTextView.tag = INSTANT_INVOICE
            purchaseInvoiceTextView.tag = PURCHASE_INVOICE
            saleInvoiceTextView.tag = SALE_INVOICE
            purchaseReturnedTextView.tag = PURCHASE_RETURNED
            salesReturnedTextView.tag = SALES_RETURNED
            paymentCompletedTextView.tag = PAYMENT_COMPLETED
            paymentUncompletedTextView.tag = PAYMENT_NOT_COMPLETED

            recycleView.binding.rv.adapter = adapter
            recycleView.binding.rv.itemAnimator = null
        }

        initListeners()

        //observe the live vendorsData and update the views accordingly
        viewModel.apply {

            invoices.observe(viewLifecycleOwner) { invoices ->
                if (invoices.isNotEmpty())
                    adapter.submitList(invoices)
            }

            invoiceDataResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is InvoiceState.SuccessShowInvoices -> {
                        binding.apply {
                            emptyStateIv.isVisible = response.data?.data.isNullOrEmpty() == true
                            if (recycleView.binding.swipeRefresh.isRefreshing)
                                recycleView.binding.rv.resetScrollState()
                        }


                        clearState()
                    }

                    is InvoiceState.Loading -> {
                        handleLoading()
                    }

                    is InvoiceState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken(), "")
                        val intent = Intent(activity, IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }

                    is InvoiceState.SuccessShowSingleInvoice -> {
                        clearState()
                        binding.searchView.setQuery("", false)
                        findNavController().navigate(
                            com.codeIn.myCash.ui.home.clients_vendors.see_more_invoices.SeeMoreInvoicesFragmentDirections.actionSeeMoreInvoicesFragmentToInvoiceFragment(
                                response.data?.id.toString()
                            )
                        )
                    }

                    is InvoiceState.Idle -> {
                        handleIdle()
                    }

                    is InvoiceState.ServerError -> {
                        handleError(
                            response.error.getErrorMessage(
                                requireContext()
                            )
                        )
                    }

                    else -> {

                    }
                }
            }

            filter.observe(viewLifecycleOwner) {
                updateInvoiceFiltersViews(it)
            }

            _invoiceDate?.let {
                binding.dateView.apply {
                    visible()
                    text = _invoiceDate
                }
            }
        }

    }

    private fun handleLoading() = binding.run {
        if (!binding.recycleView.binding.swipeRefresh.isRefreshing)
            animationView.visible()
    }

    private fun handleIdle() = binding.run {
        animationView.gone()
        recycleView.binding.swipeRefresh.isRefreshing = false
    }


    private fun handleError(message: String) {
        binding.animationView.gone()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }


    private fun initListeners() = binding.apply {
        allTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        taxInvoiceTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        simpleInvoiceTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        instantInvoicesTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        purchaseInvoiceTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        saleInvoiceTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        purchaseReturnedTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        salesReturnedTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        paymentCompletedTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        paymentUncompletedTextView.setOnClickListener(this@SeeMoreInvoicesFragment)
        transactions.setOnClickListener {
            navigateTo(
                navDirections = InvoicesFragmentDirections.actionNavigationInvoicesFragmentToMadaTransactionsFragment()
            )
        }
        reconciliation.setOnClickListener {
            if (NFCChecker.checkNotPOSAndNFC(requireContext())) {
                NearPayPayment.reconciliationNearPay(binding.root, "", nearPayListener)
            } else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())) {
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.this_device_is_not_support_nfc),
                    false
                )
            }
        }
        searchView.inputType = InputType.TYPE_CLASS_NUMBER
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getInvoiceByInvoiceNumber(query)
                return false
            }
        })
        imageScanner.setOnClickListener {
            openScanner()
        }
        date.setOnClickListener {
            dateDialog()
        }
        dateView.setOnClickListener {
            dateView.text = getString(R.string.date)
            dateView.gone()
            adapter.submitList(null)
            viewModel.getInvoicesWithFilter(viewModel.filter.value!!, null)
        }
        recycleView.binding.swipeRefresh.setOnRefreshListener {
            viewModel.getInvoicesWithFilter(viewModel.filter.value!!, viewModel._invoiceDate)
        }
    }


    private fun updateInvoiceFiltersViews(invoiceFilter: InvoiceFilter) {
        val context = requireContext()
        val binding = binding

        binding.apply {
            val viewsToStyle = listOf(
                allTextView,
                taxInvoiceTextView,
                simpleInvoiceTextView,
                instantInvoicesTextView,
                purchaseInvoiceTextView,
                saleInvoiceTextView,
                purchaseReturnedTextView,
                salesReturnedTextView,
                paymentCompletedTextView,
                paymentUncompletedTextView
            )

            val selectedView = when (invoiceFilter) {
                ALL -> allTextView
                TAX_INVOICE -> taxInvoiceTextView
                SIMPLE_INVOICE -> simpleInvoiceTextView
                INSTANT_INVOICE -> instantInvoicesTextView
                PURCHASE_INVOICE -> purchaseInvoiceTextView
                SALE_INVOICE -> saleInvoiceTextView
                PURCHASE_RETURNED -> purchaseReturnedTextView
                SALES_RETURNED -> salesReturnedTextView
                PAYMENT_COMPLETED -> paymentCompletedTextView
                PAYMENT_NOT_COMPLETED -> paymentUncompletedTextView
            }

            updateSectionsViews(context, viewsToStyle, selectedView)
        }

    }

    private val invoicesCommunicator = object : InvoicesAdapter.Communicator {
        override fun onClick(invoice: InvoiceModel) {
            findNavController().navigate(
                SeeMoreInvoicesFragmentDirections.actionSeeMoreInvoicesFragmentToInvoiceFragment(
                    invoice.id.toString()
                )
            )
        }

        override fun onUnpaidClick(invoice: InvoiceModel) {
            TODO("Not yet implemented")
        }
    }

    private fun navigateTo(navDirections: NavDirections) {
        if (findNavController().currentDestination?.id == R.id.seeMoreInvoicesFragment) {
            findNavController().navigate(navDirections)
        }
    }

    private fun openScanner() {
        if (Build.VERSION.SDK_INT >= 32) {
            val i: Intent = Intent(activity, QRCodeScannerActivity::class.java)
            startActivityForResult(i, Request.SCANNER.value)
        } else {
            if (checker?.checkPermissionCamera(activity) == true) {
                checker?.askForPermissionCamera(activity)
            } else {
                val i: Intent = Intent(activity, QRCodeScannerActivity::class.java)
                startActivityForResult(i, Request.SCANNER.value)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Request.SCANNER.value && resultCode == Activity.RESULT_OK)
            viewModel.getSingleInvoice(data?.getStringExtra(AppConstants.SCAN_VALUE))
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun dateDialog() {
        val datePicker = DatePickerBottomSheet(
            preventFutureDates = true,
            communicator = object : DatePickerBottomSheet.Communicator {
                override fun pickDate(date: String) {
                    binding.dateView.apply {
                        visible()
                        text = date
                    }
                    adapter.submitList(null)
                    viewModel.getInvoicesWithFilter(viewModel.filter.value!!, date = date)
                }
            })
        datePicker.show(childFragmentManager, DatePickerBottomSheet.TAG)
    }

    private val nearPayListener = object : NearPayPayment.Companion.Listener {
        override fun onPurchaseProcess(
            code: Int,
            transactionData: TransactionData?,
            purchaseFailure: PurchaseFailure?,
            cashValue: String?,
            visaValue: String?
        ) {

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
            if (code == 0) {
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.success_message),
                    true
                )
            } else {
                when (code) {
                    1 -> {
                        CustomToaster.show(
                            requireContext(),
                            (reconcileFailure as ReconcileFailure.FailureMessage).message.toString(),
                            false
                        )
                    }

                    2 -> {
                        CustomToaster.show(
                            requireContext(),
                            (reconcileFailure as ReconcileFailure.AuthenticationFailed).message.toString(),
                            false
                        )
                    }

                    3 -> {
                        CustomToaster.show(
                            requireContext(),
                            (reconcileFailure as ReconcileFailure.InvalidStatus).status.toString(),
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

    }

    override fun onClick(view: View?) {
        val filter = view?.tag as InvoiceFilter
        if (filter == viewModel.filter.value) {
            binding.recycleView.binding.rv.resetScrollState()
            return
        }
        adapter.submitList(null)
        viewModel.updateFilter(view?.tag as InvoiceFilter)
    }
}