package com.codeIn.myCash.features.user.data.users.model.response


import android.os.Parcelable
import com.codeIn.myCash.features.user.data.authentication.remote.response.Branch
import com.codeIn.myCash.features.user.data.settings.remote.response.device.Device
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Feature
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

data class GetUsersDTO(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: Any?,
    @SerializedName("data")
    val `data`: Data?
) {
    data class Data(
        @SerializedName("data")
        val `data`: List<Data?>?,
        @SerializedName("pagination")
        val pagination: Pagination?
    ) {

        @Parcelize
        data class Data(
            @SerializedName("id")
            val id: Int?,
            @SerializedName("name")
            val name: String?,
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
            val firebase: String?,
            @SerializedName("note")
            val note: String?,
            @SerializedName("token")
            val token: String?,
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
            val lastShift: String?,
            @SerializedName("branch")
            val branch: Branch?,
            @SerializedName("is_main")
            var isMain: Boolean? = null,
            @SerializedName("is_active") var isActive: Boolean? = null,
        ) : Parcelable {

            @Parcelize
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
            ) : Parcelable

            @Parcelize
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
                val deviceToken: String?,
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
                val device: Device?,
                @SerializedName("package")
                val packageX: Package?,
                @SerializedName("influencer")
                val influencer: String?
            ) : Parcelable {
                @Parcelize
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
                    val descAr: String?,
                    @SerializedName("desc_en")
                    val descEn: String?,
                    @SerializedName("desc")
                    val desc: String?,
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
                    val features: List<Feature?>?
                ) : Parcelable
            }

            @Parcelize
            data class AccountInfo(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("taxRecord")
                val taxRecord: String?,
                @SerializedName("commercialRecord")
                val commercialRecord: String?,
                @SerializedName("commercialRecordName")
                val commercialRecordName: String?,
                @SerializedName("tax")
                val tax: String?,
                @SerializedName("notification")
                val notification: Int?,
                @SerializedName("drafts")
                val drafts: Int?,
                @SerializedName("quickInvoice")
                val quickInvoice: Int?,
                @SerializedName("logo")
                val logo: String?
            ) : Parcelable

            @Parcelize
            data class MainBranch(
                @SerializedName("id")
                val id: Int?,
                @SerializedName("name")
                val name: String?,
                @SerializedName("address")
                val address: String?,
                @SerializedName("phone")
                val phone: String?,
                @SerializedName("country")
                val country: String?,
                @SerializedName("city")
                val city: String?,
                @SerializedName("additional_info")
                val additionalInfo: String?,
                @SerializedName("isMain")
                val isMain: Int?,
                @SerializedName("status")
                val status: Int?,
                @SerializedName("employee")
                val employee: Employee?
            ) : Parcelable {
                @Parcelize
                data class Employee(
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("id")
                    val id: Int?
                ) : Parcelable
            }
        }

        data class Pagination(
            @SerializedName("total")
            val total: Int?,
            @SerializedName("count")
            val count: Int?,
            @SerializedName("per_page")
            val perPage: Int?,
            @SerializedName("current_page")
            val currentPage: Int?,
            @SerializedName("total_pages")
            val totalPages: Int?,
            @SerializedName("is_pagination")
            val isPagination: Boolean?
        )
    }
}