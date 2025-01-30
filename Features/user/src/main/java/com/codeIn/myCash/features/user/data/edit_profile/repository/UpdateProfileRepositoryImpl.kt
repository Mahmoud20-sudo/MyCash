package com.codeIn.myCash.features.user.data.edit_profile.repository

import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.ProfileState
import com.codeIn.myCash.features.user.data.edit_profile.remote.ApiEditProfile
import com.codeIn.myCash.features.user.domain.edit_profile.repository.UpdateProfileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UpdateProfileRepositoryImpl @Inject constructor(
    private val api: ApiEditProfile,
    private val prefs: SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl
) : UpdateProfileRepository {

    override suspend fun updateProfile(
        commercialRecord: String,
        logo: File?,
        commercialRecordName: String,
        tax: String,
        taxRecord: String
    ): ProfileState<UserDTO> {
        val lang = prefs.getValue(Constants.getLang())
        val token = prefs.getValue(Constants.getToken())

        return try {

            val response = if (logo != null) {
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), logo)
                val image = MultipartBody.Part.createFormData("logo", logo.name, requestFile)
                api.updateProfile(
                    lang, token, image,
                    commercialRecord = commercialRecord,
                    commercialRecordName = commercialRecordName,
                    taxRecord = taxRecord,
                    tax = tax
                )
            } else {
                api.updateProfileWithoutImGW(
                    lang = lang,
                    authorization = token,
                    commercialRecord = commercialRecord,
                    commercialRecordName = commercialRecordName,
                    taxRecord = taxRecord,
                    tax = tax
                )
            }

            if (response.isSuccessful && response.body()?.status == 1) {

                response.body()?.let {
                    prefs.apply {
                        putValue(Constants.logoStore(), it.data?.accountInfo?.logo)
                        putValue(Constants.nameStore(), it.data?.accountInfo?.commercialRecordName)
                        putValue(Constants.taxRecordStore(), it.data?.accountInfo?.taxRecord)
                        putValue(Constants.completeInoStore(), it.data?.isCompleteAccountInfo)
                        putValue(Constants.paymentStatus() , it.data?.paymentStatus)
                        putValue(AppConstants.TAX, it.data?.accountInfo?.tax)
                    }
                    ProfileState.Success(it)
                } ?: run {
                    ProfileState.StateError(response.body()?.message.toString())
                }

            } else ProfileState.ServerError(errorHandler.invoke(response.code()))

        } catch (throwable: Throwable) {
            ProfileState.ServerError(errorHandler.getError(throwable))
        }
    }
}