package com.codein.common.printer

import android.view.View

class PrinterFactory {
    fun print(view : View , container : View){
        val device = android.os.Build.MODEL

        if (DeviceIntegration.values().any { it.name == device })
        {
            when(device){
                DeviceIntegration.Swan.name.toString()->{

                }
                else -> {
                }
            }
        }
        else{
        }
    }
}