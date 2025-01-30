package com.codeIn.myCash.ui.home.invoices.incompleteInvoice

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogPayReceiptBinding
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel
import com.codeIn.myCash.ui.setErrorMsg
import com.codeIn.myCash.utilities.pickers.DatePicker


class PayReceiptDialog(
    context: Context,
    private val receipt: ReceiptModel,
    private val currency: String?,
    private val childFragmentManager: FragmentManager,
    private val communicator: Communicator,
    private val currentReceiptModel: CurrentReceiptModel?
) : Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogPayReceiptBinding
    private val datePicker = DatePicker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPayReceiptBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initializeUI()
        setupListeners()
    }

    private fun initializeUI() {
        val invoiceRemaining =
            NumberHelper.persianToEnglishText(receipt.invoiceData?.rammingPrice ?: "0.0")
        val cashPaid = NumberHelper.persianToEnglishText(receipt.invoiceData?.cashPrice ?: "0.0")
        val visaPaid = NumberHelper.persianToEnglishText(receipt.invoiceData?.visaPrice ?: "0.0")
        val totalPaid = cashPaid.toDouble() + visaPaid.toDouble()

        binding.apply {
            invoiceNumberTextView.text = "#${receipt.invoiceData?.invoiceNumber}"
            totalAmountTextView.text = "${receipt.invoiceData?.totalPrice} $currency"
            beenPaidTextView.text = "$totalPaid $currency"
            paymentDateTextView.text = receipt.date

            deservedAmountTextView.text = "${receipt.invoiceData?.rammingPrice} $currency"
            currentReceiptModel?.let {
                remainingTextView.text = it.remaining ?: invoiceRemaining
            } ?: run {
                remainingTextView.text = invoiceRemaining
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            paidAmountEditText.binding.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // No-op
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val inputValue = s.toString().ifEmpty { "0.0" }
                    handlePaidAmountChange(inputValue)
                }

                override fun afterTextChanged(s: Editable) {
                    // No-op
                }
            })

            payReceiptButton.setOnClickListener {
                handlePayReceipt()
            }

            cancelButton.setOnClickListener {
                dismiss()
            }

            receiptPaymentDateTextView.setOnClickListener {
                datePicker.showDatePicker(
                    preventFutureDates = true,
                    childFragmentManager = childFragmentManager,
                    editText = receiptPaymentDateTextView
                )
            }
        }
    }

    private fun handlePaidAmountChange(inputValue: String) {
        val inputAmount = inputValue.toDoubleOrNull() ?: 0.0
        val invoiceRemaining =
            NumberHelper.persianToEnglishText(receipt.invoiceData?.rammingPrice ?: "0.0").toDouble()

        binding.apply {
            when {
                inputAmount == 0.0 -> {
                    paidAmountEditText.setErrorMsg(context.getString(R.string.please_enter_valid_money))
                    remainingTextView.text = invoiceRemaining.toString()
                }

                inputAmount > invoiceRemaining -> {
                    paidAmountEditText.setErrorMsg(context.getString(R.string.please_enter_valid_cash_money_smaller_equal_remianing_price))
                    remainingTextView.text = invoiceRemaining.toString()
                }

                else -> {
                    val remaining = (invoiceRemaining - inputAmount).toString()
                    remainingTextView.text = remaining
                }
            }
            payReceiptButton.isEnabled = inputAmount in 0.01..invoiceRemaining
        }
    }

    private fun handlePayReceipt() {
        binding.apply {
            val inputValue = NumberHelper.persianToEnglishText(paidAmountEditText.text)
            val remainingValue =
                NumberHelper.persianToEnglishText(remainingTextView.text.toString()).toDouble()

            if (inputValue.isEmpty()) {
                paidAmountEditText.setErrorMsg(context.getString(R.string.please_fill_all_the_fields))
                return
            }

            if (inputValue.toDouble() == 0.0) {
                paidAmountEditText.setErrorMsg(context.getString(R.string.please_enter_valid_money))
                return
            }

            if (remainingValue > 0.0 && receiptPaymentDateTextView.text.isNullOrEmpty()) {
                receiptPaymentDateTextView.error =
                    context.getString(R.string.please_enter_next_date_for_receipt)
                return
            }

            val receiptModel = CurrentReceiptModel(
                money = inputValue,
                nextDate = receiptPaymentDateTextView.text.toString(),
                remaining = remainingValue.toString()
            )

            communicator.onPay(receiptModel, receipt)
            dismiss()
        }
    }

    interface Communicator {
        fun onPay(currentReceiptModel: CurrentReceiptModel, receipt: ReceiptModel)
    }
}