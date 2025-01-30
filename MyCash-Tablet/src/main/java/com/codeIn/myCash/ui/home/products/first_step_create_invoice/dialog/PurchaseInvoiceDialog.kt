package com.codeIn.myCash.ui.home.products.first_step_create_invoice.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.codeIn.myCash.utilities.pickers.DatePicker
import androidx.fragment.app.FragmentManager
import com.codeIn.common.data.InvalidInput
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogPurchaseInvoiceBinding
import com.codeIn.myCash.ui.authentication.showError
import com.codeIn.myCash.features.stock.domain.invoice.model.PurchaseInvoiceModel
import com.codeIn.myCash.features.stock.domain.invoice.usecases.MakePurchaseInvoiceValidationUseCase
import javax.inject.Inject


class PurchaseInvoiceDialog @Inject constructor(
    context: Context,
    private val fragmentManager: FragmentManager,
    private val communicator: Communicator,
    private val validationUseCase: MakePurchaseInvoiceValidationUseCase,
    private val purchaseInvoiceModel: PurchaseInvoiceModel
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogPurchaseInvoiceBinding
    private val datePicker = DatePicker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPurchaseInvoiceBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {

            dateTextView.text = purchaseInvoiceModel.date
            notesEditText.setText(purchaseInvoiceModel.note)
            referenceInvoiceNumberEditText.setText(purchaseInvoiceModel.referenceNumber)

            dateTextView.setOnClickListener {
                datePicker.showDatePicker(
                    childFragmentManager = fragmentManager,
                    textView = dateTextView,
                    preventFutureDates = true
                )
            }

            addButton.setOnClickListener {
                validationUseCase.invoke(referenceNumber = referenceInvoiceNumberEditText.text.toString(),
                    referenceDate = dateTextView.text.toString()).let {
                        when(it){
                            InvalidInput.NONE ->{
                                referenceInvoiceNumberEditText.error = null
                                dateTextView.error = null
                                communicator.addInvoice(
                                    PurchaseInvoiceModel(
                                        referenceInvoiceNumberEditText.text.toString(),
                                        dateTextView.text.toString(),
                                        notesEditText.text.toString()
                                    )
                                )
                                dismiss()
                            }
                            InvalidInput.EMPTY ->{
                                if (referenceInvoiceNumberEditText.text.isNullOrEmpty()){
                                    referenceInvoiceNumberEditText.error = context.getString(R.string.please_fill_all_the_fields)
                                    referenceInvoiceNumberEditText.showError()
                                }
                                else if (dateTextView.text.isNullOrEmpty()){
                                    dateTextView.error = context.getString(R.string.please_fill_all_the_fields)
                                    dateTextView.showError()
                                }
                            }

                            else -> {

                            }
                        }
                }
            }
            cancelButton.setOnClickListener {
                communicator.onDismiss()
                dismiss()
            }
        }
    }

    interface Communicator {
        fun addInvoice(purchaseInvoiceModel: PurchaseInvoiceModel)
        fun onDismiss() {}
    }

}