package com.codeIn.myCash.ui.home.invoices.newPurchaseInvoicePayment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.codeIn.common.data.PaymentType
import com.codeIn.common.data.Style
import com.codeIn.common.util.gone
import com.codeIn.common.util.toTwoDigits
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentNewPurchaseInvoicePaymentBinding
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.products.first_step_create_invoice.ProductsSummary
import com.codeIn.myCash.ui.home.products.second_step_create_invoice.SecondStepCreationInvoiceViewModel
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPurchaseInvoicePaymentFragment : Fragment()  {
    private val viewModel: SecondStepCreationInvoiceViewModel by viewModels()
    private val datePicker = DatePicker()

    private var _binding: FragmentNewPurchaseInvoicePaymentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPurchaseInvoicePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
            }
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            cashTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.CASH)
            }
            creditCardTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.CREDIT_CARD)
            }
            postpaidTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.POST_PAID)
            }
            cashAndCreditCardTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.CASH_AND_CREDIT_CARD)
            }
            postpaidAndCreditCardTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.POST_PAID_AND_CREDIT_CARD)
            }
            installmentPaymentTextView.setOnClickListener {
                viewModel.updatePaymentType(PaymentType.INSTALLMENT)
            }
            invoicePaymentDateEditText.setOnClickListener {
                datePicker.showDatePicker(
                    parentFragmentManager,
                    invoicePaymentDateEditText
                )
            }
        }



        viewModel.apply {
            paymentType.observe(viewLifecycleOwner) { type ->
                this@NewPurchaseInvoicePaymentFragment.updatePaymentType(type = type)
            }
            selectedProductsInCart.observe(viewLifecycleOwner){
                showPrices(it)
            }
        }

    }

    private fun updatePaymentType(type: PaymentType) {
        val context = requireContext()
        val binding = binding

        val viewsToStyle = listOf(
            binding.cashTextView,
            binding.postpaidTextView,
            binding.cashAndCreditCardTextView,
            binding.postpaidAndCreditCardTextView,
            binding.installmentPaymentTextView
        )

        binding.apply {
            cashPaidAmountEditText.gone()
            creditCardPaidAmountEditText.gone()
            remainingLayout.gone()
            invoicePaymentDateEditText.gone()
        }
        val selectedView = when (type) {
            PaymentType.CASH -> {
                binding.cashTextView
            }
            PaymentType.CREDIT_CARD -> {
                binding.creditCardTextView
            }
            PaymentType.POST_PAID -> {
                binding.apply {
                    cashPaidAmountEditText.visible()
                    remainingLayout.visible()
                    invoicePaymentDateEditText.visible()
                }
                binding.postpaidTextView
            }
            PaymentType.CASH_AND_CREDIT_CARD -> {
                binding.apply {
                    cashPaidAmountEditText.visible()
                    creditCardPaidAmountEditText.visible()
                }
                binding.cashAndCreditCardTextView
            }
            PaymentType.POST_PAID_AND_CREDIT_CARD -> {
                binding.postpaidAndCreditCardTextView
            }
            PaymentType.INSTALLMENT -> {
                binding.installmentPaymentTextView
            }
        }

        hideKeyboard(requireContext(),binding.root)

        val (secondaryBg, primaryBg) = R.drawable.bg_white_r12_s5_stroke20_ripple to R.drawable.bg_white_r12_s2_secondary_ripple
        // Define the secondary style to be applied to the views that are not selected
        val secondaryStyle = Style(
            color = ContextCompat.getColor(context, R.color.mainBlack),
            backgroundResource = secondaryBg,
            typeface = ResourcesCompat.getFont(context, R.font.regular)
        )

        // Define the primary style to be applied to the selected view
        val primaryStyle = Style(
            color = ContextCompat.getColor(context, R.color.secondaryColor),
            backgroundResource = primaryBg,
            typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
        )

        // initially apply the secondary style to all the views
        viewsToStyle.forEach { view ->
            view.changeDrawableAndTextColors(
                color = secondaryStyle.color,
                backgroundResource = secondaryStyle.backgroundResource,
                typeface = secondaryStyle.typeface
            )
        }
        binding.creditCardTextView.changeDrawableAndTextColors(
            color = secondaryStyle.color,
            backgroundResource = secondaryStyle.backgroundResource,
            typeface = secondaryStyle.typeface,
            skipDrawable = true
        )

        if (type == PaymentType.CREDIT_CARD) {
            binding.creditCardTextView.changeDrawableAndTextColors(
                color = primaryStyle.color,
                backgroundResource = primaryStyle.backgroundResource,
                typeface = primaryStyle.typeface,
                skipDrawable = true
            )
        } else
        // apply the primary style to the selected view
            selectedView.changeDrawableAndTextColors(
                color = primaryStyle.color,
                backgroundResource = primaryStyle.backgroundResource,
                typeface = primaryStyle.typeface
            )

    }


    private fun showPrices(productsInCart: ProductsInCart?) {
        binding.apply {
            initialTotalTextView.text = productsInCart?.initialTotal ?: "0.0"
            vat15TextView.text = productsInCart?.tax ?: "0.0"
            discountTextView.text = productsInCart?.discount ?: "0.0"
            totalPriceTextView.text = productsInCart?.finalTotal ?: "0.0"
            totalAfterDiscountTextView.text = productsInCart?.totalAfterDiscount ?: "0.0"
            vatLabel.text = "${getString(R.string.tax)} (${viewModel.tax}%)"
        }
    }
}
