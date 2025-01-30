package com.codeIn.myCash.ui.options.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.NotificationsType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    // for testing purposes only (to be removed) TODO: remove this
    private var myNotificationsList = arrayListOf(
        Notification(1, "Title 1", "Subtitle 1", "2023-10-25", 1),
        Notification(2, "Title 2", "Subtitle 2", "2023-10-26", 2),
        Notification(3, "Title 3", "Subtitle 3", "2023-10-27", 3),
        Notification(4, "Title 4", "Subtitle 4", "2023-10-28", 4),
        Notification(5, "Title 5", "Subtitle 5", "2023-10-29", 2),
        Notification(6, "Title 6", "Subtitle 6", "2023-10-30", 4),
        Notification(7, "Title 7", "Subtitle 7", "2023-10-31", 3)
    )

    //the notifications type that the user is currently viewing
    private val _notificationsType = MutableLiveData<NotificationsType>()
    val notificationsType: LiveData<NotificationsType> = _notificationsType

    //the list of the Notifications that the user is currently viewing
    private val _notificationsList = MutableLiveData<List<Notification>>()
    val notificationsList: LiveData<List<Notification>> = _notificationsList

    /**
     * init block to set the default value of the _notificationsType MutableLiveData
     */
    init {
        _notificationsType.postValue(NotificationsType.ALL)
        _notificationsList.postValue(myNotificationsList)
    }


    /**
     * function to update the notifications type that will be called from the fragment when the user updates the notifications type
     * it takes the new type as a parameter and updates the notificationsType with the new vendorsData
     * @param notificationsType [NotificationsType] the new type that the user is viewing
     * @return [Unit]
     */
    fun updateNotificationsType(notificationsType: NotificationsType) {
        if (_notificationsType.value != notificationsType)
            _notificationsType.postValue(notificationsType)
    }

}