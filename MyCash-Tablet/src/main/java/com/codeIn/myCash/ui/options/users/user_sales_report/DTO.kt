package com.codeIn.myCash.ui.options.users.user_sales_report

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class SalesReport(
    @SerializedName("id"                 ) var id                 : Int?                = null,
    @SerializedName("title "             ) var title              : String?             = null,
    @SerializedName("value"              ) var value              : String?             = null,
) : Parcelable {
    companion object{
        val salesReports = arrayListOf(
            SalesReport(id = 1, title = "Title 1", value = "1000"),
            SalesReport(id = 2, title = "Title 2", value = "2000"),
            SalesReport(id = 3, title = "Title 3", value = "3000"),
            SalesReport(id = 4, title = "Title 4", value = "4000"),
            SalesReport(id = 5, title = "Title 5", value = "5000"),
        )
    }
}