package com.codeIn.myCash.ui.home.invoices.newPurchaseInvoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentNewPurchaseInvoiceBinding

import com.codeIn.myCash.ui.home.products.first_step_create_invoice.InvoiceProductsAdapter
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel

/**
 * A simple [Fragment] subclass.
 *
 */
class NewPurchaseInvoiceFragment : Fragment() {


    private val viewModel: NewPurchaseFirstStepCreationInvoiceViewMode by viewModels()


    private var _binding: FragmentNewPurchaseInvoiceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPurchaseInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //cancel operation and pop the back stack
        binding.apply {
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        // set the invoice products recycler view adapter
        val productsAdapter = InvoiceProductsAdapter(requireContext(), productCommunicator  , "" , "")
//        productsAdapter.submitList(viewModel.productsList)
        binding.productRecycleView.adapter = productsAdapter

        // set the back arrow click listener to navigate back to the previous fragment
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }


        // set the pay invoice button click listener. Navigate to the payment process fragment
//        binding.proceedButton.setOnClickListener {
//            if (findNavController().currentDestination?.id == R.id.navigation_newPurchaseInvoiceFragment)
//                findNavController().navigate(
//                    NewPurchaseInvoiceFragmentDirections.actionNavigationNewPurchaseInvoiceFragmentToNavigationNewPurchaseInvoicePaymentFragment(
//                        viewModel.productsList
//                    )
//                )
//        }


        // observe the view model live vendorsData to update the invoice details ui.
        viewModel.apply {

        }

    }


    // the invoice products adapter communicator object to handle the invoice products actions
    private val productCommunicator = object : InvoiceProductsAdapter.Communicator {
        override fun addProductToInvoice(product: ProductModel) {
            TODO("Not yet implemented")
        }

        override fun removeProductFromInvoice(product: ProductModel) {
            TODO("Not yet implemented")
        }

        override fun updateProductInInvoice(product: ProductModel) {
            TODO("Not yet implemented")
        }

        override fun removeAllProductFromInvoice(product: ProductModel) {
            TODO("Not yet implemented")
        }

        override fun addDiscount(product: ProductModel) {
            TODO("Not yet implemented")
        }

    }

}