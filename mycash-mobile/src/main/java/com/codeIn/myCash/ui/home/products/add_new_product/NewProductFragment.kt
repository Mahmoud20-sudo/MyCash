package com.codeIn.myCash.ui.home.products.add_new_product

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.Discount
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.TaxType
import com.codeIn.common.data.Taxable
import com.codeIn.common.domain.model.AutoCompleteModelSearch
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentNewProductBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.ui.showError
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.AddCategoriesAdapter
import com.codeIn.myCash.ui.home.products.add_new_product.dialog.ConfirmDeleteCategoryDialog
import com.codeIn.myCash.ui.home.products.add_new_product.dialog.DiscountOptionsDialog
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.pickers.ImagePickerBottomSheet
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.SearchCategoryAutoCompleteTextViewAdapter
import com.codeIn.myCash.ui.setErrorMsg
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class NewProductFragment : Fragment() {
    private var productImage: File? = null
    private var _binding: FragmentNewProductBinding? = null
    private val infoDialog: InfoDialog = InfoDialog()
    private var discountOptionsDialog: DiscountOptionsDialog? = null
    private val binding get() = _binding!!
    private val checker = PermissionChecker()
    private val viewModel : NewProductViewModel by viewModels()
    private lateinit var adapterBranches : SearchCategoryAutoCompleteTextViewAdapter

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewProductBinding.inflate(inflater, container, false)
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

            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }

            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            productImageView.setOnClickListener {
                if (checker?.checkPermissionCamera(activity) == true) {
                    checker?.askForPermissionCamera(activity)
                } else {
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
                viewModel.addNewProduct(
                    name = productNameEditText.text, barcode = productBarcodeEditText.text,
                    description = productDescEditText.text, price = priceEditText.text,
                    discount = discountEditText.text.toString() , discountType = discountType , hasDiscount = hasDiscount ,
                    taxable = taxable , quantity = amountEditText.text, taxType = taxType ,
                    imageFile = productImage , buyPrice = costEditText.text,
                    buyTaxType = purchaseTaxType , buyTaxAvailable = purchaseTaxable
                )
            }

            discountPicker.setOnClickListener {
                showDiscountOptionsDialog()
            }

            branch.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                val autoCompleteText = adapterView.getItemAtPosition(i) as AutoCompleteModelSearch
                binding.branch.setText(autoCompleteText.name)
                viewModel.branchId = autoCompleteText.id.toString()
            }

            branch.setOnTouchListener { _, _ ->
                branch.showDropDown()
                true
            }
        }

        viewModel.apply {
            selectedCategories.observe(viewLifecycleOwner){
                if (it?.isNotEmpty() == true) {
                    adapterSubCategory = AddCategoriesAdapter(selectedCategories , requireContext(),
                        viewModel , viewLifecycleOwner , listener = catgeoryListener)
                    binding.categories.adapter = adapterSubCategory
                }
            }

            branchesList.observe(viewLifecycleOwner){ branches ->
                if(branches.isNullOrEmpty()) return@observe
                adapterBranches = SearchCategoryAutoCompleteTextViewAdapter(binding.root.context, branches.map { BranchesDTO.Data.Data.ModelMapper.from(it) }.toList())
                binding.branch.setAdapter(adapterBranches)
                binding.branch.setText(branches.first{ it.isMain == 1}.name)
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
            currencyDiscount.collectOnLifecycle(viewModelScope , viewLifecycleOwner ){
                binding.currency.text = it
            }
            dataResult.observe(viewLifecycleOwner){response->
                when(response){
                    is CategoryState.SuccessDetailsProduct->handleSuccess()
                    is CategoryState.Loading -> handleLoading()
                    is CategoryState.Idle -> handleIdle()
                    is CategoryState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is CategoryState.InputError -> handleInputError(response.inputError)
                    is CategoryState.StateError -> handleError(response.message.toString())
                    is CategoryState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
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
                binding.priceEditText.setErrorMsg(getString(R.string.please_enter_valid_price))
            }
            InvalidInput.BUY_PRICE -> {
                binding.costEditText.setErrorMsg( getString(R.string.please_enter_valid_price))
            }
            InvalidInput.BUY_PRICE_SMALL -> {
                binding.costEditText.setErrorMsg( getString(R.string.please_enter_cost_smaller_than_price))
            }
            InvalidInput.QUANTITY -> {
                binding.amountEditText.setErrorMsg( getString(R.string.please_enter_valid_quantity))
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
        findNavController().popBackStack()
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
            binding.productBarcodeEditText.binding.editText.setText(result.contents)
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

    private val catgeoryListener = object  : AddCategoriesAdapter.Listener{
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