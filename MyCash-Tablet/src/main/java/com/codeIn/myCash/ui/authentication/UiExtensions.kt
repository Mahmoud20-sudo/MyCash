package com.codeIn.myCash.ui.authentication

import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.showError(editText: TextInputEditText) {
    this.isHovered = true
    editText.isHovered = true
    try {
        Handler(Looper.myLooper()!!).postDelayed({
            this.isHovered = false
            editText.isHovered = false
        }, 2000)
    } catch (e: Exception) {
        e.printStackTrace()
        this.isHovered = false
        editText.isHovered = false
    }

}

fun EditText.showError() {
    this.isHovered = true
    try {
        Handler(Looper.myLooper()!!).postDelayed({
            this.isHovered = false
        }, 2000)
    } catch (e: Exception) {
        e.printStackTrace()
        this.isHovered = false
    }
}

fun TextView.showError() {
    this.isHovered = true
    try {
        Handler(Looper.myLooper()!!).postDelayed({
            this.isHovered = false
        }, 2000)
    } catch (e: Exception) {
        e.printStackTrace()
        this.isHovered = false
    }
}