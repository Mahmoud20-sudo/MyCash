package com.codeIn.myCash.features.user.domain.edit_profile.repository

import com.codeIn.myCash.features.user.data.edit_profile.model.response.UpdateProfileDTO
import retrofit2.Response

interface UpdatePhoneRepository {
    suspend fun updatePhone(phone : String ,code:Int):Response<UpdateProfileDTO>
}