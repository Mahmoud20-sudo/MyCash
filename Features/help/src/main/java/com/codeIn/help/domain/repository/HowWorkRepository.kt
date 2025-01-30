package com.codeIn.help.domain.repository

import com.codeIn.help.data.model.response.GetHelpDTO
import com.codeIn.help.data.model.response.HowWorkDTO
import retrofit2.Response

interface HowWorkRepository {
    suspend fun getHowWork(): Response<HowWorkDTO>
}