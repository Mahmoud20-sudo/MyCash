package com.codeIn.common.data

import kotlin.math.roundToInt

object NumberHelper {

    fun persianToEnglishText(persianStr: String):String {
        var result = ""
        var en = '0'
        for (ch in persianStr) {
            en = ch
            when (ch) {
                '۰' -> en = '0'
                '۱' -> en = '1'
                '۲' -> en = '2'
                '۳' -> en = '3'
                '۴' -> en = '4'
                '۵' -> en = '5'
                '۶' -> en = '6'
                '۷' -> en = '7'
                '۸' -> en = '8'
                '۹' -> en = '9'
            }
            result = "${result}$en"
        }
        return result
    }


    fun roundOffDecimal(number: Double): String {
        val number3digits:Double = (number * 1000.0).roundToInt() / 1000.0
        val number2digits:Double = (number3digits * 100.0).roundToInt() / 100.0
        return number2digits.toString()
    }
}