package com.codeIn.myCash.ui.options.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.ProfileState
import com.codeIn.myCash.features.user.data.edit_profile.model.response.UpdateProfileDTO
import com.codeIn.myCash.features.user.domain.edit_profile.usecase.EditCodeUseCase
import com.codeIn.myCash.features.user.domain.edit_profile.usecase.MyInfoUseCase
import com.codeIn.myCash.features.user.domain.edit_profile.usecase.UpdateEmailUseCase
import com.codeIn.myCash.features.user.domain.edit_profile.usecase.UpdateNameUseCase
import com.codeIn.myCash.features.user.domain.edit_profile.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileInfoUseCase: MyInfoUseCase,
    private val mUpdateEmailUseCase: UpdateEmailUseCase,
    private val mUpdatePhoneUseCase: UpdateProfileUseCase,
    private val mEditCodeUseCase: EditCodeUseCase,
    private val mUpdateNameUseCase: UpdateNameUseCase
) : ViewModel() {

    private val _updateProfile = MutableStateFlow<ProfileState<UserDTO>>(ProfileState.Idle)
    val updateProfile = _updateProfile.asStateFlow()

    private val _profileInfo = MutableStateFlow<ProfileState<UserDTO>>(ProfileState.Idle)
    val profileInfo = _profileInfo.asStateFlow()

    private val _phoneResult = MutableStateFlow<UpdateProfileDTO?>(null)
    val phoneResult = _phoneResult.asStateFlow()

    private val _emailResult = MutableStateFlow<UpdateProfileDTO?>(null)
    val emailResult = _emailResult.asStateFlow()


    init {
        getProfileInfo()
    }

    private fun getProfileInfo() = launchIO {
            _profileInfo.emit(ProfileState.Loading)
            val response = getProfileInfoUseCase()
            _profileInfo.emit(response)
        }


    fun profileUpdate(
        commercialRecord: String,
        logo: File?,
        commercialRecordName: String,
        tax: String,
        taxRecord: String
    ) {
        launchIO {
            _updateProfile.emit(ProfileState.Loading)
            val response = mUpdateNameUseCase(
                commercialRecord, logo, commercialRecordName, tax, taxRecord
            )
            _updateProfile.emit(response)
        }
    }
    fun emailUpdate(email: String, code: Int) {
        launchIO {
            _emailResult.emit(null)
            val response = mUpdateEmailUseCase.invoke(email, code)
            if (response.isSuccessful) {
                val result = response.body()
                _emailResult.emit(result)
            } else {
                Log.e("emailUpdate", "Error: ${response.message()}")
                _emailResult.emit(null)
            }
        }
    }
    fun phoneUpdate(phone: String, code: Int) {
        launchIO {
            _phoneResult.emit(null)
            val response = mUpdatePhoneUseCase.invoke(phone, code)
            if (response.isSuccessful) {
                val result = response.body()
                _phoneResult.emit(result)
            } else {
                Log.e("phoneUpdate", "Error: ${response.message()}")
                _phoneResult.emit(null)
            }
        }
    }
    fun editCode(phone :String , type:Int,email:String){
        launchIO {
            mEditCodeUseCase.invoke(phone, type, email)
        }
    }
}