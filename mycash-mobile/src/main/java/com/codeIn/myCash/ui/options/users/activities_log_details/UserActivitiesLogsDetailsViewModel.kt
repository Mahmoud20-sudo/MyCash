package com.codeIn.myCash.ui.options.users.activities_log_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.myCash.ui.options.users.activities_logs.ActivityLog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserActivitiesLogsDetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    private val _activityLog = MutableLiveData<ActivityLog>()
    val activityLog: LiveData<ActivityLog> = _activityLog

    init {
        if (savedStateHandle.contains("activityLog"))
            _activityLog.value = savedStateHandle["activityLog"]
        else
            throw IllegalArgumentException("ActivityLog is not found in the savedStateHandle")

    }
}