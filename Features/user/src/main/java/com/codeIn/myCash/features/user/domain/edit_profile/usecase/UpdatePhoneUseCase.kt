package com.codeIn.myCash.features.user.domain.edit_profile.usecase

import com.codeIn.myCash.features.user.data.edit_profile.model.response.UpdateProfileDTO
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdatePhoneRepository
import retrofit2.Response
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private  val repository : UpdatePhoneRepository){
    suspend operator fun invoke(phone:String ,code:Int):Response<UpdateProfileDTO>{
        return repository.updatePhone(phone,code)
    }
}