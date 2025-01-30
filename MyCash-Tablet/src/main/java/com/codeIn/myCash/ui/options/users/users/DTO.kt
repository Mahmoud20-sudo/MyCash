package com.codeIn.myCash.ui.options.users.users

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (

    @SerializedName("id"                 ) var id                 : Int?                = null,
    @SerializedName("title "             ) var title              : String?             = null,
    @SerializedName("email"              ) var email              : String?             = null,
    @SerializedName("address"            ) var address            : String?             = null,
    @SerializedName("is_main"            ) var isMain             : Boolean?            = null,
    @SerializedName("is_active"          ) var isActive           : Boolean?            = null,
    @SerializedName("phone_number"       ) var phoneNumber        : String?             = null,
) : Parcelable {
    companion object{
        var usersList = arrayListOf(
            User(id = 1, title = "Title 1", email = "email1@ee.co", address = "address 1", isMain = true, isActive = true, phoneNumber = "1234567890"),
            User(id = 2, title = "Title 2", email = "email2@ee.com", address = "address 2", isMain = false, isActive = true, phoneNumber = "1234567890"),
            User(id = 3, title = "Title 3", email = "email3@ee.com", address = "address 2", isMain = false, isActive = true, phoneNumber = "1234567890"),
            User(id = 4, title = "Title 4", email = "email4@ee.com", address = "address 2", isMain = false, isActive = true, phoneNumber = "1234567890"),
            User(id = 5, title = "Title 5", email = "email5@ee.com", address = "address 2", isMain = false, isActive = true, phoneNumber = "1234567890"),
            )
    }
}