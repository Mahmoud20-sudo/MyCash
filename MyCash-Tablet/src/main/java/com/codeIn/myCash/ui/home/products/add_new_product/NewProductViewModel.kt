package com.codeIn.myCash.ui.home.products.add_new_product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.Discount
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.AddCategoriesAdapter
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.SearchCategoryAutoCompleteTextViewAdapter
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModelSearch
import com.codeIn.myCash.features.stock.domain.category.usecases.AddNewCategoryUseCase
import com.codeIn.myCash.features.stock.domain.category.usecases.DeleteSomeCategoriesUSeCase
import com.codeIn.myCash.features.stock.domain.category.usecases.GetMainCategoriesUseCase
import com.codeIn.myCash.features.stock.domain.category.usecases.GetCategoryRequestUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.CreateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    private val getCategoriesUseCae : GetMainCategoriesUseCase,
    private val addNewCategoryUseCase: AddNewCategoryUseCase,
    private val deleteSomeCategoriesUSeCase: DeleteSomeCategoriesUSeCase,
    private val getCategoryRequestUseCase: GetCategoryRequestUseCase,
    private val createProductUseCase: CreateProductUseCase,
    prefs : SharedPrefsModule,
): ViewModel(){

    private val _dataResult = MutableLiveData<CategoryState>(CategoryState.Idle)
    val dataResult : LiveData<CategoryState> = _dataResult

    private val _currentCategory = MutableLiveData<CategoryModel?>(null)
    val currentCategory : LiveData<CategoryModel?> = _currentCategory

    private val _selectedCategories = MutableLiveData<ArrayList<CategoryModel>>(null)
    val selectedCategories : LiveData<ArrayList<CategoryModel>> = _selectedCategories

    var categoriesAutoCompleteSearchInAdapter : MutableLiveData<List<CategoryModelSearch>> = MutableLiveData()

    lateinit var adapterSubCategory : AddCategoriesAdapter
    var adapterSubCategoryInAdapter : SearchCategoryAutoCompleteTextViewAdapter?  = null

    private val _discountType = MutableStateFlow<Discount>(Discount.None)
    val discountType = _discountType.asStateFlow()

    private val _currencyDiscount = MutableStateFlow<String?>(null)
    val currencyDiscount = _currencyDiscount.asStateFlow()

    private var _currency : String? = prefs.getValue(Constants.getCurrency())

    init {
        getCategories(null)
        _selectedCategories.value = ArrayList<CategoryModel>()
        addNewCategory(CategoryModel(isFirst = true))
    }

    fun getCategories(parentId : String?){
        launchIO {
            getCategoriesUseCae.invoke(null , parentId).let {
                _dataResult.postValue(it)
            }
        }
    }

    fun updateCurrentCategory(categoryModel: CategoryModel?){
        launchIO {
            _currentCategory.postValue(categoryModel)
        }
    }

    fun addNewCategory(categoryModel: CategoryModel){
        launchIO {
            addNewCategoryUseCase.invoke(categoryModel , _selectedCategories.value as ArrayList<CategoryModel>).let {
                _selectedCategories.postValue(it)
            }
        }
    }

    fun deleteCategories(position : Int){
        launchIO {
            deleteSomeCategoriesUSeCase.invoke(position , _selectedCategories.value).let {
                _selectedCategories.postValue(it)
            }
        }
    }

    fun addNewProduct(name : String?, barcode : String?, description : String?, price : String?,
                      quantity : String?, discountType : Int, discount : String?, taxType: Int, taxable: Int,
                      hasDiscount : Int, imageFile : File?, buyPrice : String? ,
                      buyTaxAvailable : Int , buyTaxType : Int){
        launchIO {
            _dataResult.postValue(CategoryState.Loading)
            getCategoryRequestUseCase.invoke(_currentCategory.value , _selectedCategories.value).let {categoryRequest->
                createProductUseCase.invoke(
                    name, barcode  , description  , price  ,
                    quantity  , discountType , discount  , taxType, taxable ,
                    categoryRequest?.category?:""  , hasDiscount , categoryRequest?.parentCategoryId?:""  ,
                    categoryRequest?.categoryId?:"" ,  imageFile , buyPrice ,
                    buyTaxAvailable , buyTaxType
                ).let {response ->
                    _dataResult.postValue(response)
                }

            }
        }
    }


    fun updateDiscount(type : Int){
        when (type) {
            Discount.Value.value -> {
                _discountType.value = Discount.Value
                _currencyDiscount.value = _currency
            }
            Discount.Percentage.value -> {
                _discountType.value = Discount.Percentage
                _currencyDiscount.value = "%"
            }
            else -> {
                _discountType.value = Discount.None
                _currencyDiscount.value = ""
            }
        }
    }

    fun clearState(){
        if (_dataResult.value is CategoryState.Idle)
            _dataResult.value = CategoryState.Idle
    }
}