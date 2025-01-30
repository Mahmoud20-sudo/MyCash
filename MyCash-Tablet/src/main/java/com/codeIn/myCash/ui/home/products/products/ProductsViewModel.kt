package com.codeIn.myCash.ui.home.products.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeIn.common.data.AppConstants
import com.codeIn.common.data.Cart
import com.codeIn.common.data.ProductFilterType
import com.codeIn.common.offline.Constants
import com.codeIn.common.offline.SharedPrefsModule
import com.codeIn.common.util.launchIO
import com.codeIn.common.util.launchMain
import com.codeIn.myCash.features.stock.data.category.mapper.addAllItemToCategoryList
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoriesData
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryData
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.data.category.remote.response.category.FilterCategoryState
import com.codeIn.myCash.features.stock.data.madaTransactions.remote.response.TransactionState
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductModel
import com.codeIn.myCash.features.stock.data.product.remote.response.ProductsState
import com.codeIn.myCash.features.stock.domain.category.model.HistoryCategoryModel
import com.codeIn.myCash.features.stock.domain.category.usecases.GetFilterCategoriesUseCase
import com.codeIn.myCash.features.stock.domain.category.usecases.GetMainCategoriesUseCase
import com.codeIn.myCash.features.stock.domain.category.usecases.UpdateHistoryFilterCategoryUseCase
import com.codeIn.myCash.features.stock.domain.invoice.model.ProductsInCart
import com.codeIn.myCash.features.stock.domain.invoice.usecases.CartFactoryUseCase
import com.codeIn.myCash.features.stock.domain.invoice.usecases.UpdateProductsDependOnCartUseCase
import com.codeIn.myCash.features.stock.domain.madaTransactions.usecases.CreateMadaTransactionsVisaUseCase
import com.codeIn.myCash.features.stock.domain.product.model.ProductFilter
import com.codeIn.myCash.features.stock.domain.product.usecases.DeleteProductUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.GetProductsUseCase
import com.codeIn.myCash.features.stock.domain.product.usecases.GetProductsWithFilterInCartUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ProductsViewModel @Inject constructor(
    private val getMainCategoriesUseCase: GetMainCategoriesUseCase,
    private val getFilterCategoriesUseCase: GetFilterCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val cartFactoryUseCase: CartFactoryUseCase,
    private val prefs : SharedPrefsModule,
    private val state: SavedStateHandle,
    private val getProductsWithFilterInCartUseCase: GetProductsWithFilterInCartUseCase ,
    private val updateProductsDependOnCartUseCase: UpdateProductsDependOnCartUseCase ,
    private val createMadaTransactionsVisaUseCase: CreateMadaTransactionsVisaUseCase
) : ViewModel() {

    private val _categoriesDataResult = MutableStateFlow<CategoryState>(CategoryState.Idle)
    val categoriesDataResult = _categoriesDataResult.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    private val _productsDataResult = MutableStateFlow<ProductsState>(ProductsState.Idle)
    val productsDataResult = _productsDataResult.asStateFlow()

    private val _transactionResult = MutableStateFlow<TransactionState>(TransactionState.Idle)
    val transactionResult = _transactionResult.asStateFlow()

    private val _categoriesData = MutableStateFlow<ArrayList<CategoryData>?>(null)
    val categoriesData = _categoriesData.asStateFlow()

    private val _filterCategoriesDataResult = MutableStateFlow<FilterCategoryState>(FilterCategoryState.Idle)
    val filterCategoriesDataResult = _filterCategoriesDataResult.asStateFlow()

    var currency : String? = prefs.getValue(Constants.getCurrency())

    private val _selectedProductsInCart = MutableLiveData<ProductsInCart?>(null)
    val selectedProductsInCart : LiveData<ProductsInCart?> = _selectedProductsInCart

    private var _searchText = MutableLiveData<String?>(null)
    val searchText : LiveData<String?> = _searchText

    private var _products = MutableLiveData<ArrayList<ProductModel>?>()
    val products : LiveData<ArrayList<ProductModel>?> = _products

    private var _productFilter = MutableLiveData<ProductFilter?>()
    val productFilter : LiveData<ProductFilter?> = _productFilter

    //***************************************
    private val _categories = MutableLiveData<ArrayList<Category>>()
    val categories: LiveData<ArrayList<Category>> = _categories

    private val _categoriesMain = MutableLiveData<ArrayList<Category>>()
    val categoriesMain: LiveData<ArrayList<Category>> = _categoriesMain

    //MutableLiveData for the selected products list
    private val _selectedProducts = MutableLiveData<SelectedProducts>()
    val selectedProducts: LiveData<SelectedProducts> = _selectedProducts

    // the selected products list that will be used to generate the invoice
    private var _selectedProductsList: ArrayList<Product> = arrayListOf()
    val selectedProductsList get() = _selectedProductsList

    private var tax = prefs.getStringFlowForKey(AppConstants.TAX)
        .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    init {
        _selectedProductsInCart.value = ProductsInCart()
        _productFilter.postValue(ProductFilter("" , ProductFilterType.NONE))
        launchIO {
            tax.collect {
                handleProductInCart(null, Cart.INITIAL)
            }
        }
    }

    fun getCategories(parentOnly : String? , parentId : String?= null){
        launchIO {
            getMainCategoriesUseCase.invoke(parentOnly = parentOnly , parentId = parentId).let {
                _categoriesDataResult.emit(it)
                Log.d("TAG" , "Response category $it.")
            }
        }
    }

    private fun getFilterCategories(parentId : String?= null, categoryId: String?= null){
        launchIO {
            getFilterCategoriesUseCase.invoke(parentOnly = null , parentId = parentId, categoryId=categoryId).let {
                _filterCategoriesDataResult.emit(it)
                Log.d("TAG" , "Response filter category $it.")
            }
        }
    }

    fun updateSearchText(query : String?){
        _searchText.postValue(query)
    }


    fun updateCategoriesData(data : List<CategoryData>? , all : String  , hasCategory : Boolean= false ){
        if (data?.isNotEmpty() == true)
            _categoriesData.value = addAllItemToCategoryList(data , all , hasCategory) as ArrayList<CategoryData>
    }


    fun getProducts(categoryId: String?= null, productsOnly: Boolean = false){
        val date = if (productFilter.value?.date?.isNotEmpty() == true)
            productFilter.value?.date
        else
            null
        val discountType = when (productFilter?.value?.type?.value) {
            ProductFilterType.PRICE_DISCOUNT.value -> ProductFilterType.PRICE_DISCOUNT.value.toString()
            ProductFilterType.PERCENTAGE_DISCOUNT.value -> ProductFilterType.PERCENTAGE_DISCOUNT.value.toString()
            else -> null
        }
        val sort = when (productFilter.value?.type?.value) {
            ProductFilterType.LOWEST_PRICE.value -> ProductFilterType.LOWEST_PRICE.value.toString()
            ProductFilterType.HIGHEST_PRICE.value -> ProductFilterType.HIGHEST_PRICE.value.toString()
            ProductFilterType.RECENTLY_ADDED.value -> ProductFilterType.RECENTLY_ADDED.value.toString()
            ProductFilterType.NAME.value -> ProductFilterType.NAME.value.toString()
            else -> null
        }

        var childCategoryId = selectedCategoryId.value
        if (!productsOnly){
            if (selectedCategoryId.value != null && selectedCategoryId.value != "0") {
                getFilterCategories(selectedCategoryId.value , categoryId)
            }
            else
                _filterCategoriesDataResult.value = FilterCategoryState.SuccessFilterCategory(null)
        }
        if (categoryId != null )
            childCategoryId = categoryId
        launchIO {
            _productsDataResult.emit(ProductsState.Loading)
            getProductsUseCase.invoke(sort = sort ,
                searchText = searchText.value , categoryId = childCategoryId ,
                parentCategoryId = null , discountType = discountType , date = date).let {
                _productsDataResult.emit(it)
            }
        }
    }

    fun getProductsDependOnCart(products : ArrayList<ProductModel>){
        launchIO {
            updateProductsDependOnCartUseCase.invoke(products , selectedProductsInCart.value).let {
                _products.postValue(it as ArrayList<ProductModel>?)
            }
        }
    }
    fun deleteProduct(productId : String?){
        launchIO {
            deleteProductUseCase.invoke(productId).let {
                _productsDataResult.emit(it)
            }
        }
    }

    fun updateProductFilter(filter : ProductFilter){
        _productFilter.postValue(filter)
    }
    fun updateSelectedCategoryId(categoryId: String?){
        _selectedCategoryId.value = categoryId
    }

    fun handleProductInCart(product: ProductModel?, type : Cart) {
        launchMain {
            cartFactoryUseCase.invoke(product , selectedProductsInCart.value , type).let {
                _selectedProductsInCart.value = it
            }
        }
    }


    private fun updateProductsInCart(){
        launchIO {
            getProductsWithFilterInCartUseCase.invoke(sort = null , searchText = null , categoryId = selectedCategoryId.value , parentCategoryId = null , selectedProductInCart = selectedProductsInCart.value , null , null).let {
                _productsDataResult.emit(it)
            }
        }
    }

    fun updateSelectedProductDependOnBack(productsInCart: ProductsInCart){
        _selectedProductsInCart.value = productsInCart
        updateProductsInCart()
    }

    fun clearSelectedCategories(data : CategoriesData){

    }

    fun clearState(){
        _productsDataResult.value = ProductsState.Idle
    }

    fun createMadaTransaction(
        amount : String?,
        runRefund : String?,
        codeRefund : String?,
        dateRefund : String?,
        type : String?,
    ){
        launchIO {
            createMadaTransactionsVisaUseCase.invoke(amount , runRefund, codeRefund, dateRefund, type).let {
                _transactionResult.emit(it)
            }
        }
    }
}