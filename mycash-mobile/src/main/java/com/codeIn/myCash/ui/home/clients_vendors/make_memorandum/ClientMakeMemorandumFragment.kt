package com.codeIn.myCash.ui.home.clients_vendors.make_memorandum

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.Cart
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.MemorandumType
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentStatus
import com.codeIn.common.data.PaymentType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentCreditorDebtorNoteBinding
import com.codeIn.myCash.ui.ViewStrokes
import com.codeIn.myCash.ui.home.clients_vendors.memorandums.ClientMemorandumsFragmentDirections
import com.codeIn.myCash.ui.home.invoices.makeMemorandum.InvoiceProductsAdapter
import com.codeIn.myCash.ui.home.invoices.makeMemorandum.MakeMemorandumViewModel
import com.codeIn.myCash.ui.home.invoices.makeMemorandum.SpecifyAmountDialog
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.dialogs.PaymentMethodsDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientMakeMemorandumFragment : Fragment() {

    private val viewModel: MakeMemorandumViewModel by viewModels()

    private var _binding: FragmentCreditorDebtorNoteBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditorDebtorNoteBinding.inflate(inflater, container, false)
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
            anotherProductTextView.setOnClickListener {
                findNavController()?.navigate(
                    com.codeIn.myCash.ui.home.clients_vendors.make_memorandum.ClientMakeMemorandumFragmentDirections.actionClientMakeMemorandumFragmentToAddMoreProductsToMemorandumClientFragment()
                )
            }
            issueNoticeButton.setOnClickListener {
                viewModel.makeValidationToValidItems()
            }
            creditorNoteTextView.setOnClickListener {
                viewModel.updateMemorandumType(MemorandumType.CREDITOR)
            }
            debtorNoteTextView.setOnClickListener {
                viewModel.updateMemorandumType(MemorandumType.DEBTOR)
            }
        }

        viewModel.apply {
            val adapter =
                InvoiceProductsAdapter(productsCommunicator, requireContext(), viewModel.currency)
            binding.productRecycleView.adapter = adapter

            invoiceModel.observe(viewLifecycleOwner) {
                bindInvoiceData(it)
                if (it.products?.isNotEmpty() == true) {
                    initialProductsInInvoice(it.creditsProducts as ArrayList<ProductInInvoiceModel>)
                }
            }
            selectedProductsInMemorandum.observe(viewLifecycleOwner) {
                if (it?.list?.isNotEmpty() == true) {
                    adapter.submitList(it?.list)
                    binding.productRecycleView.adapter?.notifyDataSetChanged()
                    binding.productRecycleView.visible()
                } else {
                    binding.productRecycleView.gone()
                }
                bindDataSummary(it)
            }
            memorandumType.observe(viewLifecycleOwner) {
                this@ClientMakeMemorandumFragment.updateNoticeType(it)
            }

            memorandumDataResult.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is MemorandumState.Loading -> handleLoading()
                    is MemorandumState.Idle -> handleIdle()
                    is MemorandumState.StateError -> handleError(response.message.toString())
                    is MemorandumState.SuccessValidationInput -> {
                        if (selectedProductsInMemorandum.value?.list?.isNotEmpty() == true){
                            if (viewModel.memorandumType.value == MemorandumType.DEBTOR)
                                findNavController().navigate(
                                    com.codeIn.myCash.ui.home.clients_vendors.make_memorandum.ClientMakeMemorandumFragmentDirections.actionClientMakeMemorandumFragmentToClientMemorandumInvoicePaymentFragment(
                                        viewModel.selectedProductsInMemorandum.value
                                            ?: ProductInInvoiceMemorandum(),
                                        viewModel.invoiceModel.value?.id.toString()
                                    )
                                )

                            else if (viewModel.memorandumType.value == MemorandumType.CREDITOR) {
                                handleMemorandumCreditor()
                            }
                            clearState()
                        }
                        else {
                            Toast.makeText(requireContext() , getString(R.string.you_donot_add_in_invoice) , Toast.LENGTH_LONG).show()
                        }
                    }
                    is MemorandumState.SuccessShowSingleMemorandum -> handleSuccess()
                    is MemorandumState.InputError -> handleInputError(response.inputError)
                    is MemorandumState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    is MemorandumState.UnAuthorized->{
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleMemorandumCreditor(){
        var returnedValue = 0.0
        val invoiceTotal = NumberHelper.persianToEnglishText(viewModel.invoiceModel.value?.totalPrice?:"0.0")
        val currentTotal = NumberHelper.persianToEnglishText(viewModel.selectedProductsInMemorandum.value?.finalTotal?:"0.0")
        val remaining = NumberHelper.persianToEnglishText(viewModel.invoiceModel.value?.rammingPrice?:"0.0")
        val deposit = invoiceTotal.toDouble() - remaining.toDouble()
        if (currentTotal.toDouble() > 0){
//            if (viewModel.invoiceModel.value?.paymentStatus == PaymentStatus.COMPLETED.value.toString()){
//                showPaymentMethodsDialog(currentTotal.toString())
//            }
//            else {
//                if (deposit >= currentTotal.toDouble()){
//                    showPaymentMethodsDialog(currentTotal)
//                }
//                else {
//                    viewModel.makeMemorandum("0.0" , "0.0" , PaymentType.CASH.value.toString())
//                }
//            }
            viewModel.makeMemorandum(currentTotal , "0.0" , PaymentType.CASH.value.toString())
        }
        else {
            Toast.makeText(requireContext() , getString(R.string.you_donot_add_in_invoice) , Toast.LENGTH_LONG).show()
        }
    }
    private fun handleSuccess(){
        viewModel.clearState()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().popBackStack()
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

            InvalidInput.QUANTITY ->{
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_enter_valid_quantity), Toast.LENGTH_SHORT
                ).show()
            }

            InvalidInput.PRICE ->{
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_enter_valid_price), Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    private fun bindInvoiceData(invoiceModel : InvoiceModel?){
        binding.apply {
            invoiceNumberTextView.text = "#${invoiceModel?.invoiceNumber}"
            invoiceDateTextView.text = "${invoiceModel?.date}"
        }
    }

    private fun bindDataSummary(productsInCart: ProductInInvoiceMemorandum?){
        binding.apply {
            initialTotalTextView.text = "${productsInCart?.initialTotal} ${viewModel.currency}"

            vatLabel.text = "${getString(R.string.added_tax)} (${viewModel?.tax})"

            vat15TextView.text = "${productsInCart?.tax} ${viewModel.currency}"

            totalPriceTextView.text = "${productsInCart?.finalTotal} ${viewModel.currency}"
        }
    }

    private fun updateNoticeType(memorandumType : MemorandumType) {

        val context = requireContext()
        val binding = binding

        val viewsToStyle = listOf(
            binding.creditorNoteTextView,
            binding.debtorNoteTextView
        )

        val selectedView = when (memorandumType) {
            MemorandumType.CREDITOR -> binding.creditorNoteTextView
            MemorandumType.DEBTOR -> binding.debtorNoteTextView
            else -> {
                binding.creditorNoteTextView
            }
        }
        updateSectionsViews(context, viewsToStyle, selectedView, stroke = ViewStrokes.R100_S1)
    }

    private val productsCommunicator = object : InvoiceProductsAdapter.Communicator {
        override fun specifyAmount(product: ProductModel) {
            showSpecifyAmountDialog(product)
        }

        override fun addProductToMemorandum(product: ProductModel) {
            viewModel.handleProductInMemorandum(product , Cart.ADD)
        }

        override fun updateProductInMemorandum(product: ProductModel) {
            viewModel.handleProductInMemorandum(product , Cart.ADD)
        }

        override fun removeProductFromMemorandum(product: ProductModel) {
            viewModel.handleProductInMemorandum(product , Cart.DELETE)
        }
    }


    private var specifyAmountDialog: SpecifyAmountDialog? = null
    private fun showSpecifyAmountDialog(productModel: ProductModel) {
        specifyAmountDialog?.dismiss()
        specifyAmountDialog =
            SpecifyAmountDialog(requireContext(), object : SpecifyAmountDialog.Communicator {
                override fun onContinue(amount: String) {
                    productModel.difPrice = amount
                    viewModel.handleProductInMemorandum(productModel , Cart.ADD)
                    binding.productRecycleView.adapter?.notifyDataSetChanged()
                }
            } , productModel , viewModel.moneyValidationUseCase)
        specifyAmountDialog?.show()
    }

    override fun onResume() {
        super.onResume()
        if (findNavController().currentBackStackEntry?.savedStateHandle?.contains("products_in_memorandum") == true){
            val products = findNavController().currentBackStackEntry?.savedStateHandle?.get("products_in_memorandum") ?: ProductInInvoiceMemorandum()
            if (products.list?.isNotEmpty() == true)
                viewModel.getSelectedProductsInMemorandum(products)
        }
    }

    private var paymentMethodsDialog: PaymentMethodsDialog? = null
    private fun showPaymentMethodsDialog(returnedValue : String?) {
        if (paymentMethodsDialog?.isShowing == true)
            paymentMethodsDialog?.dismiss()
        paymentMethodsDialog =
            PaymentMethodsDialog(requireContext(), returnedValue?:"0.0",
                invoiceModel = viewModel.invoiceModel.value ,currency = viewModel.currency ,
                paymentMethodsCommunicator)
        paymentMethodsDialog?.show()
    }

    private val paymentMethodsCommunicator = object : PaymentMethodsDialog.Communicator {
        override fun onPaymentMethodSelected(paymentType: PaymentType, returnedCash : String?, returnedVisa : String?) {
            viewModel.makeMemorandum(
                returnedCash , returnedVisa , paymentType.value.toString()
            )
        }
    }
}