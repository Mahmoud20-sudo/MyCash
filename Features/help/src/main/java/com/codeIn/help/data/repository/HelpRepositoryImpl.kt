package com.codeIn.help.data.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.help.data.model.response.GetHelpDTO
import com.codeIn.help.data.remote.ApiHelp
import com.codeIn.help.domain.repository.HelpRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class HelpRepositoryImpl @Inject constructor(
    private val api : ApiHelp,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : HelpRepository{
    override suspend fun getHelp(): Response<GetHelpDTO> {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            api.getSetting(lang = lang)
        } catch (throwable: Throwable) {

            Response.error(500, ResponseBody.create(null, "Internal Server Error"))
        }
    }
}