package com.codeIn.myCash.features.user.domain.edit_profile.repository

import com.codeIn.myCash.features.user.data.edit_profile.model.response.EditCodeDTO
import retrofit2.Response

interface EditCodeRepository {
    suspend fun editCode(phone :String , type:Int,email:String):Response<EditCodeDTO>
}