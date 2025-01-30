package com.codeIn.myCash.features.user.data.complete_data.model


import com.google.gson.annotations.SerializedName

data class MyInfoDTO(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: Any?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: Any?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("type")
        val type: Int?,
        @SerializedName("status")
        val status: Int?,
        @SerializedName("msgCode")
        val msgCode: Int?,
        @SerializedName("invoicesCount")
        val invoicesCount: Int?,
        @SerializedName("paymentStatus")
        val paymentStatus: Int?,
        @SerializedName("isCompleteAccountInfo")
        val isCompleteAccountInfo: Int?,
        @SerializedName("isCompleteShitInfo")
        val isCompleteShitInfo: Int?,
        @SerializedName("has_permission")
        val hasPermission: Int?,
        @SerializedName("lang")
        val lang: String?,
        @SerializedName("firebase")
        val firebase: Any?,
        @SerializedName("note")
        val note: Any?,
        @SerializedName("token")
        val token: Any?,
        @SerializedName("canReset")
        val canReset: Boolean?,
        @SerializedName("country")
        val country: Country?,
        @SerializedName("subscription")
        val subscription: Subscription?,
        @SerializedName("accountInfo")
        val accountInfo: AccountInfo?,
        @SerializedName("mainBranch")
        val mainBranch: MainBranch?,
        @SerializedName("lastShift")
        val lastShift: LastShift?
    ) {
        data class Country(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("currency")
            val currency: String?,
            @SerializedName("icon")
            val icon: String?,
            @SerializedName("status")
            val status: Int?,
            @SerializedName("countryCode")
            val countryCode: String?,
            @SerializedName("phoneNumbers")
            val phoneNumbers: Int?,
            @SerializedName("name_ar")
            val nameAr: String?,
            @SerializedName("name_en")
            val nameEn: String?,
            @SerializedName("currency_ar")
            val currencyAr: String?,
            @SerializedName("currency_en")
            val currencyEn: String?
        )

        data class Subscription(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("startDate")
            val startDate: String?,
            @SerializedName("endDate")
            val endDate: String?,
            @SerializedName("daysLeft")
            val daysLeft: Int?,
            @SerializedName("expire")
            val expire: Int?,
            @SerializedName("deviceToken")
            val deviceToken: Any?,
            @SerializedName("packagePrice")
            val packagePrice: String?,
            @SerializedName("devicePrice")
            val devicePrice: String?,
            @SerializedName("discountPrice")
            val discountPrice: String?,
            @SerializedName("taxPrice")
            val taxPrice: String?,
            @SerializedName("totalPrice")
            val totalPrice: String?,
            @SerializedName("device")
            val device: Any?,
            @SerializedName("package")
            val packageX: Package?,
            @SerializedName("influencer")
            val influencer: Any?
        ) {
            data class Package(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("image")
                val image: String?,
                @SerializedName("name_ar")
                val nameAr: String?,
                @SerializedName("name_en")
                val nameEn: String?,
                @SerializedName("duration_ar")
                val durationAr: String?,
                @SerializedName("duration_en")
                val durationEn: String?,
                @SerializedName("duration")
                val duration: String?,
                @SerializedName("desc_ar")
                val descAr: Any?,
                @SerializedName("desc_en")
                val descEn: Any?,
                @SerializedName("desc")
                val desc: Any?,
                @SerializedName("isDiscount")
                val isDiscount: Int?,
                @SerializedName("discount")
                val discount: Int?,
                @SerializedName("discountType")
                val discountType: Int?,
                @SerializedName("price")
                val price: String?,
                @SerializedName("finalPrice")
                val finalPrice: String?,
                @SerializedName("days")
                val days: Int?,
                @SerializedName("usersCount")
                val usersCount: Int?,
                @SerializedName("offlineOrOnline")
                val offlineOrOnline: Int?,
                @SerializedName("country_id")
                val countryId: Int?,
                @SerializedName("features")
                val features: List<Any?>?
            )
        }

        data class AccountInfo(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("taxRecord")
            val taxRecord: Any?,
            @SerializedName("commercialRecord")
            val commercialRecord: Any?,
            @SerializedName("commercialRecordName")
            val commercialRecordName: Any?,
            @SerializedName("tax")
            val tax: Any?,
            @SerializedName("notification")
            val notification: Int?,
            @SerializedName("drafts")
            val drafts: Int?,
            @SerializedName("quickInvoice")
            val quickInvoice: Int?,
            @SerializedName("logo")
            val logo: Any?
        )

        data class MainBranch(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("address")
            val address: String?,
            @SerializedName("phone")
            val phone: Any?,
            @SerializedName("country")
            val country: Any?,
            @SerializedName("city")
            val city: Any?,
            @SerializedName("additional_info")
            val additionalInfo: Any?,
            @SerializedName("isMain")
            val isMain: Int?,
            @SerializedName("status")
            val status: Int?,
            @SerializedName("employee")
            val employee: Employee?
        ) {
            data class Employee(
                @SerializedName("name")
                val name: Any?,
                @SerializedName("id")
                val id: Int?
            )
        }

        data class LastShift(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("startCash")
            val startCash: String?,
            @SerializedName("startVisa")
            val startVisa: String?,
            @SerializedName("endCash")
            val endCash: String?,
            @SerializedName("endVisa")
            val endVisa: String?,
            @SerializedName("currentCash")
            val currentCash: String?,
            @SerializedName("currentVisa")
            val currentVisa: String?,
            @SerializedName("endDate")
            val endDate: String?,
            @SerializedName("startDate")
            val startDate: String?,
            @SerializedName("branch")
            val branch: Any?,
            @SerializedName("statistics")
            val statistics: Statistics?
        ) {
            data class Statistics(
                @SerializedName("totalPrice")
                val totalPrice: Int?,
                @SerializedName("startDay")
                val startDay: String?,
                @SerializedName("endDate")
                val endDate: String?,
                @SerializedName("startTime")
                val startTime: String?,
                @SerializedName("endTime")
                val endTime: String?
            )
        }
    }
}