package com.codeIn.myCash.features.stock.data.product.repository

import android.util.Log
import com.codeIn.common.calculator.TaxCalculator
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.data.InvalidInput
import com.codeIn.common.data.Limit
import com.codeIn.common.data.NumberHelper
import com.codeIn.common.data.TaxType
import com.codeIn.common.data.Taxable
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.product.remote.Product
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.domain.product.repository.ProductRepository
import com.codeIn.myCash.features.stock.domain.product.usecases.CreateProductValidationUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.ValidationDiscountUseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor (private var product: Product,
                                                 private val prefs : SharedPrefsModule,
                                                 private val errorHandler: ErrorHandlerImpl,
                                                 private val discountUseCase: ValidationDiscountUseCase ,
                                                 private val createProductValidationUseCase: CreateProductValidationUseCase ,
                                                 private val taxCalculator: TaxCalculator)
    : ProductRepository {
    override suspend fun createProduct(
        name: String?,
        barcode: String?,
        description: String?,
        price: String?,
        quantity: String?,
        discountType: Int,
        discount: String?,
        taxType: Int,
        taxable: Int,
        category: String?,
        hasDiscount: Int,
        parentCategoryId: String?,
        categoryId: String?,
        buyPrice : String? ,
        imageFile: File?,
        buyTaxAvailable : Int ,
        buyTaxType : Int,
        branch: String?
    ): CategoryState {

        return try {
            createProductValidationUseCase.invoke(name = name, price = price, quantity = quantity , buyPrice= buyPrice)
                .let {
                    if (it != InvalidInput.NONE) return CategoryState.InputError(it)
                }
            val tax =
                taxCalculator.calculateTaxValue(NumberHelper.persianToEnglishText(price ?: "0.0"))
            val finalPrice = if (taxable == Taxable.YES.value && taxType == TaxType.Excludes.value)
                NumberHelper.persianToEnglishText(price?:"0.0").toDouble() + NumberHelper.persianToEnglishText(tax?:"0.0").toDouble()
            else
                NumberHelper.persianToEnglishText(price?:"0.0").toDouble()
            if (hasDiscount == 1) {
                discountUseCase.invoke(
                    discountType = discountType,
                    discount = discount,
                    finalPrice = finalPrice.toString()
                ).let {
                    if (it != InvalidInput.NONE) return CategoryState.InputError(it)
                }
            }

            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            Log.d("TAG" , "Discount $discountType , $discount , $hasDiscount")

            if (imageFile != null) {

                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                product.createProductWithImage(
                    lang = lang,
                    authorization = token,
                    name = name,
                    price = price,
                    hasDiscount = hasDiscount,
                    discount= discount ,
                    discountType = discountType ,
                    desc = description,
                    barCode = barcode,
                    catId = categoryId,
                    parentCatId = parentCategoryId,
                    taxAvailable = taxable,
                    taxType = taxType,
                    category = category,
                    quantity = quantity,
                    image = body,
                    branch = branch
//                    buyPrice = buyPrice,
//                    buyTaxAvailable = buyTaxAvailable,
//                    buyTaxType = buyTaxType
                ).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            CategoryState.SuccessDetailsProduct(response.body()?.data)
                        } else {
                            CategoryState.StateError(response.body()?.message)
                        }
                    }  else if (response.code() == 401){
                        CategoryState.UnAuthorized
                    } else {
                        CategoryState.ServerError(errorHandler.invoke(response.code()))
                    }
                }
            } else {

                product.createProductWithoutImage(
                    lang = lang,
                    authorization = token,
                    name = name,
                    price = price,
                    hasDiscount = hasDiscount,
                    discount= discount ,
                    discountType = discountType ,
                    desc = description,
                    barCode = barcode,
                    catId = categoryId,
                    parentCatId = parentCategoryId,
                    taxAvailable = taxable,
                    taxType = taxType,
                    category = category,
                    quantity = quantity,
                    branch = branch
//                    buyPrice = buyPrice,
//                    buyTaxAvailable = buyTaxAvailable,
//                    buyTaxType = buyTaxType
                ).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            CategoryState.SuccessDetailsProduct(response.body()?.data)
                        } else {
                            CategoryState.StateError(response.body()?.message)
                        }
                    } else if (response.code() == 401){
                        CategoryState.UnAuthorized
                    }  else {
                        CategoryState.ServerError(errorHandler.invoke(response.code()))
                    }
                }
            }

        } catch (throwable: Throwable) {
            CategoryState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun updateProduct(
        productId: String?,
        name: String?,
        barcode: String?,
        description: String?,
        price: String?,
        quantity: String?,
        discountType: Int,
        discount: String?,
        taxType: Int,
        taxable: Int,
        category: String?,
        hasDiscount: Int,
        parentCategoryId: String?,
        categoryId: String?,
        buyPrice: String?,
        imageFile: File?,
        buyTaxAvailable: Int,
        buyTaxType: Int,
        branch: String?
    ): CategoryState {
        return try {
            createProductValidationUseCase.invoke(name = name, price = price, quantity = quantity , buyPrice= buyPrice)
                .let {
                    if (it != InvalidInput.NONE) return CategoryState.InputError(it)
                }
            val tax =
                taxCalculator.calculateTaxValue(NumberHelper.persianToEnglishText(price ?: "0.0"))
            var finalPrice = if (taxable == Taxable.YES.value && taxType == TaxType.Excludes.value)
                NumberHelper.persianToEnglishText(price?:"0.0").toDouble() + NumberHelper.persianToEnglishText(tax?:"0.0").toDouble()
            else
                NumberHelper.persianToEnglishText(price?:"0.0").toDouble()


            if (hasDiscount == 1) {
                discountUseCase.invoke(
                    discountType = discountType,
                    discount = discount,
                    finalPrice = finalPrice.toString()
                ).let {

                    if (it != InvalidInput.NONE) return CategoryState.InputError(it)
                }
            }

            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())

            if (imageFile != null) {

                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
                val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
                product.updateProductWithImage(
                    lang = lang,
                    authorization = token,
                    productId = productId ,
                    name = name,
                    price = price,
                    hasDiscount = hasDiscount,
                    discount= discount ,
                    discountType = discountType ,
                    desc = description,
                    barCode = barcode,
                    catId = categoryId,
                    parentCatId = parentCategoryId,
                    taxAvailable = taxable,
                    taxType = taxType,
                    category = category,
                    quantity = quantity,
                    image = body,
                    branch = branch
//                    buyPrice = buyPrice,
//                    buyTaxAvailable = buyTaxAvailable,
//                    buyTaxType = buyTaxType
                ).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            CategoryState.SuccessUpdateProduct(response.body()?.data)
                        } else {
                            CategoryState.StateError(response.body()?.message)
                        }
                    } else if (response.code() == 401){
                        CategoryState.UnAuthorized
                    }  else {
                        CategoryState.ServerError(errorHandler.invoke(response.code()))
                    }
                }
            } else {
                product.updateProductWithoutImage(
                    lang = lang,
                    authorization = token,
                    productId = productId ,
                    name = name,
                    price = price,
                    hasDiscount = hasDiscount,
                    discount= discount ,
                    discountType = discountType ,
                    desc = description,
                    barCode = barcode,
                    catId = categoryId,
                    parentCatId = parentCategoryId,
                    taxAvailable = taxable,
                    taxType = taxType,
                    category = category,
                    quantity = quantity,
                    branch = branch
//                    buyPrice = buyPrice,
//                    buyTaxAvailable = buyTaxAvailable,
//                    buyTaxType = buyTaxType
                ).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()?.status == 1) {
                            CategoryState.SuccessUpdateProduct(response.body()?.data)
                        } else {
                            CategoryState.StateError(response.body()?.message)
                        }
                    }  else if (response.code() == 401){
                        CategoryState.UnAuthorized
                    } else {
                        CategoryState.ServerError(errorHandler.invoke(response.code()))
                    }
                }
            }

        } catch (throwable: Throwable) {
            CategoryState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getProducts(
        sort: String?,
        searchText: String?,
        categoryId: String?,
        parentCategoryId: String? ,
        discountType : String? ,
        date : String?
    ): ProductsState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            product.getProducts(
                lang=lang , authorization = token , sort=sort ,
                categoryId = categoryId , parentCategoryId=parentCategoryId ,
                searchText = searchText , discountType = discountType , date = date ,
                limit = Limit.HUNDRED.value.toString()
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ProductsState.SuccessShowProducts(response.body()?.data)
                    } else {
                        ProductsState.StateError(response.body()?.message)
                    }
                } else if (response.code() == 401){
                    ProductsState.UnAuthorized
                }  else {
                    ProductsState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            ProductsState.ServerError(errorHandler.getError(throwable))
        }
    }


    override suspend fun deleteProduct(productId: String?): ProductsState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            product.deleteProduct(
                lang = lang , authorization = token , productId = productId
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ProductsState.SuccessDeleteProduct(response.body()?.message)
                    } else {
                        ProductsState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    ProductsState.UnAuthorized
                }  else {
                    ProductsState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            ProductsState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getSingleProduct(productId: String?): ProductsState {
        return try {
            val lang = prefs.getValue(Constants.getLang())
            val token = prefs.getValue(Constants.getToken())
            product.getSingleProduct(
                lang=lang , authorization = token , productId= productId
            ).let { response ->
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {
                        ProductsState.SuccessDetailsProduct(response.body()?.data)
                    } else {
                        ProductsState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    ProductsState.UnAuthorized
                }  else {
                    ProductsState.ServerError(errorHandler.invoke(response.code()))
                }
            }
        } catch (throwable: Throwable) {
            ProductsState.ServerError(errorHandler.getError(throwable))
        }
    }
}
