package com.codeIn.myCash.ui.home.products.update_product

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.util.disable
import com.codeIn.common.util.enable
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemCategoryAddProductBinding
import com.codeIn.myCash.ui.hideKeyboard
import com.codeIn.myCash.ui.home.products.add_new_product.adapters.SearchCategoryAutoCompleteTextViewAdapter
import com.codeIn.myCash.features.stock.data.category.mapper.convertCategory
import com.codeIn.myCash.features.stock.data.category.remote.response.category.CategoryState
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModel
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModelSearch


class UpdatesCategoriesAdapter() : RecyclerView.Adapter<UpdatesCategoriesAdapter.ViewHolder>(){
    private lateinit var dataList: LiveData<ArrayList<CategoryModel>>
    private var context: Context? = null
    private var viewModel : UpdateProductViewModel? = null
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var listener : Listener

    constructor(dataList: LiveData<ArrayList<CategoryModel>>,
                context: Context?, viewModel: UpdateProductViewModel,
                lifecycleOwner: LifecycleOwner,
                listener: Listener
    ) : this() {
        this.dataList = dataList
        this.context = context
        this.viewModel = viewModel
        this.lifecycleOwner = lifecycleOwner
        this.listener = listener
    }

    class ViewHolder(private var binding: ItemCategoryAddProductBinding,
                     var dataList: LiveData<ArrayList<CategoryModel>>, val viewModel: UpdateProductViewModel,
                     private val lifecycleOwner: LifecycleOwner, val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryModel ) {
            binding.apply {
                category.setText(item.name)
                if (!item.isFirst)
                    addCategory.text = ""
                else
                    addCategory.text = binding.root.resources.getString(R.string.add_category)

                if (item.addChild){
                    close.visible()
                    addCategory.gone()
                    category.disable()
                }
                else{
                    close.gone()
                    addCategory.visible()
                    category.enable()

                    if (adapterPosition > 0)
                    {
                        if (viewModel.selectedCategories.value!![adapterPosition - 1].id != 0)
                            viewModel.getCategories(viewModel.selectedCategories.value!![adapterPosition - 1].id.toString())
                        else
                            viewModel.categoriesAutoCompleteSearchInAdapter.value = ArrayList<CategoryModelSearch>()
                    }
                    else if (adapterPosition == 0 )
                        viewModel.getCategories(null)


                    category.setDropDownBackgroundDrawable(binding.root.context.resources.getDrawable(R.drawable.autocomplete_dropdown))

                    category.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                        val autoCompleteText = adapterView.getItemAtPosition(i) as CategoryModelSearch
                        binding.category.setText(autoCompleteText.name)
                        dataList.value!![adapterPosition].name = autoCompleteText.name
                        dataList.value!![adapterPosition].id = autoCompleteText.id
                        viewModel.updateCurrentCategory(CategoryModel(id= autoCompleteText.id , name = autoCompleteText.name , isEnabled = true))
                    }

                    category.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
                        override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                            dataList.value!![adapterPosition].name = charSequence.toString()
                        }
                        override fun afterTextChanged(editable: Editable) {

                        }
                    })
                    viewModel.apply {
                        categoriesAutoCompleteSearchInAdapter.observe(lifecycleOwner){
                            adapterSubCategoryInAdapter =  SearchCategoryAutoCompleteTextViewAdapter(binding.root.context , it)
                            category.setAdapter(viewModel.adapterSubCategoryInAdapter)
                        }

                        dataResult.observe(lifecycleOwner) {response->
                            when(response){
                                is CategoryState.SuccessMainCategories ->{
                                    if (response.data?.data?.isNotEmpty() == true) {
                                        val categories = convertCategory(response.data?.data)
                                        categoriesAutoCompleteSearchInAdapter.value = categories
                                    }
                                    else
                                    {
                                        categoriesAutoCompleteSearchInAdapter.value = ArrayList()

                                    }
                                }
                                else ->{

                                }
                            }
                        }
                    }
                }

                addCategory.setOnClickListener {
                    if (!item.addChild && item.name != "")
                    {
                        item.addChild = true
                        viewModel.addNewCategory(CategoryModel())
                        viewModel.adapterSubCategory.notifyDataSetChanged()
                        hideKeyboard(binding.root.context , binding.addCategory)
                    }
                }
                category.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
                    override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                        dataList.value?.get(adapterPosition)?.name = charSequence.toString()
                        dataList.value?.get(adapterPosition)?.id = 0
                        if (charSequence.isNotEmpty())
                            viewModel.updateCurrentCategory(CategoryModel(id= 0 , name = charSequence.toString() , isEnabled = true))
                        else
                            viewModel.updateCurrentCategory(CategoryModel(id= 0 , name = charSequence.toString() , isEnabled = false))
                    }
                    override fun afterTextChanged(editable: Editable) {}
                })
                close.setOnClickListener {
                    if (item.addChild )
                        listener.onDeleteClick(adapterPosition)
                }
            }




        }
    }

    interface Listener {
        fun onDeleteClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryAddProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            , this.dataList , this.viewModel!! , lifecycleOwner , listener)
    }

    override fun getItemCount(): Int {
        return dataList.value!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList.value!!.get(position))
    }





}