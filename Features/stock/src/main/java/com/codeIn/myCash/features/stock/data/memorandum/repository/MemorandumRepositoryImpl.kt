package com.codeIn.myCash.features.stock.data.memorandum.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.invoice.remote.response.InvoiceState
import com.codeIn.myCash.features.stock.data.memorandum.remote.Memorandum
import com.codeIn.myCash.features.stock.data.memorandum.remote.response.MemorandumState
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInInvoiceMemorandum
import com.codeIn.myCash.features.stock.domain.memorandum.model.ProductInMemorandumInvoiceRequest
import com.codeIn.myCash.features.stock.domain.memorandum.repository.MemorandumRepository
import com.codeIn.myCash.features.stock.domain.memorandum.usecases.MakeMemorandumValidationUseCase
import com.google.gson.Gson
import javax.inject.Inject

class MemorandumRepositoryImpl @Inject constructor(
    private val memorandum: Memorandum ,
    private val prefs : SharedPrefsModule,
    private val errorHandler: ErrorHandlerImpl,
    private val makeMemorandumValidationUseCase: MakeMemorandumValidationUseCase
) : MemorandumRepository{
    override suspend fun makeMemorandum(
        invoiceId: String?,
        type: String?,
        cash: String?,
        visa: String?,
        products: ProductInInvoiceMemorandum? ,
        paymentType : String?
    ): MemorandumState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            memorandum.makeMemorandum(
                lang = lang ,
                authorization = token ,
                products = getProductRequest(products),
                cash = cash ,
                visa = visa ,
                type = type,
                invoiceId = invoiceId ,
                paymentType = paymentType
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        MemorandumState.SuccessShowSingleMemorandum(response.body()?.data)
                    } else {
                        MemorandumState.StateError(response.body()?.message)
                    }
                }    else if (response.code() == 401){
                    MemorandumState.UnAuthorized
                } else {
                    MemorandumState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            MemorandumState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getMemorandums(invoiceId: String?, type: String?
    , limit : String?): MemorandumState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            memorandum.getMemorandums(
                lang = lang ,
                authorization = token ,
                invoiceId = invoiceId ,
                type = type,
                limit = limit
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        MemorandumState.SuccessShowMemorandums(response.body()?.memorandumsData)
                    } else {
                        MemorandumState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    MemorandumState.UnAuthorized
                } else {
                    MemorandumState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            MemorandumState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getSingleMemorandum(memorandumId: String?): MemorandumState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            memorandum.getSingleMemorandum(
                lang = lang ,
                authorization = token ,
                memorandumId = memorandumId
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        MemorandumState.SuccessShowSingleMemorandum(response.body()?.data)
                    } else {
                        MemorandumState.StateError(response.body()?.message)
                    }
                }  else if (response.code() == 401){
                    MemorandumState.UnAuthorized
                } else {
                    MemorandumState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        }
        catch (throwable: Throwable) {
            MemorandumState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getProductRequest(products: ProductInInvoiceMemorandum?): String? {
        val list = ArrayList<ProductInMemorandumInvoiceRequest>()
        if (products?.list?.isNotEmpty() == true){
            products.list?.forEach { product->
                if (product.count > 0){
                    list.add(
                        ProductInMemorandumInvoiceRequest(product.id , product.count ,
                            product.difPrice?:"0" )
                    )
                }
            }
        }
        Log.d("TAG" , "Check Memornadum ${Gson().toJson(list)}")
        return Gson().toJson(list)
    }

    override suspend fun validateItemInProducts(products: ProductInInvoiceMemorandum?): MemorandumState {
        if (products?.list?.isNotEmpty() == true){
            products.list?.forEach {product->
                makeMemorandumValidationUseCase.invoke(product.difPrice , product.count.toString()).let {
                    if (it != InvalidInput.NONE) return MemorandumState.InputError(it)
                }
            }
        }
        return MemorandumState.SuccessValidationInput
    }


}