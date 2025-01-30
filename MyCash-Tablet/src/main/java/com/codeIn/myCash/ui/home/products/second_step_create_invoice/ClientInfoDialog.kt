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
import android.widget.Toast
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogAddClientToInvoiceBinding
import com.codeIn.common.util.toEditable
import com.codeIn.myCash.ui.authentication.showError
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.sales.domain.clients.model.ClientRequest
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateClientValidationUseCase
import com.codeIn.myCash.features.sales.domain.clients.usecases.CreateVendorValidationUseCase
import com.codeIn.myCash.utilities.CustomToaster
import javax.inject.Inject

class ClientInfoDialog (
    context: Context,
    private val communicator: Communicator,
    private val clientRequest : ClientRequest?,
    private val createClientValidationUseCase: CreateClientValidationUseCase,
    private val vendorValidationUseCase: CreateVendorValidationUseCase,
    private val invoiceType : InvoiceType,
    private val mainTypeInvoice: MainTypeInvoice,
    private val arrayAdapter : SearchClientsAutoCompleteTextViewAdapter
) : Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogAddClientToInvoiceBinding
    private var clientId :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddClientToInvoiceBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (mainTypeInvoice == MainTypeInvoice.PURCHASE)
        {
            binding.header.text = context.getString(R.string.add_vendor)
            binding.subHeader.text = context.getString(R.string.vendor_info)
            binding.clientNameEditText.hint = context.getString(R.string.vendor_name)
        }
        if (clientRequest != null) {
            binding.apply {
                clientNameEditText.text = clientRequest.name?.toEditable()
                phoneNumberEditText.text = clientRequest.phone?.toEditable()
                taxNumberEditText.text = clientRequest.taxNumber?.toEditable()
                commercialRegisterEditText.text = clientRequest.commercialNumber?.toEditable()
                addressEditText.text = clientRequest.address?.toEditable()
                emailEditText.text = clientRequest.email?.toEditable()
            }
        }

        binding.addButton.setOnClickListener {
            binding.apply {
                clientNameEditText.isHovered = false
                phoneNumberEditText.isHovered = false
                taxNumberEditText.isHovered = false
                commercialRegisterEditText.isHovered = false
                addressEditText.isHovered = false

                if (mainTypeInvoice == MainTypeInvoice.PURCHASE)
                    vendorValidationUseCase.invoke(
                     name = clientNameEditText.text.toString() ,
                        phone = phoneNumberEditText.text.toString() ,
                        taxNo = taxNumberEditText.text.toString() ,
                        commercialRecordNo = commercialRegisterEditText.text.toString() ,
                        email = emailEditText.text.toString()
                    ).let {
                        handleResult(it)
                    }
                else
                    createClientValidationUseCase.invoke(name = clientNameEditText.text.toString() ,
                        phone = phoneNumberEditText.text.toString() ,
                        taxNo = taxNumberEditText.text.toString() ,
                        commercialRecordNo = commercialRegisterEditText.text.toString() ,
                        email = emailEditText.text.toString()).let {
                            handleResult(it)
                    }
            }
        }

        binding.phoneNumberEditText.setAdapter(arrayAdapter)
        binding.phoneNumberEditText.setDropDownBackgroundDrawable(binding.root.context.resources.getDrawable(R.drawable.autocomplete_dropdown))

        binding.phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()){
                    clientId = null
                    binding.clientNameEditText.setText("")
                    binding.emailEditText.setText("")
                    binding.taxNumberEditText.setText("")
                    binding.commercialRegisterEditText.setText("")
                    binding.addressEditText.setText("")
                }

                communicator.searchClient(s.toString())
                clientId = null
            }
            override fun afterTextChanged(s: Editable) {
                clientId = null
                binding.clientNameEditText.setText("")
                binding.emailEditText.setText("")
                binding.taxNumberEditText.setText("")
                binding.commercialRegisterEditText.setText("")
                binding.addressEditText.setText("")
            }
        })


    }


    private fun handleResult(invalidInput: InvalidInput){
        when(invalidInput){
            InvalidInput.EMPTY ->{
                CustomToaster.show(context , context.getString(R.string.please_fill_all_the_fields) ,
                    false )
            }
            InvalidInput.PHONE_SAUDI ->{
                binding.phoneNumberEditText.showError()
                CustomToaster.show(context ,context.getString(R.string.please_enter_a_valid_saudi_phone_number) , false )
                binding.phoneNumberEditText.error = context.getString(R.string.please_enter_a_valid_saudi_phone_number)
            }
            InvalidInput.TAX_RECORD ->{
                binding.taxNumberEditText.showError()
                CustomToaster.show(context ,context.getString(R.string.please_enter_valid_tax_record), false )
                binding.taxNumberEditText.error = context.getString(R.string.please_enter_valid_tax_record)
            }
            InvalidInput.COMMERCIAL_RECORD ->{
                binding.commercialRegisterEditText.showError()
                CustomToaster.show(context , context.getString(R.string.please_enter_valid_commercial_record) , false )
                binding.commercialRegisterEditText.error = context.getString(R.string.please_enter_valid_commercial_record)
            }
            InvalidInput.NONE ->{
                communicator.addClient(ClientRequest(
                    name = binding.clientNameEditText.text.toString(),
                    phone = binding.phoneNumberEditText.text.toString() ,
                    commercialNumber = binding.commercialRegisterEditText.text.toString() ,
                    taxNumber = binding.taxNumberEditText.text.toString() ,
                    address = binding.addressEditText.text.toString() ,
                    email = binding.emailEditText.text.toString() ,
                    id = clientId
                ))
                dismiss()
            }
            else -> {

            }
        }
    }

    fun setAutoCompleteData(data : List<ClientModel>?){

        arrayAdapter.addAll(data!!)

        binding.phoneNumberEditText.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
            val autoCompleteText = adapterView.getItemAtPosition(i) as ClientModel
            binding.phoneNumberEditText.setText(autoCompleteText.phone)
            binding.clientNameEditText.setText(autoCompleteText.name)
            binding.emailEditText.setText(autoCompleteText.email)
            binding.taxNumberEditText.setText(autoCompleteText.taxRecord)
            binding.commercialRegisterEditText.setText(autoCompleteText.commercialRecord)
            binding.addressEditText.setText(autoCompleteText.address)

            clientId = autoCompleteText.id.toString()

            binding.phoneNumberEditText.error = null
            binding.phoneNumberEditText.clearFocus()

            binding.taxNumberEditText.error = null
            binding.taxNumberEditText.clearFocus()

            binding.commercialRegisterEditText.error = null
            binding.commercialRegisterEditText.clearFocus()

        }

    }
    interface Communicator {
        fun addClient(clientRequest: ClientRequest? )
        fun searchClient(search : String?)
    }
}