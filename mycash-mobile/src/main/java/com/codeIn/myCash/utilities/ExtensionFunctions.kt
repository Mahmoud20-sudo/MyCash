package com.codeIn.myCash.utilities

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.myCash.R
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale


fun String.addSar(context: Context): String {
    return "$this ${context.getString(R.string.sar)}"
}

fun ErrorEntity.getErrorMessage(context: Context): String {
    return when (this) {
        ErrorEntity.AccessDenied -> context.getString(R.string.access_denied_403)
        ErrorEntity.Network -> context.getString(R.string.no_internet_connection)
        ErrorEntity.NotFound -> context.getString(R.string.not_found_404)
        ErrorEntity.ServerInternalError -> context.getString(R.string.server_internal_error_500)
        ErrorEntity.ServiceUnavailable -> context.getString(R.string.service_unavailable_503)
        ErrorEntity.Unknown -> context.getString(R.string.unknown_error)
    }
}

fun TextInputEditText.onClearFocusAndHideKeyboard(context: Context) {
    this.setOnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // Clear focus
            v.clearFocus()

            // Hide the keyboard
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)

            true
        } else false
    }

}
fun EditText.onClearFocusAndHideKeyboard(context: Context) {
    this.setOnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // Clear focus
            v.clearFocus()

            // Hide the keyboard
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)

            true
        } else false
    }

}

fun currencyFormatter(amount: Double, currency: String?) =
    String.format(Locale.ENGLISH, "%.2f %s", amount, currency)
