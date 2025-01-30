package com.codeIn.myCash.utilities.pdf_manager

import android.content.Context
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.util.Log
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * class to export a view to a pdf file and save it to the local storage of the device or to share it.
 * @param context the context of the activity or fragment that calls this class are used to get the external storage directory of the device.
 */
class PdfExporter constructor(private val context: Context) {

    // Create a new PdfDocument instance
    private val document = PdfDocument()

    // the page number of the pdf document
    private var pageNumber = 0


    /**
     * function to add a page to the pdf document, it takes a view as a parameter and add it to the pdf document
     * @param view the view that will be added to the pdf document
     * @return unit
     */
    fun addPage(view: View) {
        // increment the page number
        pageNumber++

        // prepare the width and height as a A4-sized image with resolution width x height pixels at 300 ppi (pixels per inch)
        val size = 2.8
        val width = (595 * size).toInt()
        val height = (842 * size).toInt()


        //make the view dimensions as width x height pixels at 300 ppi (pixels per inch)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        view.layout(0, 0, width, height)

        // Obtain the width and height of the view
        val viewWidth = view.measuredWidth
        val viewHeight = view.measuredHeight

        // Create a PageInfo object specifying the page attributes (width and height) and the page number
        val pageInfo: PdfDocument.PageInfo =
            PdfDocument.PageInfo.Builder(viewWidth, viewHeight, pageNumber).create()

        // Start a new page
        val page: PdfDocument.Page = document.startPage(pageInfo)

        // Get the Canvas object to draw on the page
        val canvas: Canvas = page.canvas

        // Draw the view on the canvas
        view.draw(canvas)

        // Finish the page
        document.finishPage(page)

    }

    /**
     * function to build the pdf document and save it to the local storage of the device
     * it builds the pdf document with name "invoice" + current date and time and save it to the external storage of the device
     *
     * @return the file that contains the pdf document
     */
    fun build(): File {

        // Specify the path and filename of the output PDF file.... naming the file with current date and time.
        val currentTime = SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
        val filePath = File(context.getExternalFilesDir(null), "/invoice ${currentTime}.pdf")

        try {
            // Save the document to a file
            val fos = FileOutputStream(filePath)
            document.writeTo(fos)
            fos.close()
            // PDF conversion successful
            Toast.makeText(context, "PDF Created Successfully", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            // Error occurred while converting to PDF
            Log.e("TAG", "convertXmlToPdf: $e")
        }


        return filePath
    }

    /**
     * function to close the pdf document
     * it should be called after calling the build() function if you will not add any pages to the pdf document or build it again
     * @return unit
     */
    fun closeDocument() = document.close()


    fun convertViewToPdf(view: View, saveToLocalStorage: Boolean = false): File {

        val size = 2.8
        val width = (595 * size).toInt()
        val height = (842 * size).toInt()


        //make the view dimensions as a an A4-sized image with resolution width x height pixels at 300 ppi (pixels per inch)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        view.layout(0, 0, width, height)
        Log.e("TAG", "convertViewToPdf:    ${view.measuredWidth}   x   ${view.measuredHeight}")


        // Obtain the width and height of the view
        val viewWidth = view.measuredWidth
        val viewHeight = view.measuredHeight

        // Create a PageInfo object specifying the page attributes
        val pageInfo: PdfDocument.PageInfo =
            PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create()

        // Start a new page
        val page: PdfDocument.Page = document.startPage(pageInfo)

        // Get the Canvas object to draw on the page
        val canvas: Canvas = page.canvas

        // Draw the view on the canvas
        view.draw(canvas)

        // Finish the page
        document.finishPage(page)

        // Specify the path and filename of the output PDF file.... naming the file with current date and time.
        val currentTime = SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
        val filePath = File(context.getExternalFilesDir(null), "/invoice ${currentTime}.pdf")

        try {
            // Save the document to a file
            val fos = FileOutputStream(filePath)
            document.writeTo(fos)
            document.close()
            fos.close()
            // PDF conversion successful
            Toast.makeText(context, "PDF Created Successfully", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            // Error occurred while converting to PDF
            Log.e("TAG", "convertXmlToPdf: $e")
        }


        return filePath
    }
}