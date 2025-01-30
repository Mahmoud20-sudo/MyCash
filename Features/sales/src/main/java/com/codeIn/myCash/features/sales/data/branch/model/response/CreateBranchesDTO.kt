package com.codeIn.sales.branch.data.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateBranchesDTO(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: Data?
): Parcelable {
    @Parcelize
    data class Data(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("address")
        val address: String?,
        @SerializedName("isMain")
        val isMain: Int?,
        @SerializedName("status")
        val status: Int?,
        @SerializedName("employee")
        val employee: Employee?
    ) :Parcelable{
        @Parcelize
        data class Employee(
            @SerializedName("name")
            val name: String?,
            @SerializedName("id")
            val id: Int?
        ):Parcelable
    }
}