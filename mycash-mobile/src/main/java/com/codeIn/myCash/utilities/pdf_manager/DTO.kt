package com.codeIn.myCash.utilities.pdf_manager

import com.codeIn.myCash.ui.home.products.products.Product
import com.codeIn.myCash.features.stock.data.invoice.remote.response.ProductInInvoiceModel
import com.google.gson.annotations.SerializedName


data class PdfData(

    @SerializedName("PdfInvoiceInformation"     ) var invoiceInformation    : PdfInvoiceInformation?      = null,
    @SerializedName("PdfSellerInformation"      ) var sellerInformation     : PdfSellerInformation?       = null,
    @SerializedName("PdfProductDetailsTotal"    ) var productDetailsTotal   : PdfProductDetailsTotal?     = null,
    @SerializedName("PdfInvoiceSummary"         ) var invoiceSummary        : PdfInvoiceSummary?          = null,
    @SerializedName("qr_code"                   ) var qrCode                : String?                     = null

    )

data class PdfInvoiceInformation (

    @SerializedName("invoice_no"     ) var invoiceNo      : String?           = null,
    @SerializedName("date_time"      ) var dateTime       : String?           = null,
    @SerializedName("invoice_type"   ) var invoiceType    : String?           = null

)

data class PdfSellerInformation (

    @SerializedName("seller_name_en"     ) var sellerNameEn     : String?           = null,
    @SerializedName("seller_name_ar"     ) var sellerNameAr     : String?           = null,
    @SerializedName("seller_address_en"  ) var sellerAddressEn  : String?           = null,
    @SerializedName("seller_address_ar"  ) var sellerAddressAr  : String?           = null,
    @SerializedName("seller_vat_no_en"   ) var sellerVatNoEn    : String?           = null,
    @SerializedName("seller_vat_no_ar"   ) var sellerVatNoAr    : String?           = null,
    @SerializedName("cr_no_en"           ) var crNoEn           : String?           = null,
    @SerializedName("cr_no_ar"           ) var crNoAr           : String?           = null

)

data class PdfProductDetails (

    @SerializedName("description"         ) var description        : String?           = null,
    @SerializedName("unit_price"          ) var unitPrice          : String?           = "0.0",
    @SerializedName("quantity"            ) var quantity           : String?           = "0.0",
    @SerializedName("discount"            ) var discount           : String?           = "0.0",
    @SerializedName("total_before_vat"    ) var totalBeforeVat     : String?           = "0.0",
    @SerializedName("vat"                 ) var vat                : String?           = "0.0",
    @SerializedName("vat_amount"          ) var vatAmount          : String?           = "0.0",
    @SerializedName("total_including_vat" ) var totalIncludingVat  : String?           = "0.0"

)

data class PdfProductDetailsTotal(

    @SerializedName("unit_price_total"          ) var unitPriceTotal: String?           = null,
    @SerializedName("quantity_total"            ) var quantityTotal: String?           = null,
    @SerializedName("discount_total"            ) var discountTotal: String?           = null,
    @SerializedName("total_before_vat_total"    ) var totalBeforeVatTotal: String?           = null,
    @SerializedName("vat_total"                 ) var vatTotal: String?           = null,
    @SerializedName("vat_amount_total"          ) var vatAmountTotal: String?           = null,
    @SerializedName("total_including_vat_total" ) var totalIncludingVatTotal: String?           = null,
    @SerializedName("details"                   ) var details: List<ProductInInvoiceModel> = arrayListOf()

)


data class PdfInvoiceSummary (

    @SerializedName("total_amount_before_vat"     ) var totalAmountBeforeVat    : String?           = null,
    @SerializedName("total_vat"                   ) var totalVat                : String?           = null,
    @SerializedName("total_amount_including_vat"  ) var totalAmountIncludingVat : String?           = null,
    @SerializedName("qr_code"                     ) var qrCode                  : String?           = null

)

