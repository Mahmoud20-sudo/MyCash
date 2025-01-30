package com.codeIn.myCash.utilities.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.codeIn.common.data.Limit
import com.codeIn.common.util.gone
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.LayoutTextInputBinding
import com.codeIn.myCash.ui.resetError
import com.codeIn.myCash.ui.setErrorMsg
import com.codeIn.myCash.ui.setTextWatcher
import com.google.android.material.textfield.TextInputLayout


class CustomTextInputLayout : ConstraintLayout {

    private lateinit var typedArray: TypedArray


    private val _binding by lazy {
        LayoutTextInputBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextInputLayout, 0, 0)
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomTextInputLayout, defStyle, 0)
        init(context, attrs)
    }

    init {
        _binding.root
    }

    val binding = _binding

    private var isHintEnabled: Boolean = true
    private var showCounter: Boolean = false
    private var enabled: Boolean = true
    private var isTax: Boolean = false

    private var startIconDrawable: Drawable? = null
    private var endIconDrawable: Drawable? = null
    private var hintText: String? = null
    private var textColor: Int? = null
    private var textSize: Int? = null


    private var inputType: Int = 0
    private var maxLength: Int = 0
    private var digits: String? = null
    private var autofillHints: String? = null

    private var errorMessage: String? = null
    private var endIconMode: String? = null

    val text get() = binding.editText.text.toString()

    fun clearText() = binding.editText.setText("")

    fun init(context: Context?, attrs: AttributeSet?) = try {
        isHintEnabled =
            typedArray.getBoolean(R.styleable.CustomTextInputLayout_myHintEnabled, false)

        showCounter =
            typedArray.getBoolean(R.styleable.CustomTextInputLayout_showCounter, false)

        isTax = typedArray.getBoolean(R.styleable.CustomTextInputLayout_isTax, false)

        endIconDrawable =
            typedArray.getDrawable(R.styleable.CustomTextInputLayout_myEndIconDrawable)

        startIconDrawable =
            typedArray.getDrawable(R.styleable.CustomTextInputLayout_myStartIconDrawable)

        hintText =
            typedArray.getString(R.styleable.CustomTextInputLayout_myHint)
        //EDIT_TEXT
        inputType = typedArray.getInt(
            R.styleable.CustomTextInputLayout_android_inputType,
            InputType.TYPE_CLASS_TEXT
        )
        errorMessage = typedArray.getString(R.styleable.CustomTextInputLayout_myError)

        maxLength = typedArray.getInt(
            R.styleable.CustomTextInputLayout_android_maxLength,
            Limit.HUNDRED.value
        )

        inputType = typedArray.getInt(
            R.styleable.CustomTextInputLayout_android_inputType,
            InputType.TYPE_CLASS_TEXT
        )

        digits = typedArray.getString(R.styleable.CustomTextInputLayout_android_digits)

        textColor = typedArray.getInt(
            R.styleable.CustomTextInputLayout_android_textColor,
            R.color.mainBlack
        )

        textSize =
            typedArray.getDimensionPixelSize(R.styleable.CustomTextInputLayout_android_textSize, 0)

        enabled = typedArray.getBoolean(R.styleable.CustomTextInputLayout_android_enabled, true)

        autofillHints =
            typedArray.getString(R.styleable.CustomTextInputLayout_android_autofillHints)

    } catch (e: Exception) {
        typedArray.recycle()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFinishInflate() {
        super.onFinishInflate()

        binding.apply {
            textInputLayout.hint = hintText
            textInputLayout.isHintEnabled = isHintEnabled
            textInputLayout.startIconDrawable = startIconDrawable
            textInputLayout.setEndDrawable(endIconDrawable, endIconMode)

            val showEndDrawable = showCounter || isTax

            autofillHints?.let { editText.setAutofillHints(it) }
            editText.keyListener = digits?.let { DigitsKeyListener.getInstance(it) };

            digits?.let {
                editText.keyListener = DigitsKeyListener.getInstance(it)
                editText.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
            } ?: run { editText.inputType = inputType }

            editText.filters = arrayOf(InputFilter.LengthFilter(maxLength))

            editText.isEnabled = enabled

            textSize?.let { editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.toFloat()) }
            textColor?.let { editText.setTextColor(it) }
            errorMessage?.let { setErrorMsg(it) }

            textInputLayout.editText?.setTextWatcher(
                doOnTextChanged = {
                    binding.textInputLayout.resetError()
                    binding.counterTextview.isVisible = it.isNotEmpty() && showEndDrawable
                    binding.counterTextview.text = if (!isTax) "${it.length}/$maxLength" else "%"
                    textInputLayout.setEndDrawable(
                        if (it.isEmpty()) endIconDrawable else null,
                        endIconMode
                    )
                }
            )
        }
    }

    private fun TextInputLayout.setEndDrawable(drawable: Drawable?, endIconMode: String?) =
        drawable?.let {
            setEndIconMode(if (endIconMode == "password_toggle") TextInputLayout.END_ICON_PASSWORD_TOGGLE else TextInputLayout.END_ICON_CUSTOM)
            setEndIconDrawable(drawable)
        } ?: run {
            setEndIconDrawable(null)
            setEndIconMode(TextInputLayout.END_ICON_NONE)
        }
}