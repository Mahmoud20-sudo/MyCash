package com.codeIn.help.domain.repository

import com.codeIn.help.data.model.response.GetHelpDTO
import retrofit2.Response

interface HelpRepository {
    suspend fun getHelp(): Response<GetHelpDTO>
}