package com.codeIn.myCash.utilities.pdf

import com.bumptech.glide.Glide
import com.codeIn.common.data.InvoiceType
import com.codeIn.myCash.R



    fun InvoicePdfManager.bindHeader(){
        binding.apply {
            Glide.with(binding.root)
                .load(invoice?.cashierModel?.accountInfo?.logo)
                .error(R.drawable.icon_app)
                .into(logoPDF)

            nameStore.text = invoice?.cashierModel?.accountInfo?.commercialRecordName?:context.getString(R.string.app_name)

            brancheName.text = invoice?.shift?.branch?.name?:"----"
            brancheAddress.text = invoice?.shift?.branch?.address?:"----"

            when(invoice?.invoiceType){
                InvoiceType.SIMPLE.value.toString() -> invoiceType.text = context.getString(R.string.simple_invoice)
                InvoiceType.TAX.value.toString() -> invoiceType.text = context.getString(R.string.tax_invoice)
            }

            commercialRecord.text = invoice.cashierModel?.accountInfo?.commercialRecord?:"----"
            taxRecord.text = invoice.cashierModel?.accountInfo?.taxRecord?:"----"
            taxRecord.text = invoice.cashierModel?.accountInfo?.taxRecord?:"----"

        }


    }


    fun InvoicePdfManager.bindFooter(){

    }

    fun InvoicePdfManager.bindMainInvoiceData(){

    }

    fun InvoicePdfManager.bindProducts(){

    }
