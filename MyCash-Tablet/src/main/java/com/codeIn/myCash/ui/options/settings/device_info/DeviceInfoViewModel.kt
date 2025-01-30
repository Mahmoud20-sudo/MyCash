package com.codeIn.myCash.ui.options.settings.device_info

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.usecases.GetProfileInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class DeviceInfoViewModel @Inject constructor(
    private val useCase: GetProfileInfoUseCase,
    ) : ViewModel() {

    private val _dataResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val dataResult = _dataResult.asStateFlow()

    init {
        getInfo()
    }

    private fun getInfo(){
        launchIO {
            _dataResult.emit(AuthenticationState.Loading)
            useCase.invoke().let {
                _dataResult.emit(it)
            }
        }
    }
}