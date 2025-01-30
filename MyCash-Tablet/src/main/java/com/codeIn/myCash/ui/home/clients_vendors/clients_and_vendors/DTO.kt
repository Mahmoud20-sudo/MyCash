package com.codeIn.myCash.ui.home.clients_vendors.clients_and_vendors
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client (

    @SerializedName("id"            ) var id            : Int?              = null,
    @SerializedName("name"          ) var name          : String?           = null,
    @SerializedName("email"         ) var email         : String?           = null,
    @SerializedName("phone"         ) var phone         : String?           = null,
    @SerializedName("receipt"       ) var receipt       : String?           = null,
    @SerializedName("extraInfo"     ) var extraInfo       : String?           = null,
    @SerializedName("payment"       ) var paymentType   : Int               = 0

) : Parcelable


@Parcelize
data class Payment (

    @SerializedName("id"                 ) var id                 : Int?              = null,
    @SerializedName("count"              ) var count              : String?           = null,
    @SerializedName("date"               ) var date               : String?           = null,
    @SerializedName("number"             ) var number             : String?           = null,
    @SerializedName("price"              ) var price              : String?           = null,
    @SerializedName("payment_completed"  ) var paymentCompleted   : Int               = 0

) : Parcelable


@Parcelize
data class Receipt (

    @SerializedName("id"                 ) var id                 : Int?              = null,
    @SerializedName("date"               ) var date               : String?           = null,
    @SerializedName("number"             ) var number             : String?           = null,
    @SerializedName("invoice_number"     ) var invoiceNumber      : String?           = null,
    @SerializedName("price"              ) var price              : String?           = null,
    @SerializedName("isPaid"             ) var isPaid              : Int               = 0

) : Parcelable