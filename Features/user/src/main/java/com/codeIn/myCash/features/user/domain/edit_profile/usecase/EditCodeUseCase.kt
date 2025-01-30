package com.codeIn.myCash.features.user.domain.edit_profile.usecase

import com.codeIn.myCash.features.user.data.edit_profile.model.response.EditCodeDTO
import com.codeIn.myCash.features.user.domain.edit_profile.repository.EditCodeRepository
import retrofit2.Response
import javax.inject.Inject

class EditCodeUseCase @Inject constructor(private val repository: EditCodeRepository){
    suspend operator fun invoke(phone :String , type:Int,email:String):Response<EditCodeDTO> {
        return repository.editCode(phone, type, email)
    }
}