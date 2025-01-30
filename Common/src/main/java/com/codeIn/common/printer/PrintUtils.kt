package com.codeIn.common.printer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.io.ByteArrayOutputStream

class PrintUtils {
    companion object {
        fun getBitmapFromView(view: View  ): Bitmap? {
            //Define a bitmap with the same size as the view
            Log.d("TAG" , "hello in pdf ${view.width}")
            Log.d("TAG" , "hello in pdf ${view.height}")
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val bgDrawable = view.background
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            }
            else {
                canvas.drawColor(Color.WHITE)
            }
            view.draw(canvas)
            return bitmap
        }


        fun getBitmapFromViewPDF(view: View): Bitmap? {

            if (view.measuredHeight <= 0) {
                view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val b = Bitmap.createBitmap(
                    view.measuredWidth,
                    view.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )
                val c = Canvas(b)
                view.layout(0, 0, view.measuredWidth, view.measuredHeight)
                view.draw(c)
                return b
            }
            else
            {
                //Define a bitmap with the same size as the view
                val returnedBitmap = Bitmap.createBitmap(view.layoutParams.width, view.layoutParams.height, Bitmap.Config.ARGB_8888)
                //Bind a canvas to it
                val canvas = Canvas(returnedBitmap)
                view.draw(canvas)
                //return the bitmap
                return returnedBitmap
            }

        }

        fun getUriFromBitmap(inContext: Context, inImage: Bitmap): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
            return Uri.parse(path)
        }


    }


}