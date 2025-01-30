package com.codeIn.myCash.utilities

import android.text.Editable
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.roundToInt

/**
 * string to editable to be used in displaying text on editText because it accepts only editable not String
 * you can use setText(s:String) ... but using kotlin extension functions is better than old java method ,,it might be deprecated at anyTime..
 */
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


/**
 * double to toTwoDigits double to be used in displaying numbers ,,
 * CALL ONLY ON SETTING DOUBLES ON TEXT_VIEWS ,,, Don't change the variables
 */
fun Double.toTwoDigits(): Double = (this * 100.0).roundToInt() / 100.0

fun Double.toDecimalFormat(): String = DecimalFormat("###.#").format(this)


fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.visibleIf(flag: Boolean) {
    this.visibility = if (flag) View.VISIBLE else View.GONE
}

fun View.goneIf(flag: Boolean) {
    this.visibility = if (flag) View.VISIBLE else View.GONE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}


fun ViewModel.launch(block: suspend () -> Unit) {
    viewModelScope.launch {
        block()
    }
}

fun ViewModel.launchIO(block: suspend () -> Unit) {
    viewModelScope.launch(context = Dispatchers.IO) {
        block()
    }
}

fun ViewModel.launchMain(block: suspend () -> Unit) {
    viewModelScope.launch(Dispatchers.Main) {
        block()
    }
}

fun ViewModel.launchDefault(block: suspend () -> Unit) {
    viewModelScope.launch(Dispatchers.Default) {
        block()
    }
}