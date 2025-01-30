package com.codeIn.myCash.ui.options.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.help.data.model.response.GetHelpDTO
import com.codeIn.help.data.model.response.HowWorkDTO
import com.codeIn.help.domain.usecase.HelpUseCase
import com.codeIn.help.domain.usecase.HowWorkUseCase
import com.codeIn.myCash.features.user.data.settings.remote.response.settings.SettingsState
import com.codeIn.myCash.features.user.domain.settings.usecases.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * a view model class for [SubscriptionsFragment] fragment, it's used to handle the UI logic and prepare the vendorsData for the UI.
 */
@HiltViewModel
class HelpViewModel @Inject constructor(
    private val mHelpUseCase: HelpUseCase,
    private val mHowWorkUseCase: HowWorkUseCase
) : ViewModel() {

    private val _dataResult = MutableStateFlow<List<GetHelpDTO>>(emptyList())
    val dataResult: StateFlow<List<GetHelpDTO>> = _dataResult

    private val _dataHowWorkResult = MutableStateFlow<List<HowWorkDTO.Data.Data>>(emptyList())
    val dataHowWorkResult: StateFlow<List<HowWorkDTO.Data.Data>> = _dataHowWorkResult

    init {
        getHelp()
        getHowWork()
    }

    private fun getHelp() {
        viewModelScope.launch {
            try {
                val response = mHelpUseCase.invoke()
                if (response.isSuccessful) {
                    val helpDTO = response.body()
                    val data = helpDTO
                    if (data != null) {
                        _dataResult.value = listOf(data) // Assuming you want to emit a single item
                    } else {
                        // Handle empty data case
                        // You can set _dataResult to an empty list or handle it differently based on your requirement
                        _dataResult.value = emptyList()
                    }
                } else {
                    // Handle unsuccessful response here
                }
            } catch (e: Exception) {
                // Handle exception here
            }
        }
    }

    private fun getHowWork() {
        viewModelScope.launch {
            try {
                val response = mHowWorkUseCase.invoke()
                if (response.isSuccessful) {
                    val branchesDTO = response.body()
                    branchesDTO?.data?.let { data ->
                        _dataHowWorkResult.value = data.data?.mapNotNull { nestedData ->
                            nestedData
                        } ?: emptyList()
                    }
                } else {
                    // Handle unsuccessful response here
                }
            } catch (e: Exception) {
                // Handle exception here
            }
        }
    }
}