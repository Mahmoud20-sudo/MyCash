package com.codeIn.myCash.ui.options.settings.invoice_settings.dialog

interface OrderNoListener {
    fun startSaleInvoiceOrder(orderNo : String?)
    fun startPurchaseInvoiceOrder(orderNo : String?)
}