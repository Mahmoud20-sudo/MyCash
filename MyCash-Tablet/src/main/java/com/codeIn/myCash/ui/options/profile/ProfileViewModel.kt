package com.codeIn.myCash.ui.options.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchDefault
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.R
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AccountInfo
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.data.edit_profile.model.response.EditCodeDTO
import com.codeIn.myCash.features.user.data.edit_profile.model.response.UpdateProfileDTO
import com.codeIn.myCash.features.user.domain.authentication.usecases.GetProfileInfoUseCase
import com.codeIn.myCash.features.user.domain.edit_profile.usecase.EditCodeUseCase
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
    private val useCase: GetProfileInfoUseCase,
    private val mUpdateEmailUseCase: UpdateEmailUseCase,
    private val mUpdatePhoneUseCase : UpdateProfileUseCase,
    private val mEditCodeUseCase: EditCodeUseCase,
    private val mUpdateNameUseCase: UpdateNameUseCase
) : ViewModel() {
    val accountInfo = AccountInfo(
        id = 1,
        taxRecord = "",
        commercialRecord = null,
        commercialRecordName = "CommercialRecordName",
        tax = "0",
        notification ="1",
        drafts = "2",
        quickInvoice = "3",
        logo = "File",
    )

    private val _profileResult =MutableStateFlow<AccountInfo>(accountInfo)
    val profileResult = _profileResult.asStateFlow()

    private val _phoneResult = MutableStateFlow<UpdateProfileDTO?>(null)
    val phoneResult = _phoneResult.asStateFlow()

    private val _editCode = MutableStateFlow<EditCodeDTO?>(null)
    val editCode = _editCode.asStateFlow()

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile = _imageFile.asStateFlow()
    private val _dataResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val dataResult = _dataResult.asStateFlow()

    private val _emailResult = MutableStateFlow<UpdateProfileDTO?>(null)
    val emailResult = _emailResult.asStateFlow()


    init {
        getInfo()
    }

    private fun getInfo(){
        launchIO {
            _dataResult.emit(AuthenticationState.Loading)
            useCase.invoke().let {
                _dataResult.emit(it)
                Log.d("TAG" , "Profile : $it")
            }
        }
    }

    fun profileUpdate(commercialRecord : String, logo: File?, commercialRecordName: String, tax: String,taxRecord:String , context : Context) {
        launchMain{
            _profileResult.emit(accountInfo)
            val response = mUpdateNameUseCase.invoke(commercialRecord, logo, commercialRecordName, tax,taxRecord)
            if (response.isSuccessful) {
                val result = response.body()
                _profileResult.emit(result?.data?.accountInfo!!)
                CustomToaster.show(
                    context,
                    context.getString(R.string.success_message),
                    isSuccess = true
                )
            } else {

                Log.e("nameUpdate", "Error: ${response.body()} , ${response.code()} , ${response.message()}")
            }
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
            _editCode.emit(null)
            val response = mEditCodeUseCase.invoke(phone, type, email)
            if (response.isSuccessful) {
                val result = response.body()
                _editCode.emit(result)
            } else {
                Log.e("editCodeUpdate", "Error: ${response.message()}")
                _editCode.emit(null)
            }
        }
    }


    fun updateImage(file: File){
        launchIO {
            _imageFile.emit(file)
        }
    }
    fun selectImage(imageFile: File) {
        _imageFile.value = imageFile // Update the selected image file in the ViewModel
    }

    // Add this function to your ViewModel to update the selected image file
    fun setImageFile(imageFile: File) {
        _imageFile.value = imageFile
    }
}