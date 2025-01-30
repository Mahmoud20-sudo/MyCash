package com.codein.common.printer

import android.os.Build

enum class DeviceIntegration(value : String) {
    Sunmi("VSTC") , // Sunmi
    NewLand("newland") , // NewLand
    Marta("Vanstone") , // Marta
    SurePay("QUALCOMM") , // SurePay
    BLU("BLU") , // BLU
    Geidea("lephone") , // Geidea
    Pioneers("Wiseasy") , // Pioneers
    Swan("Swan 1") ,
}

fun <E : Enum<E>?> isInEnum(value: String, enumClass: Class<E>): Boolean {
    for (e in enumClass.enumConstants!!) {
        if (e!!.name == value) {
            return true
        }
    }
    return false
}
 fun getManufacturerInfo() : String{
    return Build.MANUFACTURER
}