package com.codeIn.common.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

object ImageHelper {

    fun getImageSize(context : Context, url : String?): String{
        var result = ""
        Glide
            .with(context)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val byteCount  = resource.allocationByteCount
                    val sizeInKB = byteCount / 1024
                    val sizeInMB = sizeInKB / 1024
                    Log.d("TAG" , "SIZE ,  $sizeInKB")
                    result = "$sizeInKB"
                }
            })

        return result
    }

}