package com.codeIn.myCash.features.stock.data.invoice.remote.response


import android.os.Parcelable
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettings
import com.codeIn.myCash.features.user.data.accountSettings.remote.invoiceSettings.InvoiceSettingsData
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.User
import com.codeIn.myCash.features.user.data.authentication.remote.response.user.UserDTO
import com.codeIn.myCash.features.user.data.shift.remote.reponse.ShiftData
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Locale.filter
import kotlin.math.roundToInt

@Parcelize
data class InvoiceModel(
    @SerializedName("cashPrice")
    val cash: String?,
    @SerializedName("paid_cashir_price")
    val cashPrice: String?,
    @SerializedName("client")
    val client: ClientModel?,
    @SerializedName("codeRefund")
    val codeRefund: String?,
    @SerializedName("dateRefund")
    val dateRefund: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("discountPrice")
    val discountPrice: String?,
    @SerializedName("discountType")
    val discountType: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoiceNumber")
    val invoiceNumber: String?,
    @SerializedName("invoiceOrder")
    val invoiceOrder: String?,
    @SerializedName("invoiceType")
    val invoiceType: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("hasInvoiceNotification")
    val hasInvoiceNotification: String?,
    @SerializedName("isPayVisa")
    val isPayVisa: String?,
    @SerializedName("isReturn")
    val isReturn: String?,
    @SerializedName("nextData")
    val nextData: String?,
    @SerializedName("paymentStatus")
    val paymentStatus: String?,
    @SerializedName("paymentType")
    val paymentType: String?,
    @SerializedName("productPrice")
    val productPrice: String?,
    @SerializedName("products")
    val products: List<ProductInInvoiceModel>?,
    @SerializedName("rammingPrice")
    val rammingPrice: String?,
    @SerializedName("runRefund")
    val runRefund: String?,
    @SerializedName("shift")
    val shift: ShiftData?,
    @SerializedName("tax")
    private val tax: String?,
    @SerializedName("taxPrice")
    val taxPrice: String?,
    @SerializedName("totalPrice")
    val totalPrice: String?,
    @SerializedName("visaPrice")
    val visaPrice: String?,
    @SerializedName("userDate")
    val cashierModel : User?,
    @SerializedName("parent")
    val parentInvoice : InvoiceModel?,
    @SerializedName("returnedAmount")
    val returnedAmount : String?,
    @SerializedName("saleOrBuy")
    val saleOrBuy : String?,
    @SerializedName("zatka")
    val qrZatca : String?
) : Parcelable{
    val _tax : Int? get() = tax?.toFloat()?.roundToInt()
    val creditsProducts : List<ProductInInvoiceModel>? get() = products?.filter { product -> product.hasNotification == null }
}