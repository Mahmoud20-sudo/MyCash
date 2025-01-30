package com.codeIn.myCash.ui.home.invoices.addProducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentAddProductsBinding
import com.codeIn.myCash.ui.home.products.products.Category
import com.codeIn.myCash.ui.home.products.products.Product
import com.codeIn.myCash.ui.home.products.products.ProductsViewModel
import com.codeIn.myCash.ui.home.products.products.adapters.CategoriesAdapter
import com.codeIn.myCash.ui.home.products.products.adapters.ProductsAdapter
import com.codeIn.myCash.utilities.MyAnimator
import com.codeIn.common.util.gone
import com.codeIn.common.util.toTwoDigits
import com.codeIn.myCash.utilities.CustomToaster
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass. This fragment is used to add products to an invoice.
 *
 */

@AndroidEntryPoint
class AddProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModels()

    // animator to animate the invoice card view it handles the animation of the card view when the user adds or removes products from the invoice
    private val myAnimator = MyAnimator()

    private var _binding: FragmentAddProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductsBinding.inflate(inflater, container, false)
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


        binding.productsFragment.topSheet.root.gone()


//        val categoriesAdapter =
//            CategoriesAdapter(context = requireContext(), communicator = categoryCommunicator)

//        val productsAdapter = ProductsAdapter(requireContext(), productCommunicator)
        binding.productsFragment.apply {
//            categoryRecycleView.adapter = categoriesAdapter
//            productRecycleView.adapter = productsAdapter
        }


        // set the click listener to the view invoice card view to navigate to the invoice details fragment
        binding.productsFragment.viewInvoiceCardView.setOnClickListener {
            binding.proceedButton.callOnClick()
        }

        binding.proceedButton.setOnClickListener {
            if (viewModel.selectedProductsList.isEmpty()) {
                CustomToaster.show(
                    requireContext(),
                    "Please select at least one product",
                    isSuccess = false
                )
                return@setOnClickListener
            }

            if (findNavController().currentDestination?.id == R.id.navigation_addProductsFragment) {
                val action =
                    com.codeIn.myCash.ui.home.invoices.addProducts.AddProductsFragmentDirections.actionNavigationAddProductsFragmentToNavigationNewPurchaseInvoiceFragment(
                        viewModel.selectedProductsList
                    )
                findNavController().navigate(action)
            }
        }

        // add the observers to the view model live vendorsData
        viewModel.apply {
            categories.observe(viewLifecycleOwner) {
                selectedProducts.observe(viewLifecycleOwner) {
                    binding.productsFragment.apply {
                        countTextView.text = it.count.toString()
                        val totalPrice = "${it.totalPrice.toTwoDigits()} ${getString(R.string.sar)}"
                        totalPriceSummaryTv?.text = totalPrice
                    }

                    if (it.count > 0)
                        myAnimator.animateTranslationYShow(binding.productsFragment.viewInvoiceCardView)
                    else
                        myAnimator.animateTranslationYHide(binding.productsFragment.viewInvoiceCardView)

                }
            }


        }


        /**
         * Communicators interfaces for the products adapter
         */

//    private val productCommunicator = object : ProductsAdapter.Communicator {
//        override fun handleProductInCart(product: Product) =
//            viewModel.handleProductInCart(product)
//
//        override fun removeProductFromInvoice(product: Product) =
//            viewModel.removeProductFromInvoice(product)
//
//        override fun removeAllProductFromInvoice(product: Product) =
//            viewModel.removeAllProductFromInvoice(product)
//
//        override fun showProductOptions(product: Product) {
//
//        }
//
//    }


        /**
         * Communicators interfaces for the categories adapter
         */
//    private val categoryCommunicator = object : CategoriesAdapter.Communicator {
//        override fun setCategory(category: Category) {
//
//        }
//    }


//    override fun onResume() {
//        super.onResume()
//        viewModel.updateProductsInvoice()
//    }


    }
}