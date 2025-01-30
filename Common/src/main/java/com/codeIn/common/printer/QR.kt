package com.codeIn.common.printer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object QR {

    fun generateQrCode(data : String, context: Context): Bitmap {
        val size = 512 //pixels
        val bits = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
        return Bitmap.createBitmap(size, size, Bitmap.Config.ALPHA_8).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.alpha(Color.TRANSPARENT))
                }
            }
        }
    }
}