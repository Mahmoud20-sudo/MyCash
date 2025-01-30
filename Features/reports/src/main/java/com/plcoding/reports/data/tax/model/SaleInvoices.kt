package com.plcoding.reports.data.tax.model

import com.google.gson.annotations.SerializedName


data class SaleInvoices (

  @SerializedName("total_price"       ) var totalPrice      : Double? = null,
  @SerializedName("total_tax"         ) var totalTax        : Double? = null,
  @SerializedName("total_without_tax" ) var totalWithoutTax : Double? = null

)