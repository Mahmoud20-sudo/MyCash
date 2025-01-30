package com.codeIn.myCash.utilities

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import com.codeIn.common.printer.MyCashDevices
import com.codeIn.myCash.ui.home.invoices.invoice.SunMiPrinter
import com.codein.common.printer.PrintUtils.Companion.getBitmapFromView
import com.codein.common.printer.marvel.ConnectionWithUsb
import com.pax.api.PrintManager

fun myCashPrint(context: Context, view: View) {
    when (Build.MANUFACTURER) {
        MyCashDevices.SUNMI.name -> {
            SunMiPrinter.start(context, getBitmapFromView(view))
        }

        MyCashDevices.lephone.name -> {
            runCatching {
                val printManager = PrintManager.getInstance(context)
                printManager.prnInit();
                val bitmap = getBitmapFromView(view)
                printManager.prnBitmap(bitmap)
                printManager.prnStep(800)
                printManager.prnStart()
            }.getOrElse {
                Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        else -> {
            val printer = ConnectionWithUsb()
            printer.print(context, view)
        }
    }
}