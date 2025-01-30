package com.codeIn.common.printer

import android.os.Build

enum class DeviceIntegration {
    VSTC , // Sunmi
    SUNMI , // SUNMI V3 MIX
    newland , // NewLand
    Vanstone , // Marta
    QUALCOMM , // SurePay
    BLU , // BLU
    lephone , // Geidea,
    Wiseasy , // Pioneers
}
enum class MyCashDevices {
    SUNMI , // SUNMI V3 MIX
    lephone , // Geidea,
    None , // non,
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