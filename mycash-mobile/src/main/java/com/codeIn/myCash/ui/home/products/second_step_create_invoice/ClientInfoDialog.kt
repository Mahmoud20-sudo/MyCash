package com.codeIn.myCash.ui.home.products.second_step_create_invoice

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.AdapterView
import com.codeIn.common.data.ClientsSection
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogAddClientToInvoiceBinding
import com.codeIn.common.util.toEditable
import com.codeIn.myCash.features.sales.data.clients.remote.request.AddClientRequest
import com.codeIn.myCash.ui.showError
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.domain.clients.model.ClientRequest
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientValidationUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateVendorValidationUseCase
import com.codeIn.myCash.utilities.CustomToaster

class ClientInfoDialog(
    context: Context,
    private val communicator: Communicator,
    private val clientRequest: ClientRequest?,
    private val createClientValidationUseCase: CreateClientValidationUseCase,
    private val vendorValidationUseCase: CreateVendorValidationUseCase,
    private val invoiceType: MainTypeInvoice,
    private val arrayAdapter: SearchClientsAutoCompleteTextViewAdapter
) : Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogAddClientToInvoiceBinding
    private var clientId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddClientToInvoiceBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (invoiceType == MainTypeInvoice.PURCHASE) {
            binding.header.text = context.getString(R.string.add_client)
            binding.subHeader.text = context.getString(R.string.client_info)
        }

        if (clientRequest != null) {
            binding.apply {
                clientNameEditText.binding.editText.text = clientRequest.name?.toEditable()
                phoneNumberEditText.text  = clientRequest.phone?.toEditable()
                taxNumberEditText.binding.editText.text  = clientRequest.taxNumber?.toEditable()
                commercialRegisterEditText.binding.editText.text  = clientRequest.commercialNumber?.toEditable()
                addressEditText.binding.editText.text  = clientRequest.address?.toEditable()
                emailEditText.binding.editText.text  = clientRequest.email?.toEditable()
            }
        }

        binding.addButton.setOnClickListener {
            binding.apply {
                clientNameEditText.isHovered = false
                phoneNumberEditText.isHovered = false
                taxNumberEditText.isHovered = false
                commercialRegisterEditText.isHovered = false
                addressEditText.isHovered = false

                if (invoiceType == MainTypeInvoice.PURCHASE)
                    vendorValidationUseCase.invoke(
                        name = clientNameEditText.text.toString(),
                        phone = phoneNumberEditText.text.toString(),
                        taxNo = taxNumberEditText.text.toString(),
                        commercialRecordNo = commercialRegisterEditText.text.toString(),
                        email = emailEditText.text.toString()
                    ).let {
                        handleResult(it)
                    }
                else
                    createClientValidationUseCase.invoke(
                        AddClientRequest(
                            name = clientNameEditText.text.toString(),
                            phone = phoneNumberEditText.text.toString(),
                            taxNo = taxNumberEditText.text.toString(),
                            commercialRecordNo = commercialRegisterEditText.text.toString(),
                            email = emailEditText.text.toString(),
                            type = ClientsSection.CLIENTS.value
                        )
                    ).let {
                        handleResult(it)
                    }
            }
        }

        binding.phoneNumberEditText.setAdapter(arrayAdapter)
        binding.phoneNumberEditText.setDropDownBackgroundDrawable(
            binding.root.context.resources.getDrawable(
                R.drawable.autocomplete_dropdown
            )
        )

        binding.phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                communicator.searchClient(s.toString())
                clientId = null
            }

            override fun afterTextChanged(s: Editable) {
                clientId = null
            }
        })


    }

    private fun handleResult(invalidInput: InvalidInput) {
        when (invalidInput) {
            InvalidInput.EMPTY -> CustomToaster.show(
                context,
                context.getString(R.string.please_fill_at_least_phone_and_name),
                false
            )

            InvalidInput.PHONE_SAUDI -> binding.phoneNumberEditText.showError(context.getString(R.string.please_enter_a_valid_saudi_phone_number))
            InvalidInput.EMAIL -> binding.emailEditText.binding.editText.showError(context.getString(R.string.email_error_message))
            InvalidInput.TAX_RECORD -> binding.taxNumberEditText.binding.editText.showError(context.getString(R.string.please_enter_valid_tax_record))
            InvalidInput.COMMERCIAL_RECORD -> binding.commercialRegisterEditText.binding.editText.showError(
                context.getString(
                    R.string.please_enter_valid_commercial_record
                )
            )

            InvalidInput.NONE -> {
                communicator.addClient(
                    ClientRequest(
                        name = binding.clientNameEditText.text.toString(),
                        phone = binding.phoneNumberEditText.text.toString(),
                        commercialNumber = binding.commercialRegisterEditText.text.toString(),
                        taxNumber = binding.taxNumberEditText.text.toString(),
                        address = binding.addressEditText.text.toString(),
                        email = binding.emailEditText.text.toString(),
                        id = clientId
                    )
                )
                dismiss()
            }

            else -> CustomToaster.show(context, context.getString(R.string.general_error), false)
        }
    }

    fun setAutoCompleteData(data: List<ClientModel>?) {

//        arrayAdapter.addAll(data!!)

        binding.phoneNumberEditText.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                var autoCompleteText = adapterView.getItemAtPosition(i) as ClientModel
                binding.phoneNumberEditText.setText(autoCompleteText.phone)
                binding.clientNameEditText.binding.editText.setText(autoCompleteText.name)
                binding.emailEditText.binding.editText.setText(autoCompleteText.email)
                binding.taxNumberEditText.binding.editText.setText(autoCompleteText.taxRecord)
                binding.commercialRegisterEditText.binding.editText.setText(autoCompleteText.commercialRecord)
                binding.addressEditText.binding.editText.setText(autoCompleteText.address)

                clientId = autoCompleteText.id.toString()

                binding.phoneNumberEditText.error = null
                binding.phoneNumberEditText.clearFocus()

                binding.taxNumberEditText.binding.editText.error = null
                binding.taxNumberEditText.clearFocus()

                binding.commercialRegisterEditText.binding.editText.error = null
                binding.commercialRegisterEditText.clearFocus()

            }

    }

    interface Communicator {
        fun addClient(clientRequest: ClientRequest?)
        fun searchClient(search: String?)
    }
}