
package com.codeIn.myCash.ui.home.invoices.addMoreProductsToMemorandum

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Cart
import com.codeIn.common.data.Request
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.scanner.QRCodeScannerActivity
import com.codeIn.common.util.PermissionChecker
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.getQueryTextChangeStateFlow
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentAddNoticeProductsBinding
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddMoreProductsToMemorandumFragment : Fragment() {

    private val viewModel: AddMoreProductsToMemorandumViewModel by viewModels()

    private var _binding: FragmentAddNoticeProductsBinding? = null
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
        _binding = FragmentAddNoticeProductsBinding.inflate(inflater, container, false)
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
            addButton.setOnClickListener {
                viewModel.getSelectedProducts()
                viewModel.viewModelScope.launch {
                    delay(100)
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_memorandum", viewModel.selectedProductsInCart.value)
                    findNavController().popBackStack()
                }

            }
            cancelButton.setOnClickListener {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("products_in_memorandum", null)
                findNavController().popBackStack()
            }

            searchView
                .getQueryTextChangeStateFlow()
                .debounce(AppConstants.DELAY_TIME_SEARCH)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .onEach { query ->
                    viewModel.getProducts(query)
                }.launchIn(viewModel.viewModelScope)

         scannerImageView.setOnClickListener {
             openScanner()
         }
        }

        viewModel.apply {

            val adapter = InvoiceProductsAdapter(
                communicator = productsCommunicator ,
                currency = viewModel.currency ,
                context = requireContext())

            binding.productRecycleView.adapter = adapter

            productsDataResult.collectOnLifecycle(viewModelScope , viewLifecycleOwner){response->
                when(response){
                    is ProductsState.SuccessShowProducts ->{
                        adapter.submitList(response.data?.data)
                    }
                    is ProductsState.Loading -> handleLoading()
                    is ProductsState.Idle -> handleIdle()
                    is ProductsState.UnAuthorized -> {
                        prefs.putValue(Constants.getToken() , "")
                        val intent = Intent(activity , IntroActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    is ProductsState.StateError -> handleError(response.message.toString())
                    is ProductsState.ServerError -> handleError(response.error.getErrorMessage(requireContext()))
                    else -> {

                    }

                }

            }
        }
    }

    private fun handleLoading() = binding.loadingLayout.root.visible()

    private fun handleIdle() = binding.loadingLayout.root.gone()

    private fun handleError(message: String) {
        CustomToaster.show(
            context = requireContext(),
            message = message,
            isSuccess = false
        )
    }

    private val productsCommunicator = object : InvoiceProductsAdapter.Communicator {

        override fun addProductToMemorandum(product: ProductModel) {
            viewModel.handleProductInCart(product , Cart.ADD)
        }

        override fun updateProductInMemorandum(product: ProductModel) {
            viewModel.handleProductInCart(product , Cart.ADD)
        }

        override fun removeProductFromMemorandum(product: ProductModel) {
            viewModel.handleProductInCart(product , Cart.ADD)
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
            CustomToaster.show(
                requireContext(),
                getString(com.codeIn.common.R.string.scanner_cancelled),
               isSuccess = false
            )
        } else {
            binding.searchView.setQuery(result.contents , false )
        }
    }
}
