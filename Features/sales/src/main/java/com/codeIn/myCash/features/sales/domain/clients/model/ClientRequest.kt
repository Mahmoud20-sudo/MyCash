package com.codeIn.myCash.features.sales.domain.clients.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientRequest(
    var name : String,
    var phone : String,
    var email : String? = "",
    var commercialNumber : String? = "",
    var taxNumber  : String? = "",
    var address : String? = "",
    var id : String? = null
) : Parcelable
