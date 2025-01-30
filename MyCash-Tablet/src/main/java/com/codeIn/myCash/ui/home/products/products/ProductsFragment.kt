package com.codeIn.myCash.ui.home.products.products

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.AppConstants.DELAY_TIME_SEARCH
import com.codeIn.common.data.Cart
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentType
import com.codeIn.common.data.TaxType
import com.codeIn.common.data.Taxable
import com.codeIn.common.nearpay.NearPayPayment
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.getQueryTextChangeStateFlow
import com.codeIn.common.util.gone
import com.codeIn.common.util.invisible
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentProductsBinding
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientState
import com.codeIn.myCash.features.sales.domain.clients.model.ClientRequest
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.category.remote.response.category.FilterCategoryState
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.product.model.ProductFilter
import com.codeIn.myCash.integration.di.app.MyCashPreferences
import com.codeIn.myCash.integration.di.app.MyCashPreferencesKeys
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.MainActivity
import com.codeIn.myCash.ui.home.invoices.invoice.InvoiceViewModel
import com.codeIn.myCash.ui.home.products.products.adapters.CategoriesAdapter
import com.codeIn.myCash.ui.home.products.products.adapters.CategoriesMainAdapter
import com.codeIn.myCash.ui.home.products.products.adapters.ProductsAdapter
import com.codeIn.myCash.ui.home.products.products.dialogs.ConfirmDeleteProductDialog
import com.codeIn.myCash.ui.home.products.products.dialogs.ProductOptionsDialog
import com.codeIn.myCash.ui.home.products.products.dialogs.ProductsFilterDialog
import com.codeIn.myCash.ui.home.products.products.mada.clear
import com.codeIn.myCash.ui.home.products.products.mada.payMada
import com.codeIn.myCash.ui.home.products.products.mada.refundMada
import com.codeIn.myCash.ui.home.products.products.quickInvoice.QuickInvoiceProductsAdapter
import com.codeIn.myCash.ui.home.products.products.quickInvoice.QuickInvoiceViewModel
import com.codeIn.myCash.ui.home.products.products.quickInvoice.changeTypeColors
import com.codeIn.myCash.ui.home.products.products.quickInvoice.handleCashAndCard
import com.codeIn.myCash.ui.home.products.products.quickInvoice.handleInputErrorInTopSheet
import com.codeIn.myCash.ui.home.products.products.quickInvoice.handlePostPaid
import com.codeIn.myCash.ui.home.products.products.quickInvoice.handlePostPaidAndCard
import com.codeIn.myCash.ui.home.products.products.quickInvoice.showSummary
import com.codeIn.myCash.ui.home.products.products.quickInvoice.updateInvoiceType
import com.codeIn.myCash.ui.home.products.second_step_create_invoice.ClientInfoDialog
import com.codeIn.myCash.ui.home.products.second_step_create_invoice.SearchClientsAutoCompleteTextViewAdapter
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.myCash.utilities.views.TopSheetBehavior
import com.codein.common.printer.marvel.ConnectionWithUsb
import com.example.tscdll.TSCActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import io.nearpay.sdk.data.models.ReconciliationReceipt
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.ReconcileFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val productsViewModel: ProductsViewModel by viewModels()
    private val quickInvoiceViewModel: QuickInvoiceViewModel by viewModels()
    private val invoiceModel: InvoiceViewModel by viewModels()

    private val datePicker = DatePicker()
    private var topSheetBehavior: TopSheetBehavior<ConstraintLayout>? = null
    @Inject
    lateinit var settings: MyCashPreferences
    private val myAnimator = MyAnimator()
    private var _binding: FragmentProductsBinding? = null
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
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsViewModel.getCategories(parentOnly = "1" )

        topSheetBehavior = TopSheetBehavior.from(binding.topSheet.topSheetLayout)
        val activity = (requireActivity() as MainActivity)

        val isQuickInvoiceMode = settings.getBoolean(MyCashPreferencesKeys.QUICK_INVOICE_MODE, false)

        if (isQuickInvoiceMode) {
            topSheetBehavior?.state = TopSheetBehavior.STATE_EXPANDED
            topSheetBehavior?.isClosable = false
            activity.changeProductsLabel(getString(com.codeIn.myCash.R.string.invoice))
        }
        else {
            activity.changeProductsLabel(getString(com.codeIn.myCash.R.string.products))
            binding.topSheet.root.setOnClickListener {
                // toggle the top sheet when the user clicks on the top sheet root view
                if (topSheetBehavior?.state == TopSheetBehavior.STATE_COLLAPSED)
                    topSheetBehavior?.state = TopSheetBehavior.STATE_EXPANDED
                else
                    topSheetBehavior?.state = TopSheetBehavior.STATE_COLLAPSED
            }

            topSheetBehavior?.setTopSheetCallback(object : TopSheetBehavior.TopSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    // Hide the bottom navigation view when the top sheet is expanded
                    if (newState != TopSheetBehavior.STATE_COLLAPSED && activity.isNavBottomVisible()) {
                        activity.hideNavBottom(topBar = false)
                        hideKeyboard(requireContext(), requireActivity().currentFocus)
                    } else if (newState == TopSheetBehavior.STATE_COLLAPSED) {
                        activity.showNavBottom(topBar = false)
                        hideKeyboard(requireContext(), requireActivity().currentFocus)
                    }
                }

                override fun onSlide(topSheet: View, slideOffset: Float, isOpening: Boolean?) {
                    // rotate the arrow image view when the top sheet is sliding with the offset
                    binding.topSheet.arrowImageView.rotation = slideOffset * 180
                }
            })
        }


        val categoriesAdapter = CategoriesAdapter(context = requireContext(), communicator = categoryCommunicator)

        val categoriesMainAdapter = CategoriesMainAdapter(context = requireContext(), communicator = categoryMainCommunicator)

        val productsAdapter = ProductsAdapter(activity, productCommunicator , productsViewModel.currency.toString())

        binding.apply {
            categoryRecycleView.adapter = categoriesAdapter
            categoryMainRecycleView.adapter = categoriesMainAdapter
            productRecycleView.adapter = productsAdapter

            // set the click listener to the view invoice card view to navigate to the invoice details fragment
            viewInvoiceCardView.setOnClickListener {
                if (findNavController().currentDestination?.id == com.codeIn.myCash.R.id.navigation_productsFragment)
                    findNavController().navigate(
                        com.codeIn.myCash.ui.home.products.products.ProductsFragmentDirections.actionNavigationProductsFragmentToNavigationFirstStepCreationInvoiceFragment(
                            productsViewModel.selectedProductsInCart.value!!
                        )
                    )
            }

            filterImageView.setOnClickListener {
                showFilterDialog()
            }

            addImageView.setOnClickListener {
                if ((requireActivity() as MainActivity).viewModel.isCompleteInfo.value == true ) {
                    productsViewModel.updateSelectedProductDependOnBack(ProductsInCart())
                    if (findNavController().currentDestination?.id == R.id.navigation_productsFragment)
                        findNavController().navigate(
                            com.codeIn.myCash.ui.home.products.products.ProductsFragmentDirections.actionNavigationProductsFragmentToNavigationNewProductFragment()
                        )
                }
                else {
                    CustomToaster.show(
                        requireContext() ,
                        getString(R.string.please_complete_your_info_before_make_any_invoice),
                        false
                    )
                }

            }

            mainCategoryImageView.setOnClickListener {
                val shouldOpen = categoryMainLayout.visibility != View.VISIBLE
                myAnimator.animateImageTransition(mainCategoryImageView, shouldOpen)
                if (shouldOpen) {
                    categoryMainLayout.visible()
                } else {
                    categoryMainLayout.gone()
                }
            }

            categoryMainBackArrow.setOnClickListener {
                val shouldOpen = categoryMainLayout.visibility != View.VISIBLE
                myAnimator.animateImageTransition(mainCategoryImageView, shouldOpen)
                if (shouldOpen) {
                    categoryMainLayout.visible()
                } else {
                    categoryMainLayout.gone()
                }
            }
            searchView
                .getQueryTextChangeStateFlow()
                .debounce(DELAY_TIME_SEARCH)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .onEach { query ->
                    productsViewModel.updateSearchText(query)

                }.launchIn(productsViewModel.viewModelScope)

            scanImageView.setOnClickListener {
                openScanner()
            }
        }

        productsViewModel.apply {
            categoriesDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is CategoryState.SuccessMainCategories->{
                        val hasCategory = selectedCategoryId.value != null && selectedCategoryId.value != "0"
                        updateCategoriesData(response.data?.data , getString(R.string.all) , hasCategory)
                    }
                    is CategoryState.Loading -> handleLoading()
                    is CategoryState.Idle -> handleIdle()
                    is CategoryState.UnAuthorized ->{
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is CategoryState.StateError -> handleError(response.message.toString())
                    is CategoryState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
            searchText.observe(viewLifecycleOwner){
                getProducts()
            }
            productsDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ProductsState.SuccessShowProducts ->{
                        getProductsDependOnCart(response.data?.data as ArrayList<ProductModel>)
                        clearState()
                    }
                    is ProductsState.SuccessDeleteProduct ->{
                        handleSuccess(response.message.toString())
                        getProducts()
                        clearState()
                    }
                    is ProductsState.Loading -> handleLoading()
                    is ProductsState.Idle -> handleIdle()
                    is ProductsState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ProductsState.StateError -> {
                        handleError(response.message.toString())
                        clearState()
                    }
                    is ProductsState.ServerError -> {
                        clearState()
                        handleError(response.error.getErrorMessage(requireContext()))
                    }else -> {

                    }

                }
            }
            products.observe(viewLifecycleOwner){
                if (it?.isNotEmpty() == true){
                    productsAdapter.submitList(it)
                    binding.productRecycleView.visible()
                    Handler().postDelayed(Runnable { binding.productRecycleView.scrollToPosition(0) }, 200)
                }
                else {
                    binding.productRecycleView.gone()
                }
            }
            productFilter.observe(viewLifecycleOwner){
                getProducts()
            }

            filterCategoriesDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is FilterCategoryState.SuccessFilterCategory->{
                        categoriesMainAdapter.submitList(response.data?.data)
                        binding.categoryMainRecycleView.adapter?.notifyDataSetChanged()
                    }
                    else ->{
                    }
                }
            }

            categoriesData.collectOnLifecycle(viewModelScope , viewLifecycleOwner){
                if (it?.isNotEmpty() == true) {
                    categoriesAdapter.submitList(it)
                }
            }

            selectedCategoryId.collectOnLifecycle(viewModelScope , viewLifecycleOwner){
                if (it != null)
                    getProducts()
            }
            selectedProductsInCart.observe(viewLifecycleOwner){cart->
                binding.apply {
                    countTextView.text = cart?.count.toString()
                    val totalPrice = "${cart?.finalTotal} $currency"
                    totalPriceTextView.text = totalPrice
                }
                if (cart?.count!! > 0)
                    myAnimator.animateTranslationYShow(binding.viewInvoiceCardView)
                else
                    myAnimator.animateTranslationYHide(binding.viewInvoiceCardView)
            }
        }

        binding.topSheet.apply {
            backButton.setOnClickListener {
                if (topSheetBehavior?.isClosable == true)
                    topSheetBehavior?.state = TopSheetBehavior.STATE_COLLAPSED
            }

            scanner.setOnClickListener {
                openQuickInvoiceScanner()
            }
            simpleInvoiceTextView.setOnClickListener {
                quickInvoiceViewModel.updateInvoiceType(InvoiceType.SIMPLE)
            }
            taxInvoiceTextView.setOnClickListener {
                quickInvoiceViewModel.updateInvoiceType(InvoiceType.TAX)
            }
            purchaseInvoiceTextView.setOnClickListener {
                quickInvoiceViewModel.updateMainInvoiceType(MainTypeInvoice.PURCHASE)
            }

            cashTextView.setOnClickListener {
                quickInvoiceViewModel.updatePaymentType(PaymentType.CASH)
            }
            creditCardTextView.setOnClickListener {
                if (NFCChecker.checkNotPOSAndNFC(requireContext()))
                    quickInvoiceViewModel.updatePaymentType(PaymentType.CREDIT_CARD)
                else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())){
                    CustomToaster.show(
                        requireContext() ,
                        getString(R.string.this_device_is_not_support_nfc) ,
                        false
                    )
                }
            }
            postpaidTextView.setOnClickListener {
                quickInvoiceViewModel.updatePaymentType(PaymentType.POST_PAID)
            }
            cashAndCreditCardTextView.setOnClickListener {
                if (NFCChecker.checkNotPOSAndNFC(requireContext()))
                    quickInvoiceViewModel.updatePaymentType(PaymentType.CASH_AND_CREDIT_CARD)
                else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())){
                    CustomToaster.show(
                        requireContext() ,
                        getString(R.string.this_device_is_not_support_nfc) ,
                        false
                    )
                }
            }
            postpaidAndCreditCardTextView.setOnClickListener {
                if (NFCChecker.checkNotPOSAndNFC(requireContext()))
                    quickInvoiceViewModel.updatePaymentType(PaymentType.POST_PAID_AND_CREDIT_CARD)
                else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())){
                    CustomToaster.show(
                        requireContext() ,
                        getString(R.string.this_device_is_not_support_nfc) ,
                        false
                    )
                }
            }
            installmentPaymentTextView.setOnClickListener {
//                quickInvoiceViewModel.updatePaymentType(PaymentType.INSTALLMENT)
            }

            invoicePaymentDateEditText.setOnClickListener {
                datePicker.showDatePicker(
                    childFragmentManager = parentFragmentManager,
                    editText = invoicePaymentDateEditText ,
                    preventOldBates = true
                )
            }

            addClientCheckBox.setOnCheckedChangeListener { _, b ->
                if (b) {
                    showAddClientToInvoiceDialog()
                } else {
                    quickInvoiceViewModel.selectedProductsInCart.value?.clientRequest = null
                }
            }

//            anotherProductTextView.setOnClickListener {
//                quickInvoiceViewModel.addProductInQuickInvoice(
//                    productNameEditText.text.toString() ,
//                    priceEditText.text.toString() ,
//                    amountEditText.text.toString()
//                )
//                hideKeyboard(requireContext() , view)
//            }

            payInvoiceButton.setOnClickListener {
                if ((requireActivity() as MainActivity).viewModel.isCompleteInfo.value == true ) {
                    if (productNameEditText.text.toString().isNotEmpty() ||
                        priceEditText.text.toString().isNotEmpty() ||
                        amountEditText.text.toString().isNotEmpty())
                    {
                        quickInvoiceViewModel.addProductInQuickInvoice(
                            productNameEditText.text.toString() ,
                            priceEditText.text.toString() ,
                            amountEditText.text.toString()
                        )
                    }
                    else if (quickInvoiceViewModel.selectedProductsInCart.value?.list?.size!! > 0)
                    {
                        var cashValue =
                            binding.topSheet.cashPaidAmountEditText.text.toString()
                        var visaValue =
                            binding.topSheet.creditCardPaidAmountEditText.text.toString()
                        if (quickInvoiceViewModel.paymentType.value == PaymentType.CASH)
                            cashValue =
                                quickInvoiceViewModel.selectedProductsInCart.value?.finalTotal
                                    ?: "0.0"
                        if (quickInvoiceViewModel.paymentType.value == PaymentType.CREDIT_CARD)
                            visaValue =
                                quickInvoiceViewModel.selectedProductsInCart.value?.finalTotal
                                    ?: "0.0"

                        if (quickInvoiceViewModel.paymentType.value == PaymentType.CASH_AND_CREDIT_CARD ||
                            quickInvoiceViewModel.paymentType.value == PaymentType.POST_PAID_AND_CREDIT_CARD ||
                            quickInvoiceViewModel.paymentType.value == PaymentType.CREDIT_CARD
                        ) {
                            val value =
                                NumberHelper.persianToEnglishText(visaValue ?: "0.0")
                            if (value.toDouble() > 0) {
                                if (NFCChecker.checkNotPOSAndNFC(requireContext())) {
                                    NearPayPayment.purchaseNearPay(
                                        view, value,
                                        "9ace70b7-977d-4094-b7f4-4ecb17de6753",
                                        "" , nearPayListener , cashValue , visaValue)
                                } else {
                                    quickInvoiceViewModel.makeQuickInvoice(
                                        cashValue,
                                        visaValue,
                                        date = binding.topSheet.invoicePaymentDateEditText.text.toString(),
                                        null
                                    )
                                }
                            } else
                                quickInvoiceViewModel.makeQuickInvoice(
                                    cashValue,
                                    visaValue,
                                    date = binding.topSheet.invoicePaymentDateEditText.text.toString(),
                                    null
                                )
                        } else {
                            quickInvoiceViewModel.makeQuickInvoice(
                                cashValue,
                                visaValue,
                                date = binding.topSheet.invoicePaymentDateEditText.text.toString(),
                                null
                            )
                        }

                    }
                    else {
                        CustomToaster.show(requireContext() ,
                            getString(R.string.please_fill_all_the_fields),
                            false )
                    }
                    hideKeyboard(requireContext(), view)
                }
                else {
                    CustomToaster.show(
                        requireContext() ,
                        getString(R.string.please_complete_your_info_before_make_any_invoice),
                        false
                    )
                }
            }
        }

        binding.mada.apply {
            cancel.setOnClickListener {
                clear(binding.mada , view)
            }
            refund.setOnClickListener {
                refundMada(binding.mada , view , productsViewModel)
            }
            pay.setOnClickListener {
                payMada(binding.mada , view , productsViewModel)
            }
        }

        quickInvoiceViewModel.apply {
            invoiceType.observe(viewLifecycleOwner) {
                this@ProductsFragment.updateInvoiceType(
                    invoiceType = it,
                    binding = binding.topSheet
                )
            }
            paymentType.observe(viewLifecycleOwner) {
                changeTypeColors(
                    type = it,
                    binding = binding.topSheet ,
                    viewModel = quickInvoiceViewModel
                )
                when(it){
                    PaymentType.POST_PAID -> handlePostPaid(
                        binding.topSheet,
                        quickInvoiceViewModel
                    )
                    PaymentType.CASH_AND_CREDIT_CARD-> handleCashAndCard(
                        binding.topSheet,
                        quickInvoiceViewModel
                    )
                    PaymentType.POST_PAID_AND_CREDIT_CARD -> handlePostPaidAndCard(
                        binding.topSheet,
                        quickInvoiceViewModel
                    )
                    else -> {}
                }
            }
            val productsInQuickInvoiceAdapter = QuickInvoiceProductsAdapter(requireContext(), productInQuickInvoiceCommunicator, currency)
            binding.topSheet.products.adapter = productsInQuickInvoiceAdapter
            selectedProductsInCart.observe(viewLifecycleOwner){
                if (it?.list?.isNotEmpty() == true) {
                    productsInQuickInvoiceAdapter.submitList(it?.list)
                    binding.topSheet.products.visible()
                }
                else
                    binding.topSheet.products.gone()
                showSummary(binding.topSheet , it , currency , tax )
                binding.topSheet.products.adapter?.notifyDataSetChanged()
            }
            clientDataResult.observe(viewLifecycleOwner){ response ->
                when (response) {
                    is ClientState.Loading -> handleLoadingInTopSheet()
                    is ClientState.Idle -> handleIdleInTopSheet()
                    is ClientState.UnAuthorized ->{
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ClientState.SuccessShowSingleClient -> handleClientSuccess(response.data)
                    is ClientState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    is ClientState.SuccessShowClients ->{
                        clientInfoDialog?.setAutoCompleteData(response.data?.data)
                    }
                    else -> {}
                }
            }

            viewModelScope.launch {
                searchText
                    .debounce(AppConstants.DELAY_TIME_SEARCH)
                    .distinctUntilChanged() // Only emit distinct values
                    .collect { query ->
                        quickInvoiceViewModel.getClients(query )
                    }
            }

            invalidInput.observe(viewLifecycleOwner){
                handleInputErrorInTopSheet(it , binding.topSheet)
            }

            invoiceDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){ response ->
                when (response) {
                    is InvoiceState.Loading -> handleLoadingInTopSheet()
                    is InvoiceState.Idle -> handleIdleInTopSheet()
                    is InvoiceState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is InvoiceState.StateError -> handleError(response.message.toString())
                    is InvoiceState.InputError -> handleInputErrorInTopSheet(response.inputError , binding.topSheet, false )
                    is InvoiceState.SuccessShowSingleInvoice -> handleInvoiceSuccess()
                    is InvoiceState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {}
                }
            }
        }

    }

    private fun openQuickInvoiceScanner(){
        if (Build.VERSION.SDK_INT >= 32){
            barcodeQuickInvoiceLauncher.launch(ScanOptions())
        }
        else {
            if (checker?.checkPermissionCamera(activity) == true) {
                checker?.askForPermissionCamera(activity)
            } else {
                barcodeQuickInvoiceLauncher.launch(ScanOptions())
            }
        }
    }
    private val barcodeQuickInvoiceLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(
                requireContext(),
                getString(com.codeIn.common.R.string.scanner_cancelled),
                Toast.LENGTH_LONG
            ).show()
        } else {
            binding.topSheet.productNameEditText.setText(result.contents)
        }
    }
    private fun handleInvoiceSuccess(){
        quickInvoiceViewModel.clearStateClient()
        quickInvoiceViewModel.clearStateInvoice()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        binding.topSheet.productNameEditText.setText("")
        binding.topSheet.amountEditText.setText("")
        binding.topSheet.priceEditText.setText("")
        binding.topSheet.notTaxableRB.isChecked = true
        quickInvoiceViewModel.clearSelectedCart()
        if (topSheetBehavior?.isClosable == true)
            topSheetBehavior?.state = TopSheetBehavior.STATE_COLLAPSED
    }

    private val nearPayListener = object : NearPayPayment.Companion.Listener{
        override fun onPurchaseProcess(
            code: Int,
            transactionData: TransactionData?,
            purchaseFailure: PurchaseFailure?,
            cashValue: String?,
            visaValue: String?
        ) {
            if (code == 0) {
                quickInvoiceViewModel.makeQuickInvoice(
                    cashValue,
                    visaValue,
                    date = binding.topSheet.invoicePaymentDateEditText.text.toString(),
                    null,
                    transactionData?.receipts?.get(0)?.transaction_uuid,
                    transactionData?.receipts?.get(0)?.approval_code?.value.toString(),
                    transactionData?.receipts?.get(0)?.created_at,
                )
            }
            else {
                when (code) {
                    1 -> {
                        CustomToaster.show(
                            requireContext() ,
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
            TODO("Not yet implemented")
        }

        override fun onReconciliationProcess(
            code: Int,
            receipt: ReconciliationReceipt?,
            reconcileFailure: ReconcileFailure?
        ) {
            TODO("Not yet implemented")
        }

    }


    fun addProductInQuickInvoice( ){
        var taxable = Taxable.NO.value
        var taxType = 0
        if (!binding.topSheet.notTaxableRB.isChecked)
        {
            taxable = Taxable.YES.value
        }
        if (binding.topSheet.taxablePriceIncludesTaxRB.isChecked)
            taxType = TaxType.INCLUDED.value
        else if (binding.topSheet.taxablePriceExcludesTaxRB.isChecked)
            taxType = TaxType.Excludes.value

        val product = ProductInQuickInvoice(
            binding.topSheet.productNameEditText.text.toString(),
            binding.topSheet.amountEditText.text.toString(),
            binding.topSheet.priceEditText.text.toString(),
            taxable.toString() ,
            taxType.toString()
        )

        quickInvoiceViewModel.handleProductInCart(product , Cart.ADD)
        binding.topSheet.productNameEditText.setText("")
        binding.topSheet.amountEditText.setText("")
        binding.topSheet.priceEditText.setText("")
        binding.topSheet.notTaxableRB.isChecked = true

        binding.topSheet.products.adapter?.notifyDataSetChanged()
    }
    private fun handleClientSuccess(clientModel: ClientModel?) {
        quickInvoiceViewModel.clearStateClient()
        quickInvoiceViewModel.clearStateInvoice()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        quickInvoiceViewModel.updateClientIdInInvoiceData(clientModel?.id.toString())
    }

    private fun openScanner(){
        if (Build.VERSION.SDK_INT >= 32){
            barcodeLauncher.launch(ScanOptions())
        }
        else {
            if (checker?.checkPermissionCamera(activity) == true) {
                checker?.askForPermissionCamera(activity)
            } else {
                barcodeLauncher.launch(ScanOptions())
            }
        }
    }
    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(
                requireContext(),
                getString(com.codeIn.common.R.string.scanner_cancelled),
                Toast.LENGTH_LONG
            ).show()
        } else {
            binding.searchView.setQuery(result.contents , false )
        }
    }

    private val productCommunicator = object : ProductsAdapter.Communicator {
        override fun addProductToInvoice(product: ProductModel) {
            if ((requireActivity() as MainActivity).viewModel.isCompleteInfo.value == true ) {
                productsViewModel.handleProductInCart(product , Cart.ADD)
            } else {
                CustomToaster.show(
                    requireContext() ,
                    getString(R.string.please_complete_your_info_before_make_any_invoice),
                    false
                )
            }
        }

        override fun removeProductFromInvoice(product: ProductModel) {
            if ((requireActivity() as MainActivity).viewModel.isCompleteInfo.value == true ) {
                if (product.count == 0)
                    productsViewModel.handleProductInCart(product , Cart.DELETE)
                else
                    productsViewModel.handleProductInCart(product , Cart.ADD)
            }
            else {
                CustomToaster.show(
                    requireContext() ,
                    getString(R.string.please_complete_your_info_before_make_any_invoice),
                    false
                )
            }
        }


        override fun removeAllProductFromInvoice(product: ProductModel) {
            if ((requireActivity() as MainActivity).viewModel.isCompleteInfo.value == true ) {
                productsViewModel.handleProductInCart(product , Cart.DELETE_ALL)
            }
            else {
                CustomToaster.show(
                    requireContext() ,
                    getString(R.string.please_complete_your_info_before_make_any_invoice),
                    false
                )
            }
        }


        override fun showProductOptions(product: ProductModel){
            if ((requireActivity() as MainActivity).viewModel.isCompleteInfo.value == true ) {
                showOptions(product)
            }
            else {
                CustomToaster.show(
                    requireContext() ,
                    getString(R.string.please_complete_your_info_before_make_any_invoice),
                    false
                )
            }
        }
    }


    private val productInQuickInvoiceCommunicator = object : QuickInvoiceProductsAdapter.Communicator {
        override fun removeProductFromInvoice(product: ProductInQuickInvoice) {
            quickInvoiceViewModel.handleProductInCart(product , Cart.DELETE)
        }

        override fun updateProductInInvoice(product: ProductInQuickInvoice) {
            quickInvoiceViewModel.handleProductInCart(product , Cart.UPDATE)
        }
    }

    private val categoryCommunicator = object : CategoriesAdapter.Communicator {
        override fun setCategory(category: CategoryData) {
            productsViewModel.updateSelectedCategoryId(categoryId = category.id.toString())
        }
    }

    private val categoryMainCommunicator = object : CategoriesMainAdapter.Communicator {
        override fun setCategory(category: CategoryData) {
            binding.categoryMainBackArrow.invisible()
            productsViewModel.getProducts( category.id.toString() , true )
        }
    }

    private var productOptionsDialog: ProductOptionsDialog? = null
    private fun showOptions(product: ProductModel) {
        productOptionsDialog?.dismiss()
        productOptionsDialog = ProductOptionsDialog(requireContext(),
            object : ProductOptionsDialog.Communicator {
                override fun editProduct() {
                    findNavController().navigate(
                        com.codeIn.myCash.ui.home.products.products.ProductsFragmentDirections.actionNavigationProductsFragmentToUpdateProductFragment(
                            product.id.toString()
                        )
                    )
                }

                override fun deleteProduct() {
                    showConfirmDelete(product)
                }

            })
        productOptionsDialog?.show()
    }

    private var confirmDeleteProductDialog: ConfirmDeleteProductDialog? = null
    private fun showConfirmDelete(product: ProductModel) {
        confirmDeleteProductDialog?.dismiss()
        confirmDeleteProductDialog = ConfirmDeleteProductDialog(requireContext(),
            object : ConfirmDeleteProductDialog.Communicator {
                override fun deleteProduct() {
                    productsViewModel.deleteProduct(product.id.toString())
                }

            })
        confirmDeleteProductDialog?.show()
    }

    private var clientInfoDialog: ClientInfoDialog? = null
    private lateinit var arrayAdapter : SearchClientsAutoCompleteTextViewAdapter

    private fun showAddClientToInvoiceDialog() {
        arrayAdapter = SearchClientsAutoCompleteTextViewAdapter(
            requireContext() , ArrayList<ClientModel>()
        )
        if (clientInfoDialog?.isShowing == true)
            clientInfoDialog?.dismiss()
        clientInfoDialog = ClientInfoDialog(
            context= requireContext(),
            createClientValidationUseCase = quickInvoiceViewModel.createClientValidationUseCase,
            vendorValidationUseCase= quickInvoiceViewModel.createVendorValidationUseCase ,
            invoiceType = quickInvoiceViewModel.selectedProductsInCart.value?.invoiceType ?: InvoiceType.SIMPLE,
            mainTypeInvoice = quickInvoiceViewModel.selectedProductsInCart.value?.mainTypeInvoice ?: MainTypeInvoice.PURCHASE,
            clientRequest = quickInvoiceViewModel.selectedProductsInCart.value?.clientRequest,
            arrayAdapter = arrayAdapter ,
            communicator = object : ClientInfoDialog.Communicator {
                override fun addClient(clientRequest: ClientRequest? ) {
                    if (clientRequest == null)
                        binding.topSheet.addClientCheckBox.isChecked = false
                    else
                    {
                        binding.topSheet.addClientCheckBox.isChecked = true
                        quickInvoiceViewModel.updateClientRequest(clientRequest)
                        if (clientRequest.id == null ){
                            quickInvoiceViewModel.createClient()
                        }
                        else{
                            quickInvoiceViewModel.updateClientIdInInvoiceData(clientRequest.id.toString())
                        }
                    }
                }

                override fun searchClient(search: String?) {
                    quickInvoiceViewModel.updateSearch(search)
                }

            })
        clientInfoDialog?.show()
    }

    private var productFilterDialog: ProductsFilterDialog? = null
    private fun showFilterDialog() {
        productFilterDialog?.dismiss()
        productFilterDialog = ProductsFilterDialog(
            context = requireContext(),
            fragmentManager = childFragmentManager,
            communicator = filterDialogCommunicator ,
            productsViewModel.productFilter.value
        )
        productFilterDialog?.show()
    }

    private val filterDialogCommunicator = object : ProductsFilterDialog.Communicator {
        override fun applyFilter(filter: ProductFilter) {
            productsViewModel.updateProductFilter(filter)
        }
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard(requireContext() , view)
        if (findNavController().currentBackStackEntry?.savedStateHandle?.contains("products_in_cart") == true){
            val products = findNavController().currentBackStackEntry?.savedStateHandle?.get("products_in_cart") ?: ProductsInCart()
            productsViewModel.updateSelectedProductDependOnBack(products)
        }
        else {
            productsViewModel.getProducts()
        }
        (requireActivity() as MainActivity).setOnBackPressedCallback(onBackPressedCallback)

    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.remove()
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        binding.loadingLayout.root.gone()
        binding.topSheet.loadingLayout.root.gone()

        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    private fun handleLoadingInTopSheet() = binding.topSheet.loadingLayout.root.visible()

    private fun handleIdleInTopSheet() = binding.topSheet.loadingLayout.root.gone()

    private fun handleSuccess(message: String) {
        binding.loadingLayout.root.gone()
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = true
        )
    }

    @Suppress("DEPRECATION")
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d("TAG", "handleOnBackPressed: ")
            if (topSheetBehavior?.state == TopSheetBehavior.STATE_EXPANDED && topSheetBehavior?.isClosable == true) {
                topSheetBehavior?.state = TopSheetBehavior.STATE_COLLAPSED
            } else
                if (isEnabled) {
                    // No more fragments in the back stack, handle back press as normal
                    Log.d(
                        "TAG",
                        "No more fragments in the back stack, handle back press as normal: "
                    )
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
        }
    }
}