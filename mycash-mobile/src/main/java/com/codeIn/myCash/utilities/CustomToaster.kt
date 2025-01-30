package com.codeIn.myCash.utilities

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.codeIn.myCash.R


object CustomToaster {
    fun show(context: Context ,  message : String, isSuccess: Boolean = true ,textSize: Float = 12f){
        val layout : View = LayoutInflater.from(context).inflate(R.layout.custom_toast , null , false)

        val toastText: TextView = layout.findViewById(R.id.message)
        val toastImage: ImageView = layout.findViewById(R.id.icon)
        val linearLayout: LinearLayout = layout.findViewById(R.id.containerToast)
        val customFont = ResourcesCompat.getFont(context, R.font.bill_bold)

        customFont?.let {
            toastText.typeface = it
        }
        if (isSuccess){
            toastImage.setImageResource(R.drawable.success_icon)
            toastText.setTextColor(context.getColor(R.color.secondaryColor))
            linearLayout.setBackgroundResource(R.drawable.toast_background_success)
        }
        else
        {
            toastImage.setImageResource(R.drawable.error_icon)
            toastText.setTextColor(context.getColor(R.color.red))
            linearLayout.setBackgroundResource(R.drawable.toast_background_error)
        }

        toastText.text = message
        toastText.textSize = textSize
        val toast = Toast(context)
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout

        toast.show()
    }
}