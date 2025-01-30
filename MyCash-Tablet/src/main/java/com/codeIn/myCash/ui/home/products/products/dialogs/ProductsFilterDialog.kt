package com.codeIn.myCash.ui.home.products.products.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.codeIn.common.data.ProductFilterType
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.DialogHomeProductsFilterBinding
import com.codeIn.myCash.ui.ViewStrokes
import com.codeIn.myCash.ui.updateSectionsViews
import com.codeIn.myCash.utilities.pickers.DatePicker
import com.codeIn.myCash.features.stock.domain.product.model.ProductFilter


class ProductsFilterDialog(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val communicator: Communicator,
    private val filter : ProductFilter?,
) :
    Dialog(context, R.style.PauseDialog) {

    private lateinit var binding: DialogHomeProductsFilterBinding
    private val datePicker = DatePicker()
    private var selectedProductFilterType: ProductFilterType = ProductFilterType.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogHomeProductsFilterBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            dateTextView.setOnClickListener {
                datePicker.showDatePicker(
                    childFragmentManager = fragmentManager,
                    textView = dateTextView,
                    preventFutureDates = true
                )
                updateSelectedView(ProductFilterType.NONE)
            }
            if(filter?.date?.isNotEmpty() == true){
                dateTextView.text = filter?.date
            }
            updateSelectedView(filter?.type ?: ProductFilterType.NONE)

            lowestPriceTextView.setOnClickListener {
                updateSelectedView(ProductFilterType.LOWEST_PRICE)
                dateTextView.text = ""
            }
            highestPriceTextView.setOnClickListener {
                updateSelectedView(ProductFilterType.HIGHEST_PRICE)
                dateTextView.text = ""
            }
            recentlyAddedTextView.setOnClickListener {
                updateSelectedView(ProductFilterType.RECENTLY_ADDED)
                dateTextView.text = ""
            }
            nameTextView.setOnClickListener {
                updateSelectedView(ProductFilterType.NAME)
                dateTextView.text = ""
            }
            percentageDiscountTextView.setOnClickListener {
                updateSelectedView(ProductFilterType.PERCENTAGE_DISCOUNT)
                dateTextView.text = ""
            }
            priceDiscountTextView.setOnClickListener {
                updateSelectedView(ProductFilterType.PRICE_DISCOUNT)
                dateTextView.text = ""
            }

            continueButton.setOnClickListener {
                val filter = ProductFilter(
                    date = dateTextView.text.toString() ,
                    selectedProductFilterType
                )
                communicator.applyFilter(filter)
                dismiss()
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
        }
    }


    private fun updateSelectedView(type: ProductFilterType) {

        selectedProductFilterType = if (selectedProductFilterType == type) ProductFilterType.NONE else type

        // Apply secondary style to all views initially
        val viewsToStyle = listOf(
            binding.lowestPriceTextView,
            binding.highestPriceTextView,
            binding.recentlyAddedTextView,
            binding.nameTextView,
            binding.percentageDiscountTextView,
            binding.priceDiscountTextView
        )

        val selectedView = when (selectedProductFilterType) {
            ProductFilterType.LOWEST_PRICE -> binding.lowestPriceTextView
            ProductFilterType.HIGHEST_PRICE -> binding.highestPriceTextView
            ProductFilterType.RECENTLY_ADDED -> binding.recentlyAddedTextView
            ProductFilterType.NAME -> binding.nameTextView
            ProductFilterType.PERCENTAGE_DISCOUNT -> binding.percentageDiscountTextView
            ProductFilterType.PRICE_DISCOUNT -> binding.priceDiscountTextView
            else -> null
        }
        updateSectionsViews(
            context = context,
            viewsToStyle = viewsToStyle,
            selectedView = selectedView,
            stroke = ViewStrokes.R0_S1
        )
    }


    interface Communicator {
        fun applyFilter(filter: ProductFilter)
    }




}