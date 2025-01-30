package com.codeIn.common.printer.pdf_from_view_manager

import android.view.View

interface PdfFromViewHandler {

    fun setInvoiceActions(action: String, applicationId : String, view: View)
}