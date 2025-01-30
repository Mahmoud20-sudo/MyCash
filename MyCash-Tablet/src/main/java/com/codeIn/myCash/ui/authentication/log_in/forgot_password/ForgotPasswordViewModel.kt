package com.codeIn.myCash.ui.authentication.log_in.forgot_password

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.GeneralResponse
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.usecases.SendOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * a view model for [ForgotPasswordFragment] it is responsible for handling the UI logic and the vendorsData for the fragment.
 */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val useCase: SendOtpUseCase
) : ViewModel() {

    private val _generalResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val generalResult = _generalResult.asStateFlow()
    fun forgotPassword(key: String, type: String) {
        launchIO {
            _generalResult.emit(AuthenticationState.Loading)
            useCase.invoke(key = key , type= type , countryId = "1").let {
                _generalResult.emit(it)
                Log.d("TAG" , "$key , $type $it")
            }
        }
    }

    fun clearViewState() {
        if (_generalResult.value !is AuthenticationState.Idle)
            _generalResult.value = AuthenticationState.Idle
    }
}