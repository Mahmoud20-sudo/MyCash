package com.codeIn.help.domain.usecase

import com.codeIn.help.data.model.response.HowWorkDTO
import com.codeIn.help.domain.repository.HowWorkRepository
import retrofit2.Response
import javax.inject.Inject

class HowWorkUseCase @Inject constructor(private val repository: HowWorkRepository){
    suspend operator fun invoke(): Response<HowWorkDTO> {
        return repository.getHowWork()
    }
}