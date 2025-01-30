package com.codeIn.myCash.features.user.data.settings.remote.response.settings

import com.google.gson.annotations.SerializedName

data class SettingsData(
    @SerializedName("address")
    val address: String,
    @SerializedName("address_ar")
    val addressAr: String,
    @SerializedName("address_en")
    val addressEn: String,
    @SerializedName("appStore")
    val appStore: String,
    @SerializedName("facebook")
    val facebook: String,
    @SerializedName("footer")
    val footer: String,
    @SerializedName("footer_ar")
    val footerAr: String,
    @SerializedName("footer_en")
    val footerEn: String,
    @SerializedName("googlePlay")
    val googlePlay: String,
    @SerializedName("homeSubTitle")
    val homeSubTitle: String,
    @SerializedName("homeSubTitle_ar")
    val homeSubTitleAr: String,
    @SerializedName("homeSubTitle_en")
    val homeSubTitleEn: String,
    @SerializedName("homeTitle")
    val homeTitle: String,
    @SerializedName("homeTitle_ar")
    val homeTitleAr: String,
    @SerializedName("homeTitle_en")
    val homeTitleEn: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("instagram")
    val instagram: String,
    @SerializedName("payment")
    val payment: String,
    @SerializedName("payment_ar")
    val paymentAr: String,
    @SerializedName("payment_en")
    val paymentEn: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("privacy")
    val privacy: String,
    @SerializedName("privacy_ar")
    val privacyAr: String,
    @SerializedName("privacy_en")
    val privacyEn: String,
    @SerializedName("snap")
    val snap: String,
    @SerializedName("tax")
    val tax: String,
    @SerializedName("terms")
    val terms: String,
    @SerializedName("terms_ar")
    val termsAr: String,
    @SerializedName("terms_en")
    val termsEn: String,
    @SerializedName("tiktok")
    val tiktok: String,
    @SerializedName("twitter")
    val twitter: String,
    @SerializedName("video")
    val video: String,
    @SerializedName("youtube")
    val youtube: String
)
