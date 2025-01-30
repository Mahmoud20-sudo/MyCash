package com.codeIn.myCash.features.stock.data.invoice.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.InvoiceType
import com.codeIn.common.data.MainTypeInvoice
import com.codeIn.common.data.SaleBuyType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.stock.data.invoice.remote.Invoice
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInInvoiceRequest
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductInReturnInvoice
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCartInQuickInvoice
import com.codeIn.myCash.features.stock.domain.invoice.repository.InvoiceRepository
import com.codeIn.myCash.features.stock.domain.invoice.usecases.MakeInvoiceValidationUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.MakePurchaseInvoiceValidationUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.CreateProductValidationUseCase
import com.google.gson.Gson
import javax.inject.Inject

class InvoiceRepositoryImpl @Inject constructor(
    private val invoice: Invoice,
    private val prefs : SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl,
    private val makeInvoiceValidationUseCase: MakeInvoiceValidationUseCase,
    private val makePurchaseInvoiceValidationUseCase: MakePurchaseInvoiceValidationUseCase,
    private val createProductValidationUseCase : CreateProductValidationUseCase,

    ) : InvoiceRepository {
    override suspend fun makeInvoice(
        cashPrice: String?,
        visaPrice: String?,
        paymentType: Int,
        products: ProductsInCart?,
        nextData: String?,
    ): InvoiceState {
        return try {
            makeInvoiceValidationUseCase.invoke(cashPrice , visaPrice , paymentType , nextData)
                .let {
                    if (it != InvalidInput.NONE) return InvoiceState.InputError(it)
                }
            if (products?.mainTypeInvoice == MainTypeInvoice.PURCHASE){
                makePurchaseInvoiceValidationUseCase.invoke(products.purchaseInvoiceModel?.referenceNumber , products.purchaseInvoiceModel?.date )
                    .let {
                        if (it != InvalidInput.NONE) return InvoiceState.InputError(it)
                    }
            }
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val branchId = prefs.getValue(Constants.branchIdStore())

            val visaValue = if (visaPrice == "0.0")
                null
            else
                visaPrice
            invoice.makeInvoice(
                lang = lang ,
                authorization = token ,
                products =  getProductParameterInInvoice(products),
                clientId = products?.clientId ,
                cashPrice =  cashPrice ,
                visaPrice =  visaValue ,
                paymentType = paymentType.toString() ,
                referenceDatePurchaseInvoice = products?.purchaseInvoiceModel?.date ,
                referenceNumberPurchaseInvoice = products?.purchaseInvoiceModel?.referenceNumber ,
                notePurchaseInvoice = products?.purchaseInvoiceModel?.note ,
                nextData = nextData ,
                invoiceType = products?.invoiceType?.value?:InvoiceType.SIMPLE.value,
                invoiceIdRefundInvoice = null ,
                saleOrBuy = products?.mainTypeInvoice?.value?:MainTypeInvoice.SALE.value,
                branchId = branchId
            ).let { response ->
                Log.d("TAG Invoice" , "invoice data $response")
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        InvoiceState.SuccessShowSingleInvoice(response.body()?.invoiceModel)
                    } else {
                        InvoiceState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceState.UnAuthorized
                } else {
                    InvoiceState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            InvoiceState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun makeQuickInvoice(
        cashPrice: String?,
        visaPrice: String?,
        paymentType: Int,
        products: ProductsInCartInQuickInvoice?,
        nextData: String?,
        product : ProductInQuickInvoice? ,
        runRefundInvoice : String?,
        codeRefundInvoice : String?,
        dateRefundInvoice : String?,
    ): InvoiceState {
        return try {
            if (product != null )
                createProductValidationUseCase.invoke(product.name , product.price , product.quantity, buyPrice = "0.0").let {
                    if (it != InvalidInput.NONE) return InvoiceState.InputError(it)
                }
            else {
                if (products?.list.isNullOrEmpty()){
                    return InvoiceState.InputError(InvalidInput.EMPTY)
                }
            }
            makeInvoiceValidationUseCase.invoke(cashPrice , visaPrice , paymentType , nextData)
                .let {
                    if (it != InvalidInput.NONE) return InvoiceState.InputError(it)
                }

            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val branchId = prefs.getValue(Constants.branchIdStore())

            invoice.makeQuickInvoice(
                lang = lang ,
                authorization = token ,
                products =  getProductParameterInQuickInvoice(products),
                clientId = products?.clientId ,
                cashPrice =  cashPrice ,
                visaPrice =  visaPrice ,
                paymentType = paymentType.toString() ,
                nextData = nextData ,
                invoiceType = products?.invoiceType?.value?:InvoiceType.SIMPLE.value,
                codeRefundInvoice = codeRefundInvoice,
                runRefundInvoice = runRefundInvoice,
                dateRefundInvoice = dateRefundInvoice,
                invoiceIdRefundInvoice = null ,
                saleOrBuy = products?.saleOrBuy?.value ?: SaleBuyType.SALE.value,
                branchId = branchId
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        InvoiceState.SuccessShowSingleInvoice(response.body()?.invoiceModel)
                    } else {
                        InvoiceState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceState.UnAuthorized
                } else {
                    InvoiceState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            InvoiceState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getInvoices(
        type: String?,
        isReturn: String?,
        clientId: String?,
        invoiceType: String?,
        saleOrBuy : String?,
        paymentStatus: String?,
        limit : String? ,
        date : String?
    ): InvoiceState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            invoice.getInvoices(
                lang= lang ,
                authorization = token ,
                clientId = clientId ,
                isReturn = isReturn ,
                paymentStatus = paymentStatus ,
                invoiceType = invoiceType ,
                saleOrBuy = saleOrBuy,
                type = type ,
                limit = limit ,
                date = date
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        InvoiceState.SuccessShowInvoices(response.body()?.data)
                    } else {
                        InvoiceState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceState.UnAuthorized
                } else {
                    InvoiceState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            InvoiceState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getSingleInvoice(invoiceId: String?): InvoiceState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            invoice.getSingleInvoice(
                lang=lang ,
                authorization = token ,
                invoiceId= invoiceId
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        InvoiceState.SuccessShowSingleInvoice(response.body()?.invoiceModel)
                    } else {
                        InvoiceState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceState.UnAuthorized
                } else {
                    InvoiceState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            InvoiceState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getSingleInvoiceWithNumber(invoiceNumber: String?): InvoiceState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            invoice.getInvoiceByNumberInvoice(
                lang=lang ,
                authorization = token ,
                invoiceNumber= invoiceNumber
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        InvoiceState.SuccessShowSingleInvoice(response.body()?.invoiceModel)
                    } else {
                        InvoiceState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceState.UnAuthorized
                } else {
                    InvoiceState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            InvoiceState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getProductParameterInInvoice(products: ProductsInCart?): String? {
        val list = ArrayList<ProductInInvoiceRequest>()
        if (products?.list?.isNotEmpty() == true){
            products.list?.forEach { product->
                list.add(ProductInInvoiceRequest(product.id , product.count ,
                    product.discountInInvoiceModel?.discountValue?:"0" ,
                    product?.discountInInvoiceModel?.discountType?.value?:0))
            }
        }
        return Gson().toJson(list)
    }

    override suspend fun getProductParameterInQuickInvoice(products: ProductsInCartInQuickInvoice? ): String? {
        val list = ArrayList<ProductInQuickInvoice>()
        if (products?.list?.isNotEmpty() == true){
            products.list?.forEach { product->
                list.add(ProductInQuickInvoice(product.name , product.quantity ,
                    product.price?:"0" ,
                    product.taxAvailable?:"0" ,
                    product.taxType?:"0"
                    ))
            }
        }
        return Gson().toJson(list)
    }

    override suspend fun getProductParameterInReturnInvoice(products: ProductInReturnInvoice?): String? {
        val list = ArrayList<ProductInInvoiceRequest>()
        if (products?.list?.isNotEmpty() == true){
            products.list?.forEach { product->
                list.add(ProductInInvoiceRequest(product.product?.id?:0 , product.count, product.invoiceDiscountValue?:"0" ,
                        product.invoiceDiscountType?.toDouble()?.toInt()?:0))
            }
            if (list.isEmpty()){
                list.add(ProductInInvoiceRequest(product_id = products.list?.get(0)?.product?.id!!, 0 , "0", 0 ))
            }
        }
        return Gson().toJson(list)
    }

    override suspend fun refundInvoice(
        cashPrice: String?,
        visaPrice: String?,
        paymentType: Int,
        products: ProductInReturnInvoice?,
        clientId : String? ,
        invoiceId : String? ,
        invoiceType : Int ,
        codeRefundInvoice : String? ,
        runRefundInvoice : String? ,
        dateRefundInvoice : String? ,
        mainTypeInvoice: Int
    ): InvoiceState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            val branchId = prefs.getValue(Constants.branchIdStore())

            val visaValue = if (visaPrice == "0.0")
                null
            else
                visaPrice
            invoice.makeInvoice(
                lang = lang ,
                authorization = token ,
                products = getProductParameterInReturnInvoice(products),
                clientId = clientId ,
                cashPrice = cashPrice ,
                visaPrice =  visaValue ,
                paymentType = paymentType.toString() ,
                referenceDatePurchaseInvoice = null  ,
                referenceNumberPurchaseInvoice = null ,
                notePurchaseInvoice = null ,
                nextData = null ,
                invoiceType = invoiceType,
                invoiceIdRefundInvoice = invoiceId.toString() ,
                saleOrBuy = mainTypeInvoice?:MainTypeInvoice.SALE.value,
                branchId = branchId
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        InvoiceState.SuccessShowSingleInvoice(response.body()?.invoiceModel)
                    } else {
                        InvoiceState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceState.UnAuthorized
                } else {
                    InvoiceState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            InvoiceState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun confirmVisa(invoiceId : String?,
                                     runRefundInvoice : String?,
                                     codeRefundInvoice : String?,
                                     dateRefundInvoice : String?,): InvoiceState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            invoice.confirmVisaPayment(
                lang = lang ,
                authorization = token ,
                invoiceId = invoiceId,
                runRefundInvoice = runRefundInvoice ,
                codeRefundInvoice = codeRefundInvoice ,
                dateRefundInvoice = dateRefundInvoice
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        InvoiceState.SuccessConfirmVisa(response.body()?.invoiceModel)
                    } else {
                        InvoiceState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    InvoiceState.UnAuthorized
                } else {
                    InvoiceState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            InvoiceState.ServerError(errorHandler.getError(throwable))
        }
    }
}