package com.codeIn.help.data.model.response


import com.google.gson.annotations.SerializedName

data class GetHelpDTO(
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
        @SerializedName("address")
        val address: String?,
        @SerializedName("address_en")
        val addressEn: String?,
        @SerializedName("address_ar")
        val addressAr: String?,
        @SerializedName("payment")
        val payment: String?,
        @SerializedName("payment_en")
        val paymentEn: Any?,
        @SerializedName("payment_ar")
        val paymentAr: Any?,
        @SerializedName("footer")
        val footer: Any?,
        @SerializedName("footer_en")
        val footerEn: Any?,
        @SerializedName("footer_ar")
        val footerAr: Any?,
        @SerializedName("homeTitle")
        val homeTitle: Any?,
        @SerializedName("homeTitle_en")
        val homeTitleEn: Any?,
        @SerializedName("homeTitle_ar")
        val homeTitleAr: Any?,
        @SerializedName("homeSubTitle")
        val homeSubTitle: Any?,
        @SerializedName("homeSubTitle_en")
        val homeSubTitleEn: Any?,
        @SerializedName("homeSubTitle_ar")
        val homeSubTitleAr: Any?,
        @SerializedName("terms")
        val terms: String?,
        @SerializedName("terms_en")
        val termsEn: String?,
        @SerializedName("terms_ar")
        val termsAr: String?,
        @SerializedName("privacy")
        val privacy: String?,
        @SerializedName("privacy_en")
        val privacyEn: String?,
        @SerializedName("privacy_ar")
        val privacyAr: String?,
        @SerializedName("facebook")
        val facebook: String?,
        @SerializedName("twitter")
        val twitter: String?,
        @SerializedName("instagram")
        val instagram: String?,
        @SerializedName("youtube")
        val youtube: String?,
        @SerializedName("snap")
        val snap: Any?,
        @SerializedName("tiktok")
        val tiktok: String?,
        @SerializedName("whatsapp")
        val whatsapp: Any?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("googlePlay")
        val googlePlay: String?,
        @SerializedName("appStore")
        val appStore: String?,
        @SerializedName("tax")
        val tax: Int?,
        @SerializedName("video")
        val video: String?
    )
}