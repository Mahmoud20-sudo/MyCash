package com.plcoding.reports.data.tax.model

import com.google.gson.annotations.SerializedName


data class TaxReportData (

    @SerializedName("sale_invoices"    ) var saleInvoices   : SaleInvoices? = SaleInvoices(),
    @SerializedName("buy_invoices"     ) var buyInvoices    : BuyInvoices?  = BuyInvoices(),
    @SerializedName("expenses"         ) var expenses       : Expenses?     = Expenses(),
    @SerializedName("net_total_price"  ) var netTotalPrice  : Double?          = null,
    @SerializedName("tax_total_price" ) var taxTotalPrice : Double?          = null

)