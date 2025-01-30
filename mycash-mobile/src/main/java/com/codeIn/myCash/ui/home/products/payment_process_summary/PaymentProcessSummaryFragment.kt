package com.codeIn.myCash.ui.home.products.payment_process_summary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.codeIn.myCash.databinding.FragmentPaymentProcessSummaryBinding


class PaymentProcessSummaryFragment : Fragment() {

    private var _binding: FragmentPaymentProcessSummaryBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentProcessSummaryBinding.inflate(inflater, container, false)
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
            shareTextView.setOnClickListener { }
            pdfDownloadTextView.setOnClickListener { }
            qrCodeTextView.setOnClickListener { }

        }
    }
}