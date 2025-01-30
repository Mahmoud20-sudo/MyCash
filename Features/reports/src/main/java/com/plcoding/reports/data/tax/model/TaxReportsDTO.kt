package com.plcoding.reports.data.tax.model

import com.google.gson.annotations.SerializedName


data class TaxReportsDTO (

  @SerializedName("status"  ) var status  : Int?    = null,
  @SerializedName("message" ) var message : String? = null,
  @SerializedName("data"    ) var taxReportData    : TaxReportData?   = TaxReportData()

)