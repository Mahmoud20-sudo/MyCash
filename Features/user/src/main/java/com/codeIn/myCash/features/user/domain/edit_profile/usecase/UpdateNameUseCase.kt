package com.codeIn.myCash.features.user.domain.edit_profile.usecase

import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.ProfileState
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdateProfileRepository
import java.io.File
import javax.inject.Inject

class UpdateNameUseCase @Inject constructor(private val repository: UpdateProfileRepository) {

    suspend operator fun invoke(
        commercialRecord: String,
        logo: File?,
        commercialRecordName: String,
        tax: String,
        taxRecord: String,
    ): ProfileState<UserDTO> {

        return repository.updateProfile(
            commercialRecord, logo, commercialRecordName, tax, taxRecord
        )
    }
}