package net.geidea.paymentsdk.ui.widget.address

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import net.geidea.paymentsdk.R
import net.geidea.paymentsdk.ui.widget.FormEditText
import net.geidea.paymentsdk.ui.widget.TextWatcherAdapter

/**
 * Text input field for [address][net.geidea.paymentsdk.model.Address] city.
 */
open class CityEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int
) : FormEditText(context, attrs, defStyleAttr) {

    init {
        addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(editable: Editable) {
                validate()
            }
        })
    }
}