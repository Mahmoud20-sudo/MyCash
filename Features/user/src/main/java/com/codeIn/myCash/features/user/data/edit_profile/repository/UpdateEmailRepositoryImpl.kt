package com.codeIn.myCash.features.user.data.edit_profile.repository

import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.edit_profile.model.response.UpdateProfileDTO
import com.codeIn.myCash.features.user.data.edit_profile.remote.ApiEditProfile
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdateEmailRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class UpdateEmailRepositoryImpl @Inject constructor(
    private val api: ApiEditProfile,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : UpdateEmailRepository {
    override suspend fun updateEmail(email: String, code: Int): Response<UpdateProfileDTO> {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val response = api.updateEmail(lang, token, email, code)
            response
        } catch (throwable: Throwable) {
            // Handle exceptions if needed
            Response.error(500, ResponseBody.create("application/json".toMediaTypeOrNull(), ""))
        }
    }
}