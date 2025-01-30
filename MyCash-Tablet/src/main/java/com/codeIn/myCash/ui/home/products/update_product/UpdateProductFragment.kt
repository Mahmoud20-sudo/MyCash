package com.codeIn.myCash.ui.home.products.update_product

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Discount
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.Request
import com.codeIn.common.data.TaxType
import com.codeIn.common.data.Taxable
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.scanner.QRCodeScannerActivity
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentEditProductBinding
import com.codeIn.myCash.ui.authentication.showError
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.AddCategoriesAdapter
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.SearchCategoryAutoCompleteTextViewAdapter
import com.codeIn.myCash.ui.home.products.add_new_product.dialog.ConfirmDeleteCategoryDialog
import com.codeIn.myCash.ui.home.products.add_new_product.dialog.DiscountOptionsDialog
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.pickers.ImagePickerBottomSheet
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class UpdateProductFragment : Fragment() {
    private var productImage: File? = null
    private var _binding: FragmentEditProductBinding? = null
    private val infoDialog: InfoDialog = InfoDialog()
    private var discountOptionsDialog: DiscountOptionsDialog? = null
    private val binding get() = _binding!!
    private val checker = PermissionChecker()
    private val viewModel : UpdateProductViewModel by viewModels()
    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (productImage != null)
            binding.productImageView.setImageURI(productImage?.toUri())
        else
            binding.productImageView.setImageResource(R.drawable.ic_images_placeholder)


        binding.apply {
            scanner.setOnClickListener {
                openScanner()
            }

            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            productImageView.setOnClickListener {
                if (checker?.checkPermissionCamera(activity) == true) {
                    checker?.askForPermissionCamera(activity)
                }
                else {
                    ImagePickerBottomSheet(communicator = imagePickerCommunicator).show(
                        childFragmentManager,
                        ImagePickerBottomSheet.TAG
                    )
                }

            }
            addButton.setOnClickListener {
                var taxable = Taxable.NO.value
                var taxType = 0
                var purchaseTaxable = Taxable.NO.value
                var purchaseTaxType = 0
                val hasDiscount = if (viewModel.discountType.value == Discount.None)
                    0
                else
                    1
                val discountType = when (viewModel.discountType.value) {
                    Discount.Value -> Discount.Value.value
                    Discount.Percentage -> Discount.Percentage.value
                    else -> 0
                }
                if (!notTaxableRB.isChecked)
                    taxable = Taxable.YES.value

                if (!notTaxableRBPurchase.isChecked)
                    purchaseTaxable = Taxable.YES.value

                if (taxablePriceIncludesTaxRB.isChecked)
                    taxType = TaxType.INCLUDED.value
                else if (taxablePriceExcludesTaxRB.isChecked)
                    taxType = TaxType.Excludes.value

                if (taxablePriceIncludesTaxRBPurchase.isChecked)
                    purchaseTaxType = TaxType.INCLUDED.value
                else if (taxablePriceExcludesTaxRBPurchase.isChecked)
                    purchaseTaxType = TaxType.Excludes.value

                hideKeyboard(requireContext(), view)
                viewModel.updateProduct(
                    name = productNameEditText.text.toString() , barcode = productBarcodeEditText.text.toString() ,
                    description = productDescEditText.text.toString() , price = priceEditText.text.toString() ,
                    discount = discountEditText.text.toString() , discountType = discountType , hasDiscount = hasDiscount , taxable = taxable , quantity = amountEditText.text.toString() ,
                    taxType = taxType ,  imageFile = productImage , buyPrice = costEditText.text.toString() ,
                    buyTaxType = purchaseTaxType , buyTaxAvailable = purchaseTaxable
                )
            }

            discountPicker.setOnClickListener {
                showDiscountOptionsDialog()
            }

        }

        viewModel.apply {


            selectedCategories.observe(viewLifecycleOwner){
                if (it?.isNotEmpty() == true) {
                    adapterSubCategory = UpdatesCategoriesAdapter(selectedCategories , requireContext(),
                        viewModel , viewLifecycleOwner , listener = catgeoryListener)
                    binding.categories.adapter = adapterSubCategory
                }
            }
            discountType.collectOnLifecycle(viewModelScope , viewLifecycleOwner){value ->
                when(value){
                    Discount.None -> {
                        binding.discountEditText.isEnabled = false
                        binding.discountEditText.setText("")
                    }
                    Discount.Value ->{
                        binding.discountEditText.isEnabled = true
                    }
                    Discount.Percentage ->{
                        binding.discountEditText.isEnabled = true
                    }
                }
            }
            labelDiscount.collectOnLifecycle(viewModelScope , viewLifecycleOwner ){
                binding.currency.text = it
            }
            dataResult.observe(viewLifecycleOwner){response->
                when(response){
                    is CategoryState.SuccessUpdateProduct->handleSuccess()
                    is CategoryState.Loading -> handleLoading()
                    is CategoryState.Idle -> handleIdle()
                    is CategoryState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is CategoryState.InputError -> handleInputError(response.inputError)
                    is CategoryState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else ->{

                    }
                }
            }
            productDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ProductsState.SuccessDetailsProduct->handleProductData(response.data)
                    is ProductsState.Loading -> handleLoading()
                    is ProductsState.Idle -> handleIdle()
                    is ProductsState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ProductsState.InputError -> handleInputError(response.inputError)
                    is ProductsState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else ->{

                    }
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

    private fun handleInputError(invalidInput: InvalidInput) {
        binding.loadingLayout.root.gone()
        when (invalidInput) {
            InvalidInput.PRICE -> {
                binding.priceEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_price),
                    isSuccess = false
                )
            }

            InvalidInput.QUANTITY -> {
                binding.amountEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_quantity),
                    isSuccess = false
                )

            }
            InvalidInput.BUY_PRICE -> {
                binding.costEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_price),
                    isSuccess = false
                )
            }
            InvalidInput.BUY_PRICE_SMALL -> {
                binding.costEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_cost_smaller_than_price),
                    isSuccess = false
                )
            }
            InvalidInput.DISCOUNT_VALUE -> {
                binding.discountEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_discount_value),
                    isSuccess = false
                )
            }
            InvalidInput.DISCOUNT_PERCENTAGE -> {
                binding.discountEditText.showError()
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_enter_valid_discount_percentage),
                    isSuccess = false
                )
            }
            InvalidInput.EMPTY -> {
                CustomToaster.show(
                    requireContext(),
                    getString(R.string.please_fill_all_the_fields),
                    isSuccess = false
                )
            }

            else -> {}
        }
    }

    private fun handleSuccess() {
        binding.loadingLayout.root.gone()
        CustomToaster.show(
            context = requireContext(),
            message = getString(R.string.success_message),
            isSuccess = true
        )
        findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_cart", null)
        findNavController().popBackStack()
    }

    private fun handleProductData(productModel : ProductModel?){
        binding.apply {
            productNameEditText.setText(productModel?.name)
            productBarcodeEditText.setText(productModel?.barCode)
            productDescEditText.setText(productModel?.description)
            priceEditText.setText(productModel?.price)
            amountEditText.setText(productModel?.quantity)
            costEditText.setText(productModel?.buyPrice)
            if (productModel?.hasDiscount == "1"){
                if (productModel?.discountType == Discount.Value.value.toString()){
                    discountEditText.setText(productModel?.discount)
                    viewModel.updateDiscount(Discount.Value.value)
                }
                else if (productModel?.discountType == Discount.Percentage.value.toString()){
                    discountEditText.setText(productModel?.discount)
                    viewModel.updateDiscount(Discount.Percentage.value)
                }
            }

            if (productModel?.taxAvailable == Taxable.YES.value.toString()){
                if (productModel.taxType == TaxType.Excludes.value.toString()){
                    taxablePriceExcludesTaxRB.isChecked = true
                }
                else if (productModel.taxType == TaxType.INCLUDED.value.toString()){
                    taxablePriceIncludesTaxRB.isChecked = true
                }
            }
            else
            {
                notTaxableRB.isChecked = true
            }

            if (productModel?.buyTaxAvailable == Taxable.YES.value.toString()){
                if (productModel.buyTaxType == TaxType.Excludes.value.toString()){
                    taxablePriceExcludesTaxRBPurchase.isChecked = true
                }
                else if (productModel.buyTaxType == TaxType.INCLUDED.value.toString()){
                    taxablePriceIncludesTaxRBPurchase.isChecked = true
                }
            }
            else
            {
                notTaxableRBPurchase.isChecked = true
            }


            if (productModel?.selectedCategories?.isNotEmpty() == true)
                viewModel.getSelectedCategories(productModel?.selectedCategories!!)
            else
                viewModel.addNewCategory(CategoryModel(isFirst = true) )

            Glide.with(requireContext())
                .load(productModel?.image)
                .error(R.drawable.icon_app)
                .into(binding.productImageView)
        }
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
            binding.productBarcodeEditText.setText(result.contents)
        }
    }
    private val imagePickerCommunicator = object : ImagePickerBottomSheet.Communicator {
        override fun setImage(file: File) {
            productImage = file
            binding.productImageView.setImageURI(file.toUri())
        }
    }

    private var confirmDeleteCategoryDialog: ConfirmDeleteCategoryDialog? = null

    private fun showConfirmDeleteCategory(position : Int) {
        confirmDeleteCategoryDialog?.dismiss()
        confirmDeleteCategoryDialog = ConfirmDeleteCategoryDialog(binding.root.context ,deleteSubCategories , position)
        confirmDeleteCategoryDialog?.show()
    }

    private val catgeoryListener = object  : UpdatesCategoriesAdapter.Listener {
        override fun onDeleteClick(position: Int) {
            showConfirmDeleteCategory(position)
        }

    }
    private val deleteSubCategories = object : ConfirmDeleteCategoryDialog.Listener {
        override fun deleteCategories(position: Int) {
            viewModel.deleteCategories(position)
        }

    }

    private val discountListener = object : DiscountOptionsDialog.Listener {
        override fun onDiscountTypeSelected(discount: Discount) {
            viewModel.updateDiscount(discount.value)
        }
    }

    private fun showDiscountOptionsDialog() {
        discountOptionsDialog?.dismiss()
        discountOptionsDialog = DiscountOptionsDialog(requireContext(), viewModel.discountType.value, discountListener)
        discountOptionsDialog?.show()
    }

}