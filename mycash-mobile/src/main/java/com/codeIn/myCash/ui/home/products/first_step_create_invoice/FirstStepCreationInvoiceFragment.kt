package com.codeIn.myCash.ui.home.products.first_step_create_invoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.Cart
import com.codeIn.common.data.Discount
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentInvoiceDetailsBinding
import com.codeIn.myCash.ui.ViewStrokes
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.pdf_manager.PdfData
import com.codeIn.myCash.utilities.pdf_manager.PdfInvoiceInformation
import com.codeIn.myCash.utilities.pdf_manager.PdfInvoiceSummary
import com.codeIn.myCash.utilities.pdf_manager.PdfManager
import com.codeIn.myCash.utilities.pdf_manager.PdfProductDetailsTotal
import com.codeIn.myCash.utilities.pdf_manager.PdfSellerInformation
import com.codeIn.myCash.ui.home.products.first_step_create_invoice.dialog.DiscountOptionsInInvoiceDialog
import com.codeIn.myCash.ui.home.products.first_step_create_invoice.dialog.PurchaseInvoiceDialog
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.domain.invoice.model.DiscountInInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.model.PurchaseInvoiceModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirstStepCreationInvoiceFragment : Fragment() {
    private val viewModel: FirstStepCreationInvoiceViewModel by viewModels()
    private var _binding: FragmentInvoiceDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInvoiceDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().onBackPressedDispatcher.addCallback() {
//            findNavController().previousBackStackEntry?.savedStateHandle?.set(
//                "products_in_cart",
//                viewModel.selectedProductsInCart.value)
//            findNavController().popBackStack()
//        }

        binding.apply {
            binding.productRv.itemAnimator = null
            backArrow.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_cart", viewModel.selectedProductsInCart.value)
                findNavController().popBackStack()
            }
            simpleInvoiceTv.setOnClickListener {
                viewModel.updateInvoiceType(InvoiceType.SIMPLE)
            }
            taxInvoiceTv.setOnClickListener {
                viewModel.updateInvoiceType(InvoiceType.TAX)
            }
            purchaseInvoiceTextView.setOnClickListener {
                showPurchaseInvoiceDialog()
            }
            saleInvoiceTv.setOnClickListener {
                viewModel.updateMainInvoiceType(MainTypeInvoice.SALE)
            }
            payInvoiceButton.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.navigation_firstStepCreationInvoiceFragment)
                    findNavController().navigate(
                        FirstStepCreationInvoiceFragmentDirections.actionNavigationFirstStepCreationInvoiceFragmentSecondStepCreationInvoiceFragment(
                            viewModel.selectedProductsInCart.value!!
                        ))
            }
            backButton.setOnClickListener {
                hideKeyboard(requireContext() , view)
                findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_cart", viewModel.selectedProductsInCart.value)
                findNavController().popBackStack()
            }
        }

        viewModel.apply {
            selectedProductsInCart.observe(viewLifecycleOwner){
                val productsAdapter =
                    InvoiceProductsAdapter(requireContext(), productCommunicator, currency ,
                        viewModel.invoiceType.value?.toString() ?: MainTypeInvoice.SALE.value.toString())

                if (it?.list?.isNotEmpty() == true) {

                    productsAdapter.submitList(it?.list)
                    binding.productRv.adapter = productsAdapter
                    showPricesSummary(it)
                }
                else {
                    findNavController().popBackStack()
                }

            }

            invoiceType.observe(viewLifecycleOwner){
                updateInvoiceTypeView(it)
            }
            mainInvoiceType.observe(viewLifecycleOwner){
                updateMainInvoiceTypeView(it)
                viewModel.handleProductInCart(null , Cart.INITIAL)
            }
        }

    }

    private fun showPricesSummary(productsInCart: ProductsInCart?){
        binding.apply {
            initialTotalTextView.text = productsInCart?.initialTotal ?: "0.0"
            vat15TextView.text = productsInCart?.tax ?: "0.0"
            discountTextView.text = productsInCart?.discount ?: "0.0"
            totalPriceTextView.text = productsInCart?.finalTotal ?: "0.0"
            totalAfterDiscountTextView.text = productsInCart?.totalAfterDiscount ?: "0.0"
            vatLabel.text = "${getString(R.string.tax)} (${viewModel.tax}%)"
        }
    }
    private var purchaseInvoiceDialog: PurchaseInvoiceDialog? = null
    private fun showPurchaseInvoiceDialog() {
        purchaseInvoiceDialog?.dismiss()
        purchaseInvoiceDialog = PurchaseInvoiceDialog(
            context = requireContext(),
            fragmentManager = childFragmentManager,
            validationUseCase = viewModel.makePurchaseInvoiceValidationUseCase,
            purchaseInvoiceModel = viewModel.selectedProductsInCart.value?.purchaseInvoiceModel?:
            PurchaseInvoiceModel("" ,"" , ""),
            communicator = object : PurchaseInvoiceDialog.Communicator {
                override fun addInvoice(purchaseInvoiceModel: PurchaseInvoiceModel) {
                    viewModel.updatePurchaseInvoice(purchaseInvoiceModel)
                    viewModel.updateMainInvoiceType(MainTypeInvoice.PURCHASE)
                }
            }
        )
        purchaseInvoiceDialog?.show()
    }

    private var discountOptionsInInvoiceDialog: DiscountOptionsInInvoiceDialog? = null
    private fun showDiscountOptionsInInvoiceDialog(productModel: ProductModel) {
        discountOptionsInInvoiceDialog?.dismiss()
        if (productModel.discountInInvoiceModel == null )
            productModel.discountInInvoiceModel = DiscountInInvoiceModel(Discount.None , "")
        discountOptionsInInvoiceDialog = DiscountOptionsInInvoiceDialog(
            context = requireContext(),
            productModel = productModel,
            currencyTxt = viewModel.currency,
            validationDiscountUseCase = viewModel.validationDiscountUseCase,
            listener = object : DiscountOptionsInInvoiceDialog.Listener {
                override fun onSaveDiscount(discount: DiscountInInvoiceModel) {
                    productModel.discountInInvoiceModel = discount
                    viewModel.handleProductInCart(productModel , Cart.ADD)
                }
            }
        )
        discountOptionsInInvoiceDialog?.show()
    }

    private fun updateInvoiceTypeView(invoiceType: InvoiceType) {

        val context = requireContext()
        val binding = binding

        val viewsToStyle = listOf(
            binding.simpleInvoiceTv,
            binding.taxInvoiceTv,
        )

        val selectedView = when (invoiceType) {
            InvoiceType.SIMPLE -> binding.simpleInvoiceTv
            InvoiceType.TAX -> binding.taxInvoiceTv
            else -> binding.simpleInvoiceTv
        }
        updateSectionsViews(context, viewsToStyle, selectedView, stroke = ViewStrokes.R12_S1)
    }

    private fun updateMainInvoiceTypeView(invoiceType: MainTypeInvoice) {

        val context = requireContext()
        val binding = binding

        val viewsToStyle = listOf(
            binding.saleInvoiceTv,
            binding.purchaseInvoiceTextView,
        )

        val selectedView = when (invoiceType) {
            MainTypeInvoice.PURCHASE -> binding.purchaseInvoiceTextView
            MainTypeInvoice.SALE -> binding.saleInvoiceTv
        }
        when (invoiceType) {
            MainTypeInvoice.PURCHASE ->  binding.invoiceTypeTv.text = getString(R.string.invoice_type)+" "+getString(R.string.purchase_invoice)
            MainTypeInvoice.SALE -> binding.invoiceTypeTv.text = getString(R.string.invoice_type)+" "+getString(R.string.sale_invoice)
        }
        updateSectionsViews(context, viewsToStyle, selectedView, stroke = ViewStrokes.R12_S1)
    }


    private val productCommunicator = object : InvoiceProductsAdapter.Communicator {
        override fun addProductToInvoice(product: ProductModel) =
           viewModel.handleProductInCart(product , Cart.ADD)

        override fun removeProductFromInvoice(product: ProductModel) =
            viewModel.handleProductInCart(product , Cart.DELETE)

        override fun updateProductInInvoice(product: ProductModel) =
            viewModel.handleProductInCart(product, Cart.ADD)

        override fun removeAllProductFromInvoice(product: ProductModel) =
            viewModel.handleProductInCart(product, Cart.DELETE_ALL)

        override fun addDiscount(product: ProductModel) {
            if (viewModel.mainInvoiceType.value?.value == MainTypeInvoice.SALE.value)
                showDiscountOptionsInInvoiceDialog(product)
        }


    }

    override fun onResume() {
        super.onResume()
        findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_cart", null)

    }

}