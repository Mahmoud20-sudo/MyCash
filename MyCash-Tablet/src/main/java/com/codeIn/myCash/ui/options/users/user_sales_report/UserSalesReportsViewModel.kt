package com.codeIn.myCash.ui.options.users.user_sales_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserSalesReportsViewModel @Inject constructor() : ViewModel() {
    //class name for debug and errors logging
    private val debugTAG by lazy { this.javaClass.name }

    //for testing purposes only (to be removed) TODO: remove this
    private var salesReportsList = SalesReport.salesReports

    private val _salesReports = MutableLiveData<List<SalesReport>>()
    val salesReports: LiveData<List<SalesReport>> = _salesReports

    init {
        _salesReports.postValue(salesReportsList)
    }
}