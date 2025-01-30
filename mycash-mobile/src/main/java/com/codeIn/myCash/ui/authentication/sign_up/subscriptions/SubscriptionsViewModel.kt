package com.codeIn.myCash.ui.authentication.sign_up.subscriptions

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.AppConstants.PACKAGE_REGISTER
import com.codeIn.common.data.AuthConsts.PACKAGE_SUBSCRIPTION
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.PackagesState
import com.codeIn.myCash.features.user.domain.settings.usecases.GetPackagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
/**
 * a view model class for [SubscriptionsFragment] fragment, it's used to handle the UI logic and prepare the vendorsData for the UI.
 */
@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val getPackagesUseCase: GetPackagesUseCase,
    val state: SavedStateHandle,
    private val prefs : SharedPrefsModule,
) : ViewModel() {

    private val _subscriptionsResult = MutableStateFlow<PackagesState>(PackagesState.Idle)
    val subscriptionsResult = _subscriptionsResult.asStateFlow()

    private val _selectedPackage = MutableStateFlow<Package?>(null)
    val selectedPackage = _selectedPackage.asStateFlow()


    init {
        if (state.contains(PACKAGE_SUBSCRIPTION))
            _selectedPackage.value = state[PACKAGE_SUBSCRIPTION]
        else
            _selectedPackage.value = null

        getPackages()
    }




    fun getPackages() {
        launchIO{
            _subscriptionsResult.emit(PackagesState.Loading)
            getPackagesUseCase.invoke(countryId = null).let {
                Log.d("TAG" , "packages ... $it")
                _subscriptionsResult.emit(it)
            }
        }
    }

    fun clearState() {
        _subscriptionsResult.value = PackagesState.Idle
        _selectedPackage.value = null
        state[PACKAGE_SUBSCRIPTION] = null
    }

    fun subscripePackage(selected : Package){
        _selectedPackage.value = selected
        prefs.putValue(PACKAGE_REGISTER , selected.id.toString())
    }


}