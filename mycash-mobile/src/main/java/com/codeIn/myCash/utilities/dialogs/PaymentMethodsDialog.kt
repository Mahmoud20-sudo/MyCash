package com.codeIn.myCash.utilities.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.PaymentMethods
import com.codeIn.common.data.PaymentType
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogPaymentMethodBinding
import com.codeIn.myCash.utilities.dialogs.PaymentMethodsDialog.Communicator
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel

/**
 * Simple dialog used to show options for payment methods selection.
 * @param context [Context] the context of the activity
 * @param amount [String] the amount to pay
 * @param communicator [Communicator] the interface used to communicate with the fragment
 */
class PaymentMethodsDialog(
    context: Context,
    private var amount: String = "0",
    private val invoiceModel : InvoiceModel? ,
    private val currency : String? ,
    private val communicator: Communicator ,
) :
    Dialog(context, R.style.PauseDialog) {

    init {
        amount = NumberHelper.roundOffDecimal(amount.toDouble())
    }

    private lateinit var binding: DialogPaymentMethodBinding
    var returnedCash = 0.0
    var returnedVisa = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPaymentMethodBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.apply {
            setValues()
            cancelButton.setOnClickListener {
                dismiss()
            }
            payButton.setOnClickListener {
                val selectedPaymentMethod = if (cashRadioButton.isChecked && !creditCardRadioButton.isChecked)
                    PaymentType.CASH
                else if (creditCardRadioButton.isChecked && !cashRadioButton.isChecked)
                    PaymentType.CREDIT_CARD
                else if(creditCardRadioButton.isChecked && !cashRadioButton.isChecked)
                    PaymentType.CASH_AND_CREDIT_CARD
                else
                    PaymentType.CASH
                communicator.onPaymentMethodSelected(selectedPaymentMethod , returnedCash.toString() , returnedVisa.toString())
                dismiss()
            }
        }

    }

    /**
     * Interface used to communicate with the fragment
     */
    interface Communicator {
        fun onPaymentMethodSelected(paymentType: PaymentType , returnedCash : String? , returnedVisa : String?)
    }

    private fun DialogPaymentMethodBinding.setValues(){
        if (invoiceModel?.paymentType == PaymentType.CASH.value.toString() || invoiceModel?.paymentType == PaymentType.POST_PAID.value.toString()){
            cashRadioButton.isChecked = true
            creditCardRadioButton.isChecked = false
            cashRadioButton.text = "${context.getString(R.string.cash)} $amount $currency"
            returnedCash =  amount.toDouble()
        }
        else if (invoiceModel?.paymentType == PaymentType.CREDIT_CARD.value.toString()){
            cashRadioButton.isChecked = false
            creditCardRadioButton.isChecked = true
            creditCardRadioButton.text = "${context.getString(R.string.credit_card)} $amount $currency"
            returnedVisa = amount.toDouble()
        }
        else if (invoiceModel?.paymentType == PaymentType.POST_PAID_AND_CREDIT_CARD.value.toString() ||
            invoiceModel?.paymentType == PaymentType.CASH_AND_CREDIT_CARD.value.toString()){

            val cashValue = NumberHelper.persianToEnglishText(invoiceModel?.cashPrice?:"0.0").toDouble()
            val visaValue = NumberHelper.persianToEnglishText(invoiceModel?.visaPrice?:"0.0").toDouble()

            if (cashValue >= amount.toDouble()){
                cashRadioButton.text = "${context.getString(R.string.cash)} $amount $currency"
                creditCardRadioButton.isChecked = false
                cashRadioButton.isChecked = true
                returnedCash = amount.toDouble()
                return
            }
            if (visaValue >= amount.toDouble()){
                creditCardRadioButton.text = "${context.getString(R.string.credit_card)} $amount $currency"
                creditCardRadioButton.isChecked = true
                cashRadioButton.isChecked = false
                returnedVisa = amount.toDouble()
                return
            }
            if (cashValue < amount.toDouble() || visaValue < amount.toDouble()){
                if (visaValue != 0.0)
                {
                    creditCardRadioButton.text = "${context.getString(R.string.credit_card)} $visaValue $currency"
                    val result = amount.toDouble() - visaValue
                    cashRadioButton.text = "${context.getString(R.string.cash)} $result $currency"

                    returnedCash= result
                    returnedVisa = visaValue
                    creditCardRadioButton.isChecked = true
                    cashRadioButton.isChecked = true
                    return
                }
                if (cashValue != 0.0)
                {
                    cashRadioButton.text = "${context.getString(R.string.cash)} $cashValue $currency"
                    val result = amount.toDouble() - cashValue
                    creditCardRadioButton.text = "${context.getString(R.string.credit_card)} $result $currency"
                    creditCardRadioButton.isChecked = true
                    cashRadioButton.isChecked = true
                    returnedCash= cashValue
                    returnedVisa = result
                    return
                }

            }
        }

    }
}