package com.codeIn.myCash.ui.home.invoices.invoice

import android.content.Context
import android.graphics.Bitmap
import android.os.RemoteException
import com.sunmi.peripheral.printer.InnerPrinterCallback
import com.sunmi.peripheral.printer.InnerPrinterManager
import com.sunmi.peripheral.printer.InnerResultCallback
import com.sunmi.peripheral.printer.SunmiPrinterService
import timber.log.Timber

object SunMiPrinter {

    private const val ALIGNMENT_CENTER = 1

    /**
     * Starts the Sun-mi printer with a bitmap.
     *
     * @param context The application context.
     * @param bitmap The bitmap to be printed.
     */
    fun start(context: Context, bitmap: Bitmap?) {
        bitmap ?: return logError("Bitmap is null")

        try {
            val innerPrinterCallback = createInnerPrinterCallback(bitmap)
            val isServiceBound = InnerPrinterManager.getInstance().bindService(context, innerPrinterCallback)
            Timber.d("bindService: $isServiceBound")
        } catch (ex: Exception) {
            logError("Exception during printer start: ${ex.message}")
            ex.printStackTrace()
        }
    }

    /**
     * Creates an InnerPrinterCallback to manage the connection and printing process.
     *
     * @param bitmap The bitmap to be printed.
     * @return The InnerPrinterCallback implementation.
     */
    private fun createInnerPrinterCallback(bitmap: Bitmap): InnerPrinterCallback {
        return object : InnerPrinterCallback() {
            override fun onConnected(sunmiPrinterService: SunmiPrinterService) {
                try {
                    val callback = createResultCallback()
                    sunmiPrinterService.setAlignment(ALIGNMENT_CENTER, callback)
                    sunmiPrinterService.printBitmap(bitmap, callback)
                } catch (e: RemoteException) {
                    logError("RemoteException while printing: ${e.message}")
                    e.printStackTrace()
                }
            }

            override fun onDisconnected() {
                logError("Printer disconnected.")
            }
        }
    }

    /**
     * Creates a result callback to handle printer responses.
     *
     * @return The InnerResultCallback implementation.
     */
    private fun createResultCallback(): InnerResultCallback {
        return object : InnerResultCallback() {
            override fun onRunResult(isSuccess: Boolean) {
                Timber.d("onRunResult: $isSuccess")
            }

            override fun onReturnString(result: String) {
                Timber.d("onReturnString: $result")
            }

            override fun onRaiseException(code: Int, msg: String) {
                logError("Error code: $code, message: $msg")
            }

            override fun onPrintResult(code: Int, msg: String) {
                Timber.d("onPrintResult: Code $code, message $msg")
            }
        }
    }

    /**
     * Logs an error message using Timber.
     *
     * @param message The error message to log.
     */
    private fun logError(message: String) {
        Timber.e(message)
    }
}
