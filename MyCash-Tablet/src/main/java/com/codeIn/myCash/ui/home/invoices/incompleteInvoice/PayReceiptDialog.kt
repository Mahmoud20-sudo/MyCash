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
import com.codeIn.common.data.Discount
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.NumberHelper
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogPayReceiptBinding
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.myCash.features.stock.data.receipt.remote.response.ReceiptModel
import com.codeIn.myCash.features.stock.domain.receipt.model.CurrentReceiptModel


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


        binding.apply {

            val cashPaid = NumberHelper.persianToEnglishText(receipt.invoiceData?.cashPrice?:"0.0")
            val visaPaid = NumberHelper.persianToEnglishText(receipt.invoiceData?.visaPrice?:"0.0")
            val paid = cashPaid.toDouble() + visaPaid.toDouble()

            invoiceNumberTextView.text = "#${receipt.invoiceData?.invoiceNumber}"
            totalAmountTextView.text = "${receipt.invoiceData?.totalPrice} $currency"
            beenPaidTextView.text = "${paid} $currency"
            paymentDateTextView.text = "${receipt.date}"
            deservedAmountTextView.text = "${receipt.invoiceData?.rammingPrice} $currency"


            payReceiptButton.setOnClickListener {
                if (paidAmountEditText.text.isNotEmpty() )
                {
                    val inputValue = NumberHelper.persianToEnglishText(paidAmountEditText.text.toString())
                    if (inputValue.toDouble() == 0.0){
                        paidAmountEditText.error = context.getString(R.string.please_enter_valid_money)
                    }
                    else{
                        val remainingValue = NumberHelper.persianToEnglishText(remainingTextView.text.toString()).toDouble()
                        if (remainingValue > 0.0 && receiptPaymentDateTextView.text.isNullOrEmpty()){
                            receiptPaymentDateTextView.error = context.getString(R.string.please_enter_next_date_for_receipt)
                            return@setOnClickListener
                        }

                        communicator.onPay(
                            CurrentReceiptModel(paidAmountEditText.text.toString() ,
                                receiptPaymentDateTextView.text.toString() ,
                                remainingValue.toString()) ,
                            receipt
                        )
                        dismiss()
                    }
                }
                else
                {
                    paidAmountEditText.error = context.getString(R.string.please_fill_all_the_fields)
                }
            }
            cancelButton.setOnClickListener {
                dismiss()
            }

            receiptPaymentDateTextView.setOnClickListener {
                receiptPaymentDateTextView.error = null
                datePicker.showDatePicker(
                    childFragmentManager = childFragmentManager,
                    editText = binding.receiptPaymentDateTextView
                )
            }

            var moneyNeed = "0.0"
            var currentMoneyPaid = ""
            val invoiceRemaining = NumberHelper.persianToEnglishText(receipt?.invoiceData?.rammingPrice?:"0.0")
            moneyNeed = invoiceRemaining
            if (currentReceiptModel != null )
            {
                moneyNeed = currentReceiptModel.remaining?:"0.0"
                currentMoneyPaid = currentReceiptModel?.money?:"0.0"
                receiptPaymentDateTextView.setText(currentReceiptModel?.nextDate?:"")
            }
            paidAmountEditText.setText(currentMoneyPaid)
            remainingTextView.text = moneyNeed
            paidAmountEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    remainingTextView.text = moneyNeed
                }
                override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                    paidAmountEditText.error = null
                    val inputValue = if (charSequence.isNotEmpty())
                        NumberHelper.persianToEnglishText(charSequence.toString())
                    else
                        "0.0"

                    if (charSequence.isNotEmpty()){
                        if (inputValue.toDouble() > NumberHelper.persianToEnglishText(receipt?.invoiceData?.rammingPrice?:"0.0").toDouble()){
                            paidAmountEditText.error = context.getString(R.string.please_enter_valid_cash_money_smaller_equal_remianing_price)
                        }
                        else if (inputValue.toDouble() == 0.0){
                            paidAmountEditText.error = context.getString(R.string.please_enter_valid_money)
                        }
                        else{
                            remainingTextView.text = NumberHelper.persianToEnglishText((moneyNeed.toDouble() - inputValue.toDouble()).toString())
                        }
                    }
                    else
                    {
                        remainingTextView.text = receipt?.invoiceData?.rammingPrice?:"0.0"
                    }

                    val total = NumberHelper.persianToEnglishText(inputValue).toDouble()+
                            NumberHelper.persianToEnglishText(remainingTextView.text.toString()).toDouble()
                    if (total !=  invoiceRemaining.toDouble()){
                        binding.remainingTextView.text = NumberHelper.persianToEnglishText((invoiceRemaining.toDouble() - inputValue.toDouble()).toString())

                    }
                }
                override fun afterTextChanged(editable: Editable) {}
            })

        }

    }

    interface Communicator {
        fun onPay(currentReceiptModel : CurrentReceiptModel , receipt: ReceiptModel)
    }
}