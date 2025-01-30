package com.codeIn.myCash.ui.options.users.activities_logs

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ActivityLog(
    @SerializedName("id"                 ) var id                 : Int?                = null,
    @SerializedName("name"               ) var name               : String?             = null,
    @SerializedName("date"               ) var date               : String?             = null,
    @SerializedName("total_sales"        ) var totalSales         : String?             = null,
    @SerializedName("begin_day"          ) var beginDay           : DailyActivityLog?   = null,
    @SerializedName("end_day"            ) var endDay             : DailyActivityLog?   = null,
    @SerializedName("branch"             ) var branch             : String?             = null,

    ) : Parcelable {
    companion object {
        val activityLogs = arrayListOf(
            ActivityLog(id = 1, name = "John Doe",date = "20/08/2022", totalSales = "1000 SAR", beginDay = DailyActivityLog(id = 1, time = "08:00 AM", cash = "500 SAR", credit = "500 SAR" ), endDay = DailyActivityLog( id = 2, time = "08:00 PM", cash = "500 SAR", credit = "500 SAR"), branch = "Riyadh"),
            ActivityLog(id = 2, name = "Jane Doe",date = "21/08/2022", totalSales = "1000 SAR", beginDay = DailyActivityLog(id = 3, time = "08:00 AM", cash = "500 SAR", credit = "500 SAR" ), endDay = DailyActivityLog( id = 4, time = "08:00 PM", cash = "500 SAR", credit = "500 SAR"), branch = "Jeddah"),
            ActivityLog(id = 3, name = "John Doe",date = "22/08/2022", totalSales = "1000 SAR", beginDay = DailyActivityLog(id = 5, time = "08:00 AM", cash = "500 SAR", credit = "500 SAR" ), endDay = DailyActivityLog( id = 6, time = "08:00 PM", cash = "500 SAR", credit = "500 SAR"), branch = "Riyadh"),
            ActivityLog(id = 4, name = "Jane Doe",date = "23/08/2022", totalSales = "1000 SAR", beginDay = DailyActivityLog(id = 7, time = "08:00 AM", cash = "500 SAR", credit = "500 SAR" ), endDay = DailyActivityLog( id = 8, time = "08:00 PM", cash = "500 SAR", credit = "500 SAR"), branch = "Jeddah"),
            ActivityLog(id = 5, name = "John Doe",date = "24/08/2022", totalSales = "1000 SAR", beginDay = DailyActivityLog(id = 9, time = "08:00 AM", cash = "500 SAR", credit = "500 SAR" ), endDay = DailyActivityLog( id = 10, time = "08:00 PM", cash = "500 SAR", credit = "500 SAR"), branch = "Makkah"),
            ActivityLog(id = 6, name = "Jane Doe",date = "25/08/2022", totalSales = "1000 SAR", beginDay = DailyActivityLog(id = 11, time = "08:00 AM", cash = "500 SAR", credit = "500 SAR" ), endDay = DailyActivityLog( id = 12, time = "08:00 PM", cash = "500 SAR", credit = "500 SAR"), branch = "Jeddah"),
        )
    }
}

@Parcelize
data class DailyActivityLog(
    @SerializedName("id"                 ) var id                 : Int?                = null,
    @SerializedName("time"               ) var time               : String?             = null,
    @SerializedName("cash"               ) var cash               : String?             = null,
    @SerializedName("credit"             ) var credit             : String?             = null,
) : Parcelable