package com.codeIn.myCash.ui.options.subscriptions.current_subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.SubscriptionsOptions
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.AuthenticationState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.features.user.domain.authentication.usecases.GetProfileInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CurrentSubscriptionViewModel @Inject constructor(
    private val useCase: GetProfileInfoUseCase,
) : ViewModel() {
    private val _dataResult = MutableLiveData<AuthenticationState>(AuthenticationState.Idle)
    val dataResult : LiveData<AuthenticationState> = _dataResult

    private val _selectedPackage = MutableStateFlow<Package?>(null)
    val selectedPackage = _selectedPackage.asStateFlow()

    init {
        getInfo()
    }

    private fun getInfo(){
        launchIO {
            _dataResult.postValue(AuthenticationState.Loading)
            useCase.invoke().let {
                _dataResult.postValue(it)
            }
        }
    }

    fun subscripePackage(selected : Package?){
        _selectedPackage.value = selected
    }
}