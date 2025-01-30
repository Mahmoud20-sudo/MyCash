package com.codeIn.myCash.utilities.pdf_manager

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.codeIn.myCash.databinding.PdfInvoiceInformationBinding
import com.codeIn.myCash.databinding.PdfInvoiceSummaryBinding
import com.codeIn.myCash.databinding.PdfProductDetailsBinding
import com.codeIn.myCash.databinding.PdfSellerInformationBinding
import com.codeIn.myCash.databinding.PdfTaxBillBinding
import com.codeIn.myCash.ui.home.products.products.Product
import com.codeIn.common.util.toTwoDigits
import java.io.File

/**
 * class to manage the pdf file creation and binding the vendorsData to the pdf file
 * it takes a context and a vendorsData as a parameter and bind the vendorsData as pages to the pdf file.
 * @param context the context of the activity or fragment that calls this class are used to get the external storage directory of the device.
 * @param data the vendorsData that will be bind to the pdf file
 */
class PdfManager constructor(context: Context, private val data: PdfData) {

    // the binding object of the pdf file
    private val binding: PdfTaxBillBinding

    // the pdf exporter object that will be used to export the pdf file
    private val pdfExporter: PdfExporter = PdfExporter(context)

    // the list of the sub lists of the products list that will determine the number of pages of the pdf file.
    private val subLists: ArrayList<MutableList<Product>> = arrayListOf()

    // a boolean to determine if the pdf file is ready to be exported or not,
    // it used to prevent the pdf file from being exported before the vendorsData is bind to it. because the vendorsData is bind to the pdf file asynchronously(Glide).
    private var isReady: Boolean = false

    //initiate the binding object.
    // and load the qr code image. in the init block. to save the time of loading the image when binding the vendorsData to the pdf file.
    init {

        // Inflate the XML layout file
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = PdfTaxBillBinding.inflate(inflater)

        //load the qr code image.
//        Glide.with(context)
//            .load(vendorsData.qrCode)
//            .into(binding.invoiceSummary.qrCode2ImageView)

        //load the qr code image. with glide listener to make sure that the image is loaded before binding the vendorsData to the pdf file.
        //and make the isReady boolean true.
        Glide.with(context)
            .load(data.qrCode)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                //glide has loaded the image successfully.
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    //bind the qr code image to the pdf file.
                    binding.qrCode1ImageView.setImageDrawable(resource)
                    bindData()
                    isReady = true
                    return true
                }

            })
            .into(binding.qrCode1ImageView)


    }


    /**
     * function to bind the vendorsData to the pdf file and add the pages to the pdf file.
     * @return unit
     */
    private fun bindData() {


        //bind first page vendorsData to the pdf file
        binding.invoiceInformation.bind(data.invoiceInformation)
//        binding.sellerInformation.bind(vendorsData.sellerInformation)

        //split products list into smaller lists, 4 items for the first page and 12 for the rest of the pages.

        //get the last index of the first page.
        var lastIndex =
            if (data.productDetailsTotal?.details?.size!! >= 4) 4 else data.productDetailsTotal?.details?.size!!
        //add the first page to the sub lists.
//        data.productDetailsTotal?.details?.subList(0, lastIndex)?.let { subLists.add(it) }

        //if there is more than 4 items in the products list, split the rest of the list into smaller lists of 12 items.
        for (i in 4..<data.productDetailsTotal?.details?.size!! step 12) {
            lastIndex =
                if (data.productDetailsTotal?.details?.size!! >= i + 12) i + 12 else data.productDetailsTotal?.details?.size!!
//
//            data.productDetailsTotal?.details?.subList(i, lastIndex)?.let { subLists.add(it) }
        }


        // bind vendorsData for every page and add it to the pdf file.
        for (i in 0..<subLists.size) {
            //reset the total of every page.
            var unitPriceTotal = 0.0
            var quantityTotal = 0
            var discountTotal = 0.0
            var totalBeforeVatTotal = 0.0
            var vatTotal = 0.0
            var vatAmountTotal = 0.0
            var totalIncludingVatTotal = 0.0

            //calculate the total of every page.
            for (item in subLists[i]) {
                unitPriceTotal += item.price?.toDouble() ?: 0.0
                quantityTotal += item.count
                discountTotal += item.discount?.toDouble() ?: 0.0
                totalBeforeVatTotal += item.priceAfterDiscount?.toDouble() ?: 0.0
                vatTotal += item.tax?.toDouble() ?: 0.0
                vatAmountTotal += item.tax?.toDouble() ?: 0.0
                totalIncludingVatTotal += item.finalPriceAfterDiscount?.toDouble() ?: 0.0
            }

            //bind the total of every page to the pdf file.
            val pdfProductDetailsTotal = PdfProductDetailsTotal(
                unitPriceTotal = unitPriceTotal.toTwoDigits().toString(),
                quantityTotal = quantityTotal.toString(),
                discountTotal = discountTotal.toTwoDigits().toString(),
                totalBeforeVatTotal = totalBeforeVatTotal.toTwoDigits().toString(),
                vatTotal = vatTotal.toTwoDigits().toString(),
                vatAmountTotal = vatAmountTotal.toTwoDigits().toString(),
                totalIncludingVatTotal = totalIncludingVatTotal.toTwoDigits().toString(),
//                details = List(subLists[i])
            )

            //bind the summary of every page to the pdf file.
            val invoiceSummary = PdfInvoiceSummary(
                totalAmountBeforeVat = totalBeforeVatTotal.toTwoDigits().toString(),
                totalVat = vatTotal.toTwoDigits().toString(),
                totalAmountIncludingVat = totalIncludingVatTotal.toTwoDigits().toString()
            )

            //if it is the first page bind the invoice information and the seller information to the pdf file.
            //else hide the invoice information and the seller information.
//            if (i == 0) {
//                binding.invoiceInformation.root.visible()
//                binding.sellerInformation.root.visible()
//            } else {
//                binding.invoiceInformation.root.gone()
//                binding.sellerInformation.root.gone()
//            }

            //bind the vendorsData to the pdf file.
//            binding.productDetails.bind(pdfProductDetailsTotal)
//            binding.invoiceSummary.bind(invoiceSummary)

            //add the page to the pdf file.
            pdfExporter.addPage(binding.root)
        }
    }

    /**
     * function to get the pdf file. it is called after the vendorsData is bind to the pdf file.
     * @return the pdf file if it is ready, else return null.
     */
    fun getPdf(): File? {
        return if (isReady)
            pdfExporter.build()
        else null
    }

    /**
     * function to close the pdf document.
     * it should be called after calling the build() function if you will not add any pages to the pdf document or build it again
     * @return unit
     */
    fun onDestroy() = pdfExporter.closeDocument()

    /**
     * function to bind summary vendorsData to the pdf file.
     * @param summary the summary vendorsData that will be bind to the pdf file.
     * @return unit
     */
    private fun PdfInvoiceSummaryBinding.bind(summary: PdfInvoiceSummary?) {
        totalAmountBeforeVatTextView.text = summary?.totalAmountBeforeVat
        totalVatTextView.text = summary?.totalVat
        totalAmountIncludingVatTextView.text = summary?.totalAmountIncludingVat
    }

    /**
     * function to bind product details vendorsData to the pdf file.
     * @param details the product details vendorsData that will be bind to the pdf file.
     * @return unit
     */
    private fun PdfProductDetailsBinding.bind(details: PdfProductDetailsTotal?) {
        val adapter = PdfProductDetailsAdapter()
//        adapter.submitList(details?.details)
        productDetailsRecycleView.adapter = adapter

        unitPriceTotalTextView.text = details?.unitPriceTotal
        quantityTotalTextView.text = details?.quantityTotal
        discountTotalTextView.text = details?.discountTotal
        totalBeforeVatTotalTextView.text = details?.totalBeforeVatTotal
        vatTotalTextView.text = details?.vatTotal
        vatAmountTotalTextView.text = details?.vatAmountTotal
        totalIncludingVatTotalTextView.text = details?.totalIncludingVatTotal
    }

    /**
     * function to bind seller information vendorsData to the pdf file.
     * @param info the seller information vendorsData that will be bind to the pdf file.
     * @return unit
     */
    private fun PdfSellerInformationBinding.bind(info: PdfSellerInformation?) {
        sellerNameEnTextView.text = info?.sellerNameEn
        sellerNameArTextView.text = info?.sellerNameAr

        sellerAddressEnTextView.text = info?.sellerAddressEn
        sellerAddressArTextView.text = info?.sellerAddressAr

        sellerVatNoEnTextView.text = info?.sellerVatNoEn
        sellerVatNoArTextView.text = info?.sellerVatNoAr

        crNoEnTextView.text = info?.crNoEn
        crNoArTextView.text = info?.crNoAr
    }

    /**
     * function to bind invoice information vendorsData to the pdf file.
     * @param info the invoice information vendorsData that will be bind to the pdf file.
     * @return unit
     */
    private fun PdfInvoiceInformationBinding.bind(info: PdfInvoiceInformation?) {
        invoiceNoTextView.text = info?.invoiceNo
        dateTimeTextView.text = info?.dateTime
        invoiceTypeTextView.text = info?.invoiceType
    }


}