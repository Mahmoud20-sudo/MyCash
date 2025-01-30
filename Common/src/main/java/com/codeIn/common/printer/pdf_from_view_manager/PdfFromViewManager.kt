package com.codeIn.common.printer.pdf_from_view_manager

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class PdfFromViewManager @Inject constructor(private val activity: Activity) : PdfFromViewHandler {


    override fun setInvoiceActions(action: String, applicationId: String, view: View) {
        val pdfUri = getUriFromPdf(applicationId, view)
        val intent = Intent(action)
        when (action) {
            Intent.ACTION_VIEW -> intent.setDataAndType(pdfUri, "application/pdf")
            Intent.ACTION_SEND -> {
                intent.putExtra(Intent.EXTRA_STREAM, pdfUri)
                intent.type = "application/pdf"
            }
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivity(intent)
    }


    private fun getUriFromPdf(applicationId: String, view: View): Uri {
        val file = createPdfFromBitmap(view)
        return FileProvider.getUriForFile(activity, "$applicationId.provider", file)
    }


    private fun createPdfFromBitmap(view: View): File {
        val bitmap = getBitmapFromView(view)
        val document = PdfDocument()
        val pageInfo = PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)
        val file =
            File(activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "invoice.pdf")
        FileOutputStream(file).use { fos -> document.writeTo(fos) }
        document.close()
        return file
    }


    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
}