package com.codeIn.help.data.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.help.data.model.response.HowWorkDTO
import com.codeIn.help.data.remote.ApiHelp
import com.codeIn.help.domain.repository.HowWorkRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class HowWorkRepositoryImpl @Inject constructor(
    private val api: ApiHelp,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : HowWorkRepository {
    override suspend fun getHowWork(): Response<HowWorkDTO> {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            api.getHowWork(lang = lang)
        } catch (throwable: Throwable) {

            Response.error(500, ResponseBody.create(null, "Internal Server Error"))
        }
    }
}