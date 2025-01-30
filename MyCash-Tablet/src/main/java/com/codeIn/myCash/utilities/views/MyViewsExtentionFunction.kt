package com.codeIn.myCash.utilities.views

import android.graphics.Typeface
import android.widget.TextView

/**
 * Extension function to change the color of the TextView and its compound drawables to the given color
 * and change the background resource to the given background resource (optional)
 * and change the typeface to the given typeface (optional)
 *
 * @param color the color to change the TextView and its compound drawables to
 * @param backgroundResource the background resource to change the TextView to (optional)
 * @param typeface the typeface to change the TextView to (optional)
 */
fun TextView.changeDrawableAndTextColors(
    color: Int,
    backgroundResource: Int? = null,
    typeface: Typeface? = null,
    skipDrawable: Boolean = false
) {
    this.setTextColor(color)
    if (backgroundResource != null) {
        this.setBackgroundResource(backgroundResource)
    }
    if (skipDrawable)
        for (drawable in this.compoundDrawablesRelative)
            drawable?.setTintList(null)
    else
        for (drawable in this.compoundDrawablesRelative)
            drawable?.setTint(color)

    if (typeface != null)
        this.typeface = typeface
}