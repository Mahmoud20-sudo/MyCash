package com.codeIn.myCash.ui.authentication.log_in.reset_password

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.usecases.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * a ViewModel class for [ResetPasswordFragment] fragment. This class is responsible for handling UI logic and preparing vendorsData for [ResetPasswordFragment] fragment.
 */
@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val useCase: ResetPasswordUseCase,
    private val state: SavedStateHandle,
) : ViewModel() {
    private val _authResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val authResult = _authResult.asStateFlow()

    fun resetPassword(password: String, confirmPassword: String) {
        launchIO {
            _authResult.emit(AuthenticationState.Loading)
            useCase.invoke(password, confirmPassword)
                .let {
                    _authResult.emit(it)
                }
        }
    }

    fun clearViewState() {
        if (_authResult.value !is AuthenticationState.Idle)
            _authResult.value = AuthenticationState.Idle
    }
}