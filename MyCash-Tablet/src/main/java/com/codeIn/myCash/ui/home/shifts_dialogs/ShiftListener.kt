package com.codeIn.myCash.ui.home.shifts_dialogs

interface ShiftListener {
    fun startShift(cash : String? , visa : String? , differentCash : String? , differentVisa : String?)
    fun endShift(cash : String? , visa : String?)
}