package com.codeIn.myCash.ui

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringDef
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.Style
import com.codeIn.myCash.R
import com.codeIn.myCash.ui.ViewStrokes.R0_S1
import com.codeIn.myCash.ui.ViewStrokes.R100_S1
import com.codeIn.myCash.ui.ViewStrokes.R12_S1
import com.codeIn.myCash.ui.ViewStrokes.R12_S5
import com.codeIn.myCash.ui.ViewStrokes.ViewStroke
import com.codeIn.myCash.utilities.views.CustomTextInputLayout
import com.codeIn.myCash.utilities.views.changeDrawableAndTextColors
import com.google.android.material.textfield.TextInputLayout


object ViewStrokes {
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(R12_S1, R12_S5, R100_S1, R0_S1)
    annotation class ViewStroke

    const val R12_S1 = "r12_s1"
    const val R12_S5 = "r12_s5"
    const val R100_S1 = "r100_s1"
    const val R0_S1 = "r0_s1"
}


/**
 * This function is used to update the style of the sections views that highlight the selected section
 * @param context the context to get the resources
 * @param viewsToStyle the list of views to style
 * @param selectedView the view that needs to be highlighted. If null, all the views will be styled with the secondary style
 */
fun updateSectionsViews(
    context: Context,
    viewsToStyle: List<TextView>,
    selectedView: TextView? = null,
    @ViewStroke stroke: String = R100_S1
) {

    val (secondaryBg, primaryBg) = when (stroke) {
        R12_S1 -> R.drawable.bg_white_r12_s1_stroke60_ripple to R.drawable.bg_white_r12_s2_secondary_ripple
        R12_S5 -> R.drawable.bg_white_r12_s5_stroke20_ripple to R.drawable.bg_white_r12_s2_secondary_ripple
        R0_S1 -> R.drawable.bg_white_s1_main_ripple to R.drawable.bg_white_s1_secondary_ripple
        else -> R.drawable.bg_white_r100_s1_stroke_ripple to R.drawable.bg_white_r100_s1_secondary_ripple
    }

    // Define the secondary style to be applied to the views that are not selected
    val secondaryStyle = Style(
        color = ContextCompat.getColor(context, R.color.mainBlack),
        backgroundResource = secondaryBg,
        typeface = ResourcesCompat.getFont(context, R.font.regular)
    )

    // Define the primary style to be applied to the selected view
    val primaryStyle = Style(
        color = ContextCompat.getColor(context, R.color.secondaryColor),
        backgroundResource = primaryBg,
        typeface = ResourcesCompat.getFont(context, R.font.semi_bold)
    )

    // initially apply the secondary style to all the views
    viewsToStyle.forEach { view ->
        view.changeDrawableAndTextColors(
            color = secondaryStyle.color,
            backgroundResource = secondaryStyle.backgroundResource,
            typeface = secondaryStyle.typeface
        )
    }

    // apply the primary style to the selected view
    selectedView?.changeDrawableAndTextColors(
        color = primaryStyle.color,
        backgroundResource = primaryStyle.backgroundResource,
        typeface = primaryStyle.typeface
    )
}


fun hideKeyboard(context: Context, view: View?) {
    if (view != null) {
        (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }
}

fun CustomTextInputLayout.setErrorMsg(message: String) = binding.textInputLayout.apply {
    error = null
    isErrorEnabled = false
    error = message
}

fun TextInputLayout.resetError(){
    error = null
    isErrorEnabled = false
}

fun EditText.setTextWatcher(
    doBeforeTextChanged: (String) -> Unit = {},
    doOnTextChanged: (String) -> Unit = {},
    doAfterTextChanged: (String) -> Unit = {}
) =
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) =
            doBeforeTextChanged.invoke(charSequence.toString())

        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) =
            doOnTextChanged.invoke(charSequence.toString())

        override fun afterTextChanged(editable: Editable) =
            doAfterTextChanged.invoke(editable.toString())
    })

