package com.codeIn.myCash.utilities.pdf

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeIn.common.printer.PrintUtils.Companion.getBitmapFromView
import com.codeIn.myCash.BuildConfig
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.InvoicePdfBinding
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceModel
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class InvoicePdfManager constructor(
    val context: Context ,
    val invoice : InvoiceModel,
    private val lifecycleOwner: LifecycleOwner ,
    private val layoutInflater: LayoutInflater
){

    private var stringFilePath: String
    var file : File
    private val stringFilePathDownload : String = Environment.getExternalStorageDirectory().path.toString() + "/Download/"
    private val fileDownload = File(stringFilePathDownload)
    private var builder : StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
    private var listPages = ArrayList<Bitmap>()
    private var listInPDF = ArrayList<List<ProductInInvoiceModel>>()
    val binding : InvoicePdfBinding = InvoicePdfBinding.inflate(layoutInflater)

    private var isWorking = MutableLiveData<Boolean>(false)
    private var currentIndex = MutableLiveData<Int>(0)
    private var lastIndex = MutableLiveData<Int>(0)

    init {

        stringFilePath =  binding.root.context.getExternalFilesDir(null)?.absolutePath.toString()
        file = File(stringFilePath)
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        when{
            invoice.products?.size!! <= 4 -> listInPDF.add(invoice.products!!)
            else -> {
                var part1 = invoice.products!!.chunked(4)
                val list = ArrayList<ProductInInvoiceModel>()
                listInPDF.add(part1[0])
                if (invoice.products!!.size > 4)
                {
                    var position  = 0

                    for (l in invoice.products!!)
                    {
                        if (position > 3)
                        {
                            list.add(l)
                        }
                        position += 1
                    }
                    part1 = list.chunked(11)
                    for (p in part1)
                    {
                        listInPDF.add(p)
                    }

                    lastIndex.value = listInPDF.size -1
                }
            }
        }

        this.bindHeader()
    }

    fun download(invoice : String , case : Int , width : Int , height : Int){
        val pdfDocument : PdfDocument = PdfDocument()

        if (listInPDF.size == 1)
        {
            val temp = MutableLiveData<List<ProductInInvoiceModel>>()
            temp.value = listInPDF[0]
            binding.productsInPDf.layoutManager = LinearLayoutManager(context)
            val adapter = ProductInPDFAdapter(context, false , true )
            adapter.submitList(temp.value)
            binding.productsInPDf.adapter = adapter

            val bitmapScale : Bitmap = getBitmapFromView(binding.containerPDf )!!
            listPages.add(bitmapScale)

            val pageInfo : PdfDocument.PageInfo = PdfDocument.PageInfo.Builder( listPages[0].width ,  listPages[0].height , 1).create()
            val page : PdfDocument.Page = pdfDocument.startPage(pageInfo)
            val canvas : Canvas = page.canvas
            val paint : Paint = Paint()
            paint.isFilterBitmap = true;
            canvas.drawPaint(paint)
            listPages[0] = Bitmap.createScaledBitmap( listPages[0] ,  listPages[0].width ,  listPages[0].height , true)
            canvas.drawBitmap( listPages[0] , 0f , 0f , null)
            pdfDocument.finishPage(page)


            if (case == 0)
            {

                if (!fileDownload.exists())
                    fileDownload.mkdir()
                val newFile = File(fileDownload , invoice)
                try {
                    val fileOutputStream : FileOutputStream = FileOutputStream(newFile)
                    pdfDocument.writeTo(fileOutputStream)
//                        CustomToaster.show(
//                            context,
//                            context.getString(R.string.success_message),
//                            isSuccess = true
//                        )
                }catch (e : IOException)
                {
                    Log.d("ERROR" , e.message.toString())
                }

                pdfDocument.close()
            }
            else if (case == 1)
            {

                if (!file.exists())
                    file.mkdir()
                val newFile = File(file , invoice)
                try {
                    val fileOutputStream : FileOutputStream = FileOutputStream(newFile)
                    pdfDocument.writeTo(fileOutputStream)
                }catch (e : IOException)
                {
                    Log.d("ERROR" , e.message.toString())
                }

                pdfDocument.close()
                if (newFile.exists())
                {

                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.putExtra(Intent.EXTRA_STREAM,  uriFromFile(binding.root.context,File(binding.root.context.getExternalFilesDir(null)?.absolutePath.toString(), "$invoice")))
                    shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    shareIntent.type = "application/pdf"
                    context.startActivity(Intent.createChooser(shareIntent, "share.."))
                }
            }
        }
        else
        {
            createBitmaps(invoice , case)
        }


        //add the pdf layout and observe changes
    }

    private fun createBitmaps(invoice : String , case : Int ){
        val pdfDocument : PdfDocument = PdfDocument()
        isWorking.observe(lifecycleOwner , Observer {
            if (!it && (currentIndex.value != lastIndex.value!!+1))
            {

                isWorking.value = true
                if (currentIndex.value != lastIndex.value )
                {
                    if (currentIndex.value == 0 )
                    {

                        binding.clientInfo.visibility = View.VISIBLE
                        binding.headerProduct.visibility = View.VISIBLE
                        binding.headerPDF.visibility = View.VISIBLE
                        binding.lineHeader.visibility = View.VISIBLE
                        binding.footerPDF.visibility = View.VISIBLE
                        binding.productLine.visibility = View.GONE
                        binding.lineSummaryInvoice.visibility = View.VISIBLE

                    }
                    else
                    {
                        binding.clientInfo.visibility = View.GONE
                        binding.headerProduct.visibility = View.VISIBLE
                        binding.headerPDF.visibility = View.GONE
                        binding.lineHeader.visibility = View.GONE
                        binding.footerPDF.visibility = View.VISIBLE
                        binding.lineSummaryInvoice.visibility = View.GONE
                        binding.productLine.visibility = View.VISIBLE
                    }
                }
                else
                {
                    binding.clientInfo.visibility = View.GONE
                    binding.headerProduct.visibility = View.VISIBLE
                    binding.headerPDF.visibility = View.GONE
                    binding.lineHeader.visibility = View.GONE
                    binding.footerPDF.visibility = View.VISIBLE
                    binding.productLine.visibility = View.VISIBLE
                    binding.lineSummaryInvoice.visibility = View.VISIBLE
                }

                val temp = MutableLiveData<List<ProductInInvoiceModel>>()
                temp.value = listInPDF.get(currentIndex.value!!)
                binding.productsInPDf.layoutManager = LinearLayoutManager(binding.root.context)
                val adapterInPDf = ProductInPDFAdapter( context , false ,true)
                adapterInPDf.submitList(temp.value)
                binding.productsInPDf.adapter = adapterInPDf

                //make the view dimensions as width x height pixels at 300 ppi (pixels per inch)
//                binding.containerPDf.measure(
//                    View.MeasureSpec.makeMeasureSpec(410, View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(730, View.MeasureSpec.EXACTLY)
//                )
//                binding.containerPDf.layout(0, 0, width, height)
//
//                // Obtain the width and height of the view
//                val viewWidth = binding.containerPDf.measuredWidth
//                val viewHeight = binding.containerPDf.measuredHeight

                val bitmapScale : Bitmap = getBitmapFromView(binding.containerPDf)!!
                listPages.add(bitmapScale)

                currentIndex.value =  currentIndex.value!!+ 1
                isWorking.value = false
            }

            else if (!it && (currentIndex.value == lastIndex.value!!+1))
            {
                var index = 0
                var numberPage = 1

                for (i in listPages)
                {


                    val pageInfo : PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(i.width , i.height , numberPage).create()
                    val page : PdfDocument.Page = pdfDocument.startPage(pageInfo)
                    val canvas : Canvas = page.canvas
                    val paint : Paint = Paint()
                    paint.isFilterBitmap = true;
                    canvas.drawPaint(paint)
                    listPages[index] = Bitmap.createScaledBitmap(i , i.width , i.height , true)
                    canvas.drawBitmap(i , 0f , 0f , null)
                    pdfDocument.finishPage(page)
                    index +=1
                    numberPage+=1
                }

                if (case == 0)
                {

                    if (!fileDownload.exists())
                        fileDownload.mkdir()
                    val newFile = File(fileDownload , invoice)
                    try {
                        val fileOutputStream : FileOutputStream = FileOutputStream(newFile)
                        pdfDocument.writeTo(fileOutputStream)
                        CustomToaster.show(
                            context,
                            context.getString(R.string.success_message),
                            isSuccess = true
                        )
                    }catch (e : IOException)
                    {
                        Log.d("ERROR" , e.message.toString())
                    }

                    pdfDocument.close()
                }
                else if (case == 1)
                {

                    if (!file.exists())
                        file.mkdir()
                    val newFile = File(file , invoice)
                    try {
                        val fileOutputStream : FileOutputStream = FileOutputStream(newFile)
                        pdfDocument.writeTo(fileOutputStream)


                    }catch (e : IOException)
                    {
                        Log.d("ERROR" , e.message.toString())
                    }

                    pdfDocument.close()
                    if (newFile.exists())
                    {

                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.putExtra(Intent.EXTRA_STREAM,  uriFromFile(binding.root.context,File(binding.root.context.getExternalFilesDir(null)?.absolutePath.toString(), "$invoice")))
                        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        shareIntent.type = "application/pdf"
                        (binding.root.context as Activity).startActivity(Intent.createChooser(shareIntent, "share.."))
                    }
                }}
        })
    }
    
    private fun uriFromFile(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        } else {
            Uri.fromFile(file)
        }
    }

}