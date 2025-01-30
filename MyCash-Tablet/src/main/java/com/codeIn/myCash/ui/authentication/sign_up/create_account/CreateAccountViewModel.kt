package com.codeIn.myCash.ui.authentication.sign_up.create_account

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AuthConsts
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val state : SavedStateHandle
) : ViewModel() {
    private val _authResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val authResult = _authResult.asStateFlow()

    private val _registerType = MutableStateFlow<Int>(0)
    val registerType = _registerType.asStateFlow()

    init {
        if (state.contains(AuthConsts.REGISTER_TYPE))
            _registerType.value = state[AuthConsts.REGISTER_TYPE]!!
    }
    fun registerUser(phone: String, email: String, password: String) {
        launchIO {
            _authResult.emit(AuthenticationState.Loading)
            registerUseCase.invoke(
                countryId = "1",
                phone = phone,
                password = password,
                email = email,
                type = _registerType.value
            ).let {
                _authResult.emit(it)
                Log.d("TAG" , "Result : ${phone} , ${email} , ${password} , ${it.toString()}")
            }
        }
    }

    fun clearRegisterState() {
        if (_authResult.value !is AuthenticationState.Idle)
            _authResult.value = AuthenticationState.Idle
    }

}