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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.codeIn.common.data.Limit
import com.codeIn.common.util.gone
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.LayoutRecyclerviewBinding
import com.codeIn.myCash.databinding.LayoutTextInputBinding
import com.codeIn.myCash.ui.resetError
import com.codeIn.myCash.ui.setErrorMsg
import com.codeIn.myCash.ui.setTextWatcher
import com.google.android.material.textfield.TextInputLayout


class CustomRecyclerView : ConstraintLayout {

    private lateinit var typedArray: TypedArray


    private val _binding by lazy {
        LayoutRecyclerviewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRecyclerView, 0, 0)
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomRecyclerView, defStyle, 0)
        init(context, attrs)
    }

    init {
        _binding.root
    }

    val binding = _binding

    private var layoutManager: LayoutManager? = null
    private var spanCount: Int = 2
    private var orientation: Int = 1
    private var overScrollMode : Int = 0

    fun init(context: Context?, attrs: AttributeSet?) = try {

        val manager = typedArray.getString(R.styleable.CustomRecyclerView_layoutManager)
        spanCount = typedArray.getInt(R.styleable.CustomRecyclerView_spanCount, spanCount)
        orientation = typedArray.getInt(R.styleable.CustomRecyclerView_android_orientation, 1)

        val mode = typedArray.getString(R.styleable.CustomRecyclerView_android_overScrollMode)

        overScrollMode = when(mode){
            "never" -> RecyclerView.OVER_SCROLL_NEVER
            "always" -> RecyclerView.OVER_SCROLL_ALWAYS
            else -> RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS
        }


        val recyclerViewOrientation = when (orientation) {
            1 -> LinearLayoutManager(context)
            else -> LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        layoutManager = when (manager) {
            "androidx.recyclerview.widget.LinearLayoutManager" -> recyclerViewOrientation
            else -> GridLayoutManager(context, spanCount)
        }

    } catch (e: Exception) {
        typedArray.recycle()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFinishInflate() {
        super.onFinishInflate()

        binding.apply {
            binding.rv.layoutManager = layoutManager
            binding.rv.overScrollMode = overScrollMode
        }
    }
}