package com.codeIn.myCash.ui.home.products.payment_process_confimation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentPaymentProcessConfirmationBinding


class PaymentProcessConfirmationFragment : Fragment()  {

    private var _binding: FragmentPaymentProcessConfirmationBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentProcessConfirmationBinding.inflate(inflater, container, false)
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
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
//            payInvoiceButton.setOnClickListener {
//                if (findNavController().currentDestination?.id == R.id.navigation_paymentProcessConfirmationFragment)
//                    findNavController().navigate(PaymentProcessConfirmationFragmentDirections.actionNavigationPaymentProcessConfirmationFragmentToNavigationPaymentProcessSummaryFragment())
//            }
        }
    }
}