package com.codeIn.myCash.ui.home.invoices.newInvoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.ClientsSection
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentNewInvoiceBinding
import com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors.Client
import com.codeIn.common.util.toEditable


/**
 * A simple [Fragment] subclass.
 * This fragment is used to create a new invoice.
 *
 */
class NewInvoiceFragment : Fragment() {

    private var _binding: FragmentNewInvoiceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewInvoiceBinding.inflate(inflater, container, false)
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

        binding.vendorEditText.setOnClickListener {
            binding.addVendorImageView.callOnClick()

        }

        binding.addVendorImageView.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.navigation_newInvoiceFragment)
                findNavController().navigate(
                    com.codeIn.myCash.ui.home.invoices.newInvoice.NewInvoiceFragmentDirections.actionNavigationNewInvoiceFragmentToNavigationAddClientsFragmentForInvoice(
                        ClientsSection.VENDORS
                    )
                )
        }

        binding.proceedButton.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.navigation_newInvoiceFragment)
                findNavController().navigate(
                    com.codeIn.myCash.ui.home.invoices.newInvoice.NewInvoiceFragmentDirections.actionNavigationNewInvoiceFragmentToNavigationAddProductsFragment()
                )
        }

        //wait for the result from the add client fragment
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Client>("client")
            ?.observe(viewLifecycleOwner) { result ->
                binding.vendorEditText.text = result.name?.toEditable()
            }

    }


}
