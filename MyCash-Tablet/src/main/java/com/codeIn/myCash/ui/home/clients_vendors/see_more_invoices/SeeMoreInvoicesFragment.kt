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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.InvoiceFilter.*
import com.codeIn.common.data.Request
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.scanner.QRCodeScannerActivity
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoicesBinding
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.invoices.invoices.InvoicesAdapter
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.myCash.utilities.pickers.DatePickerBottomSheet
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SeeMoreInvoicesFragment : Fragment() {
    private val viewModel: SeeMoreInvoicesViewModel by viewModels()
    private var _binding: FragmentInvoicesBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()
    private val checker = PermissionChecker()

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

        //set the click listeners for the main section and filter
        binding.apply {
            header.visible()
            allTextView.setOnClickListener {
                viewModel.updateFilter(ALL)
            }
            taxInvoiceTextView.setOnClickListener {
                viewModel.updateFilter(TAX_INVOICE)
            }
            simpleInvoiceTextView.setOnClickListener {
                viewModel.updateFilter(SIMPLE_INVOICE)
            }
            instantInvoicesTextView.setOnClickListener {
                viewModel.updateFilter(INSTANT_INVOICE)
            }
            purchaseInvoiceTextView.setOnClickListener {
                viewModel.updateFilter(PURCHASE_INVOICE)
            }
            saleInvoiceTextView.setOnClickListener {
                viewModel.updateFilter(SALE_INVOICE)
            }
            purchaseReturnedTextView.setOnClickListener {
                viewModel.updateFilter(PURCHASE_RETURNED)
            }
            salesReturnedTextView.setOnClickListener {
                viewModel.updateFilter(SALES_RETURNED)
            }
            paymentCompletedTextView.setOnClickListener {
                viewModel.updateFilter(PAYMENT_COMPLETED)
            }
            paymentUncompletedTextView.setOnClickListener {
                viewModel.updateFilter(PAYMENT_NOT_COMPLETED)
            }
            transactions.gone()
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
                viewModel.getInvoicesWithFilter(viewModel.filter.value!!, null)
            }
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        val adapter = InvoicesAdapter(requireContext(), invoicesCommunicator , viewModel.currency)
        binding.recycleView.adapter = adapter

        //observe the live vendorsData and update the views accordingly
        viewModel.apply {

            invoiceDataResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is InvoiceState.SuccessShowInvoices -> {
                        hideKeyboard(requireContext() , view)
                        if (response.data?.data.isNullOrEmpty()) {
                            binding.recycleView.gone()
                        } else {
                            binding.recycleView.visible()
                            adapter.submitList(response.data?.data)
                        }
                        clearState()
                    }

                    is InvoiceState.Loading -> handleLoading()
                    is InvoiceState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is InvoiceState.SuccessShowSingleInvoice ->{
                        clearState()
                        binding.searchView.setQuery("" , false )
                        findNavController().navigate(
                            com.codeIn.myCash.ui.home.clients_vendors.see_more_invoices.SeeMoreInvoicesFragmentDirections.actionSeeMoreInvoicesFragmentToInvoiceFragment(
                                response.data?.id.toString()
                            )
                        )
                    }
                    is InvoiceState.Idle -> handleIdle()
                    is InvoiceState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {

                    }
                }
            }

            clientId.observe(viewLifecycleOwner){
                if (it?.isNotEmpty() == true){
                    getInvoicesWithFilter(filter.value?:ALL, date = binding.dateView.text.toString())
                }
            }
            filter.observe(viewLifecycleOwner) {
                updateInvoiceFiltersViews(it)
            }
        }

    }

    private fun handleLoading() = binding.animationView.visible()

    private fun handleIdle() = binding.animationView.gone()

    private fun handleError(message: String) {
        binding.animationView.gone()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
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
               com.codeIn.myCash.ui.home.clients_vendors.see_more_invoices.SeeMoreInvoicesFragmentDirections.actionSeeMoreInvoicesFragmentToInvoiceFragment(
                   invoice.id.toString()
               )
           )
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getInvoicesWithFilter(viewModel.filter.value?:ALL , binding.dateView.text.toString())
    }

    private fun openScanner(){
        if (Build.VERSION.SDK_INT >= 32){
            val i: Intent = Intent(activity, QRCodeScannerActivity::class.java)
            startActivityForResult(i , Request.SCANNER.value)
        }
        else {
            if (checker?.checkPermissionCamera(activity) == true) {
                checker?.askForPermissionCamera(activity)
            } else {
                val i: Intent = Intent(activity, QRCodeScannerActivity::class.java)
                startActivityForResult(i , Request.SCANNER.value)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Request.SCANNER.value && resultCode == Activity.RESULT_OK)
            viewModel.getSingleInvoice(data?.getStringExtra(AppConstants.SCAN_VALUE))
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

     private fun dateDialog(){
         val datePicker = DatePickerBottomSheet(
             preventFutureDates = true,
             communicator = object : DatePickerBottomSheet.Communicator {
                 override fun pickDate(date: String) {
                     binding.dateView.visible()
                     binding.dateView.text = date
                     viewModel.getInvoicesWithFilter(viewModel.filter.value!!,date= date)
                 }
             })
         datePicker.show(childFragmentManager, DatePickerBottomSheet.TAG)
     }
}