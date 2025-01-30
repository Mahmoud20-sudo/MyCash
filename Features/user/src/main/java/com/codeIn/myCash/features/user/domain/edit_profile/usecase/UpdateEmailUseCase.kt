package com.codeIn.myCash.features.user.domain.edit_profile.usecase

import com.codeIn.myCash.features.user.data.edit_profile.model.response.UpdateProfileDTO
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdateEmailRepository
import retrofit2.Response
import javax.inject.Inject

class UpdateEmailUseCase @Inject constructor(private  val repository :UpdateEmailRepository){
    suspend operator fun invoke(email:String ,code:Int): Response<UpdateProfileDTO> {
        return  repository.updateEmail(email, code)
    }
}