package com.codeIn.myCash.ui.home.invoices.madaPayment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.Style
import com.codeIn.common.nearpay.NearPayPayment
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.payment.NFCChecker
import com.codeIn.common.util.collectOnLifecycle
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentMadaPaymentBinding
import com.codeIn.myCash.databinding.FragmentPayReceiptBinding
import com.codeIn.myCash.ui.PaymentType
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.intro.IntroActivity
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.dialogs.InfoDialog
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptState
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel
import dagger.hilt.android.AndroidEntryPoint
import io.nearpay.sdk.data.models.ReconciliationReceipt
import io.nearpay.sdk.utils.enums.PurchaseFailure
import io.nearpay.sdk.utils.enums.ReconcileFailure
import io.nearpay.sdk.utils.enums.RefundFailure
import io.nearpay.sdk.utils.enums.TransactionData
import javax.inject.Inject

@AndroidEntryPoint
class MadaPaymentFragment : Fragment() {

    private val viewModel: MadaPaymentViewModel by viewModels()
    private var _binding: FragmentMadaPaymentBinding? = null
    private val binding get() = _binding!!
    private val infoDialog: InfoDialog = InfoDialog()

    @Inject
    lateinit var prefs : SharedPrefsModule
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMadaPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            refund.setOnClickListener {
                val amount = binding.amount.text.toString()
                val rrn = binding.rrn.text.toString()
                if (NFCChecker.checkNotPOSAndNFC(requireContext())) {
                    if (!amount.isNullOrEmpty() && amount != "0.0" && amount != "0." && amount != "." && amount.toDouble() > 0) {
                        if (!rrn.isNullOrEmpty() && rrn != " ")
                            NearPayPayment.refundNearPay(
                                view, "", amount ,
                                rrn,
                                "9ace70b7-977d-4094-b7f4-4ecb17de6753",
                                nearPayListener , null , null , 1)

                    }

                } else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())) {
                    CustomToaster.show(
                        requireContext(),
                        getString(R.string.this_device_is_not_support_nfc),
                        false
                    )
                }
            }

            pay.setOnClickListener {
                val amount = binding.amount.text.toString()
                if (NFCChecker.checkNotPOSAndNFC(requireContext())) {
                    if (!amount.isNullOrEmpty() && amount != "0.0" && amount != "0." && amount != "." && amount!!.toDouble() > 0) {
                        NearPayPayment.purchaseNearPay(
                            view, amount,
                            "9ace70b7-977d-4094-b7f4-4ecb17de6753",
                            "", nearPayListener
                        )

                    }

                } else if (NFCChecker.checkPhoneNotSupportNFC(requireContext())) {
                    CustomToaster.show(
                        requireContext(),
                        getString(R.string.this_device_is_not_support_nfc),
                        false
                    )
                }
            }

            cancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }



    private val nearPayListener = object : NearPayPayment.Companion.Listener {
        override fun onPurchaseProcess(
            code: Int,
            transactionData: TransactionData?,
            purchaseFailure: PurchaseFailure?,
            cashValue: String?,
            visaValue: String?
        ) {

            if (code == 0){
                binding.amount.setText("")
                binding.rrn.setText("")
                CustomToaster.show(
                    requireContext() ,
                    getString(R.string.success_message),
                    true
                )
            }
            else
            {
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
                            requireContext() ,
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }
                    3 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.authentication_failed),
                            false
                        )
                    }
                    4 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                    5 -> {
                        CustomToaster.show(
                            requireContext() ,
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
            if (code == 0){
                binding.amount.setText("")
                binding.rrn.setText("")
                CustomToaster.show(
                    requireContext() ,
                    getString(R.string.success_message),
                    true
                )
            }
            else
            {
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
                            requireContext() ,
                            getString(R.string.payment_is_declined),
                            false
                        )
                    }
                    3 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.authentication_failed),
                            false
                        )
                    }
                    4 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                    5 -> {
                        CustomToaster.show(
                            requireContext() ,
                            getString(R.string.general_error),
                            false
                        )
                    }
                }
            }
        }

        override fun onReconciliationProcess(
            code: Int,
            receipt: ReconciliationReceipt?,
            reconcileFailure: ReconcileFailure?
        ) {
            TODO("Not yet implemented")
        }

    }

}