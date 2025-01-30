package com.codeIn.myCash.ui.authentication.sign_up.device_selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.AppConstants.DEVICE_REGISTER
import com.codeIn.common.data.AuthConsts
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DeviceModel
import com.codeIn.myCash.features.user.data.settings.remote.response.device.DevicesState
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
import com.codeIn.myCash.features.user.domain.settings.usecases.GetDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class DeviceSelectionViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase,
    private val state: SavedStateHandle,
    private val prefs : SharedPrefsModule,
) : ViewModel() {

    private val _devicesResult = MutableStateFlow<DevicesState>(DevicesState.Idle)
    val devicesResult = _devicesResult.asStateFlow()

    private var _selectedDevice = MutableLiveData<DeviceModel?>(null)
    val selectedDevice:LiveData<DeviceModel?> = _selectedDevice

    private var _selectedPackage = MutableLiveData<Package?>(null)
    val selectedPackage:LiveData<Package?> = _selectedPackage

    init {
        if (state.contains(AuthConsts.DEVICE_SUBSCRIPTION))
            _selectedDevice.value = state[AuthConsts.DEVICE_SUBSCRIPTION]
        else
            _selectedDevice.value = null

        if (state.contains(AuthConsts.PACKAGE_SUBSCRIPTION))
            _selectedPackage.value = state[AuthConsts.PACKAGE_SUBSCRIPTION]
        else
            _selectedPackage.value = null

        getDevices()
    }


    fun getDevices() {
        launchIO {
            _devicesResult.emit(DevicesState.Loading)
            getDevicesUseCase.invoke(countryId = "1" ).let {
                _devicesResult.emit(it)
            }
        }
    }


    fun clearState() {
        _devicesResult.value = DevicesState.Idle
        _selectedDevice.value = null
        state[AuthConsts.DEVICE_SUBSCRIPTION] = null
    }

    fun payDevice(device : DeviceModel){
        _selectedDevice.value = device
        prefs.putValue(DEVICE_REGISTER , device.id.toString())
    }
}