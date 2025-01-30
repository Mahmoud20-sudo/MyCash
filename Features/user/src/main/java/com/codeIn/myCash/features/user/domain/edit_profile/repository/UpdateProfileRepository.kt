package com.codeIn.myCash.features.user.domain.edit_profile.repository

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.ProfileState
import java.io.File

interface UpdateProfileRepository {

    suspend fun updateProfile(
        commercialRecord: String,
        logo: File?,
        commercialRecordName: String,
        tax: String,
        taxRecord: String
    ): ProfileState<UserDTO>

}