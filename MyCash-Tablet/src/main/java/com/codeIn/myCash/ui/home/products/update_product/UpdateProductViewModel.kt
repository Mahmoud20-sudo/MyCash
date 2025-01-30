package com.codeIn.myCash.ui.home.products.update_product

import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.codeIn.common.data.Discount
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.AddCategoriesAdapter
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.SearchCategoryAutoCompleteTextViewAdapter
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.data.product.remote.response.SelectedCategory
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModelSearch
import com.codeIn.myCash.features.stock.domain.category.usecases.AddNewCategoryUseCase
import com.codeIn.myCash.features.stock.domain.category.usecases.DeleteSomeCategoriesUSeCase
import com.codeIn.myCash.features.stock.domain.category.usecases.GetMainCategoriesUseCase
import com.codeIn.myCash.features.stock.domain.category.usecases.GetCategoryRequestUseCase
import com.codeIn.myCash.features.stock.domain.category.usecases.GetSelectedCategoriesInDetailsProductUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.GetSingleProductUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateProductViewModel @Inject constructor(
    private val getCategoriesUseCae : GetMainCategoriesUseCase,
    private val addNewCategoryUseCase: AddNewCategoryUseCase,
    private val deleteSomeCategoriesUSeCase: DeleteSomeCategoriesUSeCase,
    private val getCategoryRequestUseCase: GetCategoryRequestUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val getSingleProductUseCase: GetSingleProductUseCase,
    private val getSelectedCategoriesInDetailsProductUseCase: GetSelectedCategoriesInDetailsProductUseCase ,
    prefs : SharedPrefsModule,
    state:SavedStateHandle
): ViewModel(){

    private val _dataResult = MutableLiveData<CategoryState>(CategoryState.Idle)
    val dataResult : LiveData<CategoryState> = _dataResult

    private val _productDataResult = MutableStateFlow<ProductsState>(ProductsState.Idle)
    val productDataResult = _productDataResult.asStateFlow()

    private val _currentCategory = MutableLiveData<CategoryModel?>(null)
    val currentCategory : LiveData<CategoryModel?> = _currentCategory

    private val _selectedCategories = MutableLiveData<ArrayList<CategoryModel>>(null)
    val selectedCategories : LiveData<ArrayList<CategoryModel>> = _selectedCategories

    var categoriesAutoCompleteSearchInAdapter : MutableLiveData<List<CategoryModelSearch>> = MutableLiveData()

    lateinit var adapterSubCategory : UpdatesCategoriesAdapter
    var adapterSubCategoryInAdapter : SearchCategoryAutoCompleteTextViewAdapter?  = null

    private val _discountType = MutableStateFlow<Discount>(Discount.None)
    val discountType = _discountType.asStateFlow()

    private val _labelDiscount = MutableStateFlow<String?>(null)
    val labelDiscount = _labelDiscount.asStateFlow()

    private var _currency : String? = prefs.getValue(Constants.getCurrency())

    private val _productId = MutableStateFlow<String?>(null)
    val productId = _productId.asStateFlow()


    init {
        _selectedCategories.value = ArrayList<CategoryModel>()
        _productId.value = state["productId"]
        if (_productId.value?.isNotEmpty() == true) {
            getSingleProduct()
        }
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

    private fun getSingleProduct(){
        launchIO {
            getSingleProductUseCase.invoke(productId = productId.value).let { product ->
                _productDataResult.emit(product)
                Log.d("TAG" , "product ,,,,$product , ${productId.value}")
            }
        }
    }


    fun addNewCategory(categoryModel: CategoryModel ){
        launchMain {
            addNewCategoryUseCase.invoke(categoryModel , _selectedCategories.value as ArrayList<CategoryModel>).let {
                _selectedCategories.postValue( it)
            }
        }
    }

    fun getSelectedCategories(data: List<SelectedCategory>){
        launchIO {
            getSelectedCategoriesInDetailsProductUseCase.invoke(data).let {

                _selectedCategories.postValue(it)
                if (it?.isNotEmpty() == true)
                    updateCurrentCategory(it[it.lastIndex])
                else
                    addNewCategory(CategoryModel(isFirst = true))
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

    fun updateProduct(name : String?, barcode : String?, description : String?, price : String?,
                      quantity : String?, discountType : Int, discount : String?, taxType: Int, taxable: Int,
                      hasDiscount : Int, imageFile : File?, buyPrice : String? ,
                      buyTaxAvailable : Int , buyTaxType : Int){
        launchIO {
            _dataResult.postValue(CategoryState.Loading)
            getCategoryRequestUseCase.invoke(_currentCategory.value , _selectedCategories.value).let {categoryRequest->
                updateProductUseCase.invoke( productId.value ,
                    name, barcode  , description  , price  ,
                    quantity  , discountType , discount  , taxType, taxable ,
                    categoryRequest?.category?:""  , hasDiscount ,
                    categoryRequest?.parentCategoryId?:""  ,
                    categoryRequest?.categoryId?:"" ,  imageFile ,
                    buyPrice , buyTaxAvailable , buyTaxType
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
                _labelDiscount.value = _currency
            }
            Discount.Percentage.value -> {
                _discountType.value = Discount.Percentage
                _labelDiscount.value = "%"
            }
            else -> {
                _discountType.value = Discount.None
                _labelDiscount.value = ""
            }
        }
    }

    fun clearState(){
        if (_dataResult.value is CategoryState.Idle)
            _dataResult.value = CategoryState.Idle
    }
}