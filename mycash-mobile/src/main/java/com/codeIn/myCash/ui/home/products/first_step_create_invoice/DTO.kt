package com.codeIn.myCash.ui.home.products.first_step_create_invoice

import com.google.gson.annotations.SerializedName


data class ProductsSummary (
    @SerializedName("initialPrice" ) var initialPrice   : Double         = 0.0,
    @SerializedName("vat15"        ) var vat15          : Double         = 0.0,
    @SerializedName("dicount"      ) var dicount        : Double         = 0.0,
    @SerializedName("totalPrice"   ) var totalPrice     : Double         = 0.0
)
