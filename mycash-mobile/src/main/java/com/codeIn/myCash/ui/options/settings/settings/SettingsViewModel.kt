package com.codeIn.myCash.ui.options.settings.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.codeIn.myCash.features.user.domain.authentication.usecases.EnableFeaturesOfSettingsUseCase
import com.codeIn.myCash.features.user.domain.authentication.usecases.GetProfileInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: GetProfileInfoUseCase,
    private val enableFeaturesOfSettingsUseCase : EnableFeaturesOfSettingsUseCase
    ) : ViewModel() {

    private val _dataResult = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val dataResult = _dataResult.asStateFlow()

    private val _initial = MutableStateFlow<Int>(0)
    val initial = _initial.asStateFlow()
    init {
        getInfo()
    }


    private fun getInfo(){
        launchIO {
            useCase.invoke().let {
                _dataResult.emit(it)
                Log.d("TAG" , "Profile : $it")
            }
        }
    }

    fun enableFeatures(drafts: String? , notifications: String? , quickInvoiceMode : String?){
        launchIO {
            enableFeaturesOfSettingsUseCase.invoke(drafts = drafts , notification = notifications , quickInvoiceMode = quickInvoiceMode).let {
                _dataResult.emit(it)
                Log.d("TAG" , " $drafts , $notifications , $quickInvoiceMode enable features : $it")
            }
        }
    }
    fun initialized(){
        launchIO {
            _initial.emit(1)
        }
    }

    fun clearState(){
        _dataResult.value = AuthenticationState.Idle
    }
}