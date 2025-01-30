package com.codeIn.myCash.ui.authentication.sign_up.termsCondition

import android.util.Log
import androidx.lifecycle.ViewModel
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.settings.remote.response.settings.SettingsState
import com.codeIn.myCash.features.user.domain.settings.usecases.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * a view model class for [SubscriptionsFragment] fragment, it's used to handle the UI logic and prepare the vendorsData for the UI.
 */
@HiltViewModel
class TermsAndConditionsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase
    ) : ViewModel() {

    private val _dataResult = MutableStateFlow<SettingsState>(SettingsState.Idle)
    val dataResult = _dataResult.asStateFlow()
    init {
        getSettings()
    }


    private fun getSettings(){
        launchIO {
            _dataResult.emit(SettingsState.Loading)
            getSettingsUseCase.invoke().let {
                _dataResult.emit(it)
                Log.d("TAG" , it.toString())
            }
        }
    }

}