package com.codeIn.myCash.features.stock.data.category.repository

import android.util.Log
import com.codeIn.common.data.ErrorHandlerImpl
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.myCash.features.sales.data.expenses.remote.response.ExpenseState
import com.codeIn.myCash.features.stock.data.category.remote.Category
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.category.remote.response.category.FilterCategoryState
import com.codeIn.myCash.features.stock.data.product.remote.response.SelectedCategory
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModelRequest
import com.codeIn.myCash.features.stock.domain.category.model.CategoryRequest
import com.codeIn.myCash.features.stock.domain.category.model.HistoryCategoryModel
import com.codeIn.myCash.features.stock.domain.category.model.HistoryCategoryModule
import com.codeIn.myCash.features.stock.domain.category.repository.CategoryRepository
import com.google.gson.Gson
import javax.inject.Inject

class CategoryRepositoryImpl  @Inject constructor (private var category: Category,
                                                   private val prefs : SharedPrefsModule,
                                                   private val errorHandler: ErrorHandlerImpl,
                                                   private val historyCategoryModule: HistoryCategoryModule) : CategoryRepository{
    override suspend fun getCategories(parentOnly: String?, parentId: String?): CategoryState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            category.getCategories(lang , token , parentOnly , parentId , null ).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {
                        CategoryState.SuccessMainCategories(response.body()?.data)
                    }
                    else {
                        CategoryState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    CategoryState.UnAuthorized
                }
                else{
                    CategoryState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        }catch (throwable : Throwable){
            CategoryState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun getFilterCategories(
        parentOnly: String?,
        parentId: String?,
        categoryId : String?
    ): FilterCategoryState {
        return try{
            val lang  = prefs.getValue(Constants.getLang())
            val token  = prefs.getValue(Constants.getToken())

            category.getCategories(lang , token , parentOnly , parentId , categoryId).let { response ->
                if(response.isSuccessful){
                    if (response.body()?.status == 1) {

                        FilterCategoryState.SuccessFilterCategory(response.body()?.data)
                    }
                    else {
                        FilterCategoryState.StateError(response.body()?.message)
                    }
                }else if (response.code() == 401){
                    FilterCategoryState.UnAuthorized
                }
                else{
                    FilterCategoryState.ServerError(errorHandler.invoke(response.code()))
                }
            }

        }catch (throwable : Throwable){
            FilterCategoryState.ServerError(errorHandler.getError(throwable))
        }
    }

    override suspend fun updateHistoryFilterCategory (historyCategoryModel: HistoryCategoryModel) : Boolean{
        historyCategoryModule.setItem(historyCategoryModel)
        return true
    }

    override suspend fun getHistoryFilterCategory(): List<CategoryData>? {
        return historyCategoryModule.getItem()
    }

    override suspend fun getCategoryRequest(
        currentCategoryModel: CategoryModel?,
        selectedCategories: List<CategoryModel>?
    ): CategoryRequest? {

        val categoryRequest = CategoryRequest()

        val list : ArrayList <CategoryModelRequest> = ArrayList()
        var request : CategoryModelRequest

        if (currentCategoryModel?.id != 0)
        {
            categoryRequest.categoryId = currentCategoryModel?.id.toString()
        }
        else
        {
            if (selectedCategories?.isNotEmpty() == true) {
                selectedCategories.forEach {
                    if (it.id != 0) {
                        categoryRequest.categoryId = it.id.toString()
                    }
                }
            }
            else {
                categoryRequest.category = ""
            }
        }

        if (currentCategoryModel?.id != 0 && selectedCategories.isNullOrEmpty()){
            categoryRequest.parentCategoryId = currentCategoryModel?.id.toString()
        }
        else if (selectedCategories?.isNotEmpty() == true){
            if (selectedCategories[0].id != 0)
                categoryRequest.parentCategoryId = selectedCategories[0].id.toString()
        }


        if (currentCategoryModel?.id == 0 && currentCategoryModel.name != "")
        {
            if (selectedCategories?.isNotEmpty()== true)
            {
                selectedCategories.forEach {
                    if (it.id == 0)
                    {
                        val categoryModelRequest : CategoryModelRequest = CategoryModelRequest(it.name , null)
                        list.add(categoryModelRequest)
                    }
                }
            }
            val categoryModelRequest : CategoryModelRequest = CategoryModelRequest(currentCategoryModel.name , null)
            list.add(categoryModelRequest)
        }
        else if (currentCategoryModel?.id != 0){
            if (selectedCategories?.isNotEmpty() == true)
            {
                for (i in selectedCategories)
                {
                    if (i.id != 0)
                    {
                        continue
                    }
                    else
                    {
                        val categoryModelRequest : CategoryModelRequest = CategoryModelRequest(i.name , null)
                        list.add(categoryModelRequest)
                    }
                }
            }

        }


        if (list.size > 0 )
        {
            request = CategoryModelRequest(null , null)
            request.name = list[0].name
            list.removeAt(list.lastIndex)
            val length = list.size - 2
            val index =  list.size - 1
            for( i in length downTo 0){
                if (i == length){
                    list[i].child = CategoryModelRequest(list[index].name , null)
                }
                else{
                    list[i] = CategoryModelRequest(list[i].name ,  list[i+1])
                }
            }
            if (list.isNotEmpty())
                request = list[0]
            categoryRequest.category = Gson().toJson(request)
        }

        if (categoryRequest.parentCategoryId == "null")
            categoryRequest.parentCategoryId = null
        if(categoryRequest.categoryId == "null")
            categoryRequest.categoryId = null
        return categoryRequest
    }

    override suspend fun getSelectedCategoryModel(data: CategoryData): ArrayList<CategoryModel>? {

        val lastIndex = data?.subCategories?.indexOfFirst { it.selected } ?: -1
        val selectedCategories = ArrayList<CategoryModel>()

        if (lastIndex != -1 ){
            if (data?.subCategories?.isNotEmpty() == true)
                data?.subCategories.forEachIndexed{i , value->
                    if (i <= lastIndex)
                        selectedCategories.add(CategoryModel(id= value.id , name =  value.name , isEnabled = true))
                }


            if (selectedCategories.isNotEmpty()){
                selectedCategories[0].isFirst = true

                selectedCategories.forEachIndexed{ i , _ ->
                    if (i != lastIndex){
                        selectedCategories[i].addChild = true
                    }
                }
            }

        }
        return selectedCategories.apply { add(0, CategoryModel(id= data.id , name =  data.name , isEnabled = true)) }

    }


}