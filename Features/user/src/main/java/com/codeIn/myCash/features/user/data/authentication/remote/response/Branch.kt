package com.codeIn.myCash.features.user.data.authentication.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Branch(
    @SerializedName("address")
    val address: String?,
    @SerializedName("employee_id")
    val employeeId: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isMain")
    val isMain: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("user_id")
    val userId: String?
) : Parcelable {
    // I need to override the toString method to display the name of the branch in the spinner
    override fun toString(): String {
        return this.name ?: ""
    }
}
