package com.codeIn.myCash.ui.authentication.log_in.log_in

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.usecases.LoginUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.SaveCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase, private val cacheUseCase: SaveCacheUseCase) : ViewModel() {

    private val _authResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val authResult = _authResult.asStateFlow()

    fun login(phone: String, password: String) {
        launchIO {
            _authResult.emit(AuthenticationState.Loading)
            loginUseCase.invoke(
                phone = phone,
                countryId = "1",
                password = password
            ).let {
                _authResult.emit(it)
                Log.e("TAG", "login: $phone $password result: $it")
            }
        }
    }

    fun clearState() {
        if (_authResult.value != AuthenticationState.Idle)
            _authResult.value = AuthenticationState.Idle
    }

    fun saveCache(phone : String?, password: String?){
        launchIO {
            cacheUseCase.invoke(phone , password)
        }
    }

}