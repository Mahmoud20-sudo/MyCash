package com.codeIn.myCash.ui.home.invoices.seeMoreReceipts

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.ReceiptsFilter
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.databinding.FragmentSeeMoreReceiptsBinding
import com.codeIn.myCash.ui.home.clients_vendors.cleint_details.adapters.ReceiptsAdapter
import com.codeIn.myCash.ui.home.invoices.incompleteInvoice.PayReceiptDialog
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.pickers.DatePickerBottomSheet
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SeeMoreReceiptsFragment : Fragment() {

    private val viewModel: SeeMoreReceiptsViewModel by viewModels()

    private var _binding: FragmentSeeMoreReceiptsBinding? = null
    private val binding get() = _binding!!

    private var currentReceiptModel : CurrentReceiptModel? = null

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeMoreReceiptsBinding.inflate(inflater, container, false)
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

        binding.apply {
            all.setOnClickListener {
                viewModel.updateFilter(ReceiptsFilter.All , binding.searchView.text.toString())
            }
            completedTextView.setOnClickListener {
                viewModel.updateFilter(ReceiptsFilter.COMPLETED , binding.searchView.text.toString())
            }
            unCompletedTextView.setOnClickListener {
                viewModel.updateFilter(ReceiptsFilter.UN_COMPLETED , binding.searchView.text.toString())
            }

            refresh.setOnClickListener {
                searchView.text = ""
                viewModel.getReceiptsWithFilter(viewModel.filter.value?:ReceiptsFilter.All, null)
            }

            searchView.setOnClickListener {
                dateDialog()
            }
        }

        val receiptsAdapter = ReceiptsAdapter(receiptsCommunicator , viewModel.currency)
        binding.receiptRecycleView.adapter = receiptsAdapter

        viewModel.apply {

            invoiceId.observe(viewLifecycleOwner) {
                if (it?.isNotEmpty() == true)
                    getReceipts()
            }

            receiptDataResult.observe(
                viewLifecycleOwner
            ) { response ->
                when (response) {
                    is ReceiptState.Loading -> handleLoading()
                    is ReceiptState.Idle -> handleIdle()
                    is ReceiptState.StateError -> {}
                    is ReceiptState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ReceiptState.SuccessShowReceipts -> {
                        if (response.data?.data?.isEmpty() == true) {
                            binding.receiptRecycleView.gone()
                        } else {
                            receiptsAdapter.submitList(response?.data?.data)
                            binding.receiptRecycleView.visible()
                        }
                    }
                    is ReceiptState.InputError -> {}
                    is ReceiptState.ServerError -> {}
                    else -> {}
                }
            }

            filter.observe(viewLifecycleOwner) {
                updateReceiptFilterViews(it)
            }
        }
    }


    private val receiptsCommunicator = object : ReceiptsAdapter.Communicator {
        override fun payReceipt(receipt: ReceiptModel) {
            showPayReceiptDialog(receipt)
        }
    }
    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private var payReceiptDialog: PayReceiptDialog? = null
    private fun showPayReceiptDialog(receipt: ReceiptModel) {
        if (payReceiptDialog?.isShowing == true)
            payReceiptDialog?.dismiss()
        payReceiptDialog = PayReceiptDialog(requireContext(), receipt, viewModel.currency
            , parentFragmentManager , payReceiptCommunicator , currentReceiptModel)
        payReceiptDialog?.show()
    }

    private val payReceiptCommunicator = object : PayReceiptDialog.Communicator {
        override fun onPay(model : CurrentReceiptModel , receipt: ReceiptModel) {
            currentReceiptModel = model

            findNavController().navigate(
                com.codeIn.myCash.ui.home.invoices.seeMoreReceipts.SeeMoreReceiptsFragmentDirections.actionSeeMoreReceiptsFragmentToNavigationPayReceiptFragment(
                    receipt, model
                )
            )
        }
    }

    private fun updateReceiptFilterViews(receiptsFilter: ReceiptsFilter) {
        val context = requireContext()
        val binding = binding

        binding.apply {
            val viewsToStyle = listOf(
                all,
                completedTextView,
                unCompletedTextView,
            )

            val selectedView = when (receiptsFilter) {
                ReceiptsFilter.All -> all
                ReceiptsFilter.COMPLETED -> completedTextView
                ReceiptsFilter.UN_COMPLETED -> unCompletedTextView
            }

            updateSectionsViews(context, viewsToStyle, selectedView)
        }
    }

    private fun dateDialog(){
        val datePicker = DatePickerBottomSheet(
            preventFutureDates = false,
            preventOldBates = false,
            communicator = object : DatePickerBottomSheet.Communicator {
                override fun pickDate(date: String) {
                    binding.searchView.text = date
                    viewModel.getReceiptsWithFilter(viewModel.filter.value?:ReceiptsFilter.All ,date= date)
                }
            })
        datePicker.show(childFragmentManager, DatePickerBottomSheet.TAG)
    }
}