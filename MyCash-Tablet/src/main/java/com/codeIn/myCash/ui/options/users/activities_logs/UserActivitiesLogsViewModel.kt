package com.codeIn.myCash.ui.options.users.activities_logs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserActivitiesLogsViewModel @Inject constructor() : ViewModel() {
    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    //for testing purposes only (to be removed) TODO: remove this
    private var activityLogsList = ActivityLog.activityLogs

    private val _activityLogs = MutableLiveData<List<ActivityLog>>()
    val activityLogs: LiveData<List<ActivityLog>> = _activityLogs

    init {
        _activityLogs.postValue(activityLogsList)
    }
}