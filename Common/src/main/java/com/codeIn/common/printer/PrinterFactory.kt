package com.codeIn.common.printer

import android.view.View

class PrinterFactory {
    fun print(view : View , container : View){
        val device = android.os.Build.MODEL
        if (DeviceIntegration.values().any { it.name == device })
        {
            when(device){
                DeviceIntegration.BLU.toString() ->{
                }
                DeviceIntegration.QUALCOMM.toString() ->{
                }
                DeviceIntegration.VSTC.toString() ->{
                }
                DeviceIntegration.lephone.toString() ->{
                }
                DeviceIntegration.Wiseasy.toString() ->{
                }
                DeviceIntegration.newland.toString() ->{
                }
                else -> {
                }
            }
        }
        else{
        }
    }
}