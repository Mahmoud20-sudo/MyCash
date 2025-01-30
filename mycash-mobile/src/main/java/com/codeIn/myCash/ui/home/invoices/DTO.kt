package com.codeIn.myCash.ui.home.invoices

import android.os.Parcelable
import com.codeIn.common.data.InvoiceFilter
import com.codeIn.common.data.NoteType
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

@Parcelize
data class Invoice (

    @SerializedName("id"                 ) var id                 : Int?              = null,
    @SerializedName("count"              ) var count              : String?           = (1..100).random().toString(),
    @SerializedName("date"               ) var date               : String?           = generateRandomDate(),
    @SerializedName("number"             ) var number             : String?           = (1..500).random().toString(),
    @SerializedName("price"              ) var price              : String?           = (1..500).random().toString(),
    @SerializedName("type"               ) var type               : Int               = (1..6).random(),
    @SerializedName("payment_completed"  ) var paymentCompleted   : Int               = (0..1).random(),
    @SerializedName("type_name"          ) var typeName           : InvoiceFilter     = InvoiceFilter.from(type)

) : Parcelable


@Parcelize
data class CreditorDebtorNote (

    @SerializedName("id"                 ) var id                 : Int?              = null,
    @SerializedName("count"              ) var count              : String?           = (1..100).random().toString(),
    @SerializedName("date"               ) var date               : String?           = generateRandomDate(),
    @SerializedName("number"             ) var number             : String?           = (1..500).random().toString(),
    @SerializedName("price"              ) var price              : String?           = (1..500).random().toString(),
    @SerializedName("type"               ) var type               : Int               = (1..2).random(),
    @SerializedName("payment_completed"  ) var paymentCompleted   : Int               = (0..1).random(),
    @SerializedName("type_name"          ) var typeName           : NoteType          = NoteType.from(type)

) : Parcelable


fun generateRandomDate(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy, hh:mm a", Locale.getDefault())
    val start = Calendar.getInstance()
    start.set(2021, Calendar.JANUARY, 1, 0, 0)
    val end = Calendar.getInstance()
    end.set(2021, Calendar.DECEMBER, 31, 23, 59)
    val randomDate = Random.nextLong(start.timeInMillis, end.timeInMillis)
    val randomCalendar = Calendar.getInstance()
    randomCalendar.timeInMillis = randomDate
    return formatter.format(randomCalendar.time)
}
