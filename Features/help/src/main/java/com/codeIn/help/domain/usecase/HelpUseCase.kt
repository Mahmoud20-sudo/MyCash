package com.codeIn.help.domain.usecase

import com.codeIn.help.data.model.response.GetHelpDTO
import com.codeIn.help.domain.repository.HelpRepository
import retrofit2.Response
import javax.inject.Inject

class HelpUseCase @Inject constructor( private val repository: HelpRepository){
    suspend operator fun invoke(): Response<GetHelpDTO> {
        return repository.getHelp()
    }
}