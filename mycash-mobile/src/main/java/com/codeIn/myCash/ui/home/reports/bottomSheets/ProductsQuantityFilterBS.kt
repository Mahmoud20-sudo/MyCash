package com.codeIn.myCash.ui.home.reports.bottomSheets

import android.content.Context
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.codeIn.common.data.ReceiptStatusFilter
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ProductsInventoryFilterBottomSheetBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.ui.getInvoiceFilterDisplayName
import com.codeIn.myCash.ui.home.reports.adapters.BranchesAutoCompleteAdapter
import com.codeIn.myCash.ui.home.reports.adapters.ReceiptStatusAutoCompleteAdapter
import com.codeIn.myCash.utilities.onClearFocusAndHideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProductsQuantityFilterBS(private val context: Context) {

    private lateinit var sheetBinding: ProductsInventoryFilterBottomSheetBinding
    private var bottomSheetDialog: BottomSheetDialog? = null

    private var branchId: Int = -1
    private var productStatusId: Int? = null
    private var comparisonOptionId = 0 // -> 0 for less than and 1 for equal to
    private var selectedBranchId: Int = 0
    private lateinit var branches: List<BranchesDTO.Data.Data>
    private var defaultBranchPosition: Int = 0
    private var selectedBranchPosition: Int = 0

    private var productStatusListener: ((
        branchId: Int,
        selectedBranchPosition: Int,
        productStatusId: Int?,
        comparisonOptionId: Int?,
        quantities: Int?
    ) -> Unit) = { _, _, _, _, _ -> }

    private lateinit var adapterBranches: BranchesAutoCompleteAdapter

    init {
        buildBottomSheetDialog()
        initComparisonOption()

        sheetBinding.returnedPrice.onClearFocusAndHideKeyboard(context)
    }

    private fun initComparisonOption() {

        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            context.resources.getStringArray(R.array.comparison_options)
        )

        sheetBinding.comparisonOptions.setAdapter(adapter)
        setDefaultComparisonOption(
            0, context.resources.getStringArray(R.array.comparison_options).first()
        )
        sheetBinding.comparisonOptions.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position) as String
            setDefaultComparisonOption(position, selectedItem)
        }
        sheetBinding.comparisonOptions.setOnClickListener {
            sheetBinding.comparisonOptions.showDropDown()
        }

    }

    private fun buildBottomSheetDialog(): BottomSheetDialog? {
        val inflater = LayoutInflater.from(context)
        sheetBinding = ProductsInventoryFilterBottomSheetBinding.inflate(inflater)
        bottomSheetDialog = BottomSheetDialog(context, R.style.reports_bottom_sheet_style)
        bottomSheetDialog?.setContentView(sheetBinding.root)
        sheetBinding.tvClose.setOnClickListener { dismiss() }
        onClicks()
        return bottomSheetDialog
    }

    private fun onClicks() {
        sheetBinding.apply {
            initBranch()
            initProductStatus()

            applyBtn.setOnClickListener {
                productStatusListener(
                    branchId,
                    selectedBranchPosition,
                    productStatusId,
                    comparisonOptionId,
                    sheetBinding.returnedPrice.text.toString().toIntOrNull()
                )

                dismiss()
            }
            reset.setOnClickListener {
                clearState()
            }
        }
    }

    private fun clearState() {
        setSelectedBranch(defaultBranchPosition, branches)
        setDefaultProductStatus(ReceiptStatusFilter.ALL)
        setDefaultComparisonOption(
            0, context.resources.getStringArray(R.array.comparison_options).first()
        )
        sheetBinding.returnedPrice.setText("0")
        productStatusListener(
            branchId,
            defaultBranchPosition,
            productStatusId,
            comparisonOptionId,
            null
        )
    }

    private fun initBranch() {
        sheetBinding.branch.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                val selectedBranch =
                    adapterView.getItemAtPosition(position) as BranchesDTO.Data.Data
                branchId = selectedBranch.id
                selectedBranchPosition = position
                sheetBinding.branch.setText(selectedBranch.name, false)

                // Clear focus to ensure dropdown is reset for next click
                sheetBinding.productStatus.clearFocus()
            }
        sheetBinding.branch.setOnClickListener {
            sheetBinding.branch.showDropDown()
        }
    }

    private fun initProductStatus() {
        val receiptStatusAdapter =
            ReceiptStatusAutoCompleteAdapter(context, ReceiptStatusFilter.entries)
        sheetBinding.productStatus.setAdapter(receiptStatusAdapter)
        setDefaultProductStatus(ReceiptStatusFilter.ALL)
        sheetBinding.productStatus.setOnItemClickListener { _, _, position, _ ->
            val selectedFilter = receiptStatusAdapter.getItem(position)
            setDefaultProductStatus(selectedFilter)
            sheetBinding.productStatus.clearFocus()
        }
        sheetBinding.productStatus.setOnClickListener {
            sheetBinding.productStatus.showDropDown()
        }

    }

    fun getProductStatus(
        receiptStatus: (
            branchId: Int, selectedBranchPosition: Int, productStatusId: Int?, comparisonOptionId: Int?, quantities: Int?
        ) -> Unit
    ) {
        this.productStatusListener = receiptStatus
    }

    fun setBranches(branches: List<BranchesDTO.Data.Data>, selectedBranchPosition: Int = 0) {
        this.branches = branches
        this.defaultBranchPosition = selectedBranchPosition
        setSelectedBranch(selectedBranchPosition, branches)

        adapterBranches = BranchesAutoCompleteAdapter(context, branches)
        sheetBinding.branch.setAdapter(adapterBranches)
    }

    private fun setSelectedBranch(
        selectedBranchPosition: Int,
        branches: List<BranchesDTO.Data.Data>
    ) {
        selectedBranchId = selectedBranchPosition
        branchId = branches[selectedBranchPosition].id
        sheetBinding.branch.setText(branches[selectedBranchPosition].name, false)
    }

    private fun setDefaultProductStatus(selectedStatusFilter: ReceiptStatusFilter?) {
        productStatusId = selectedStatusFilter?.value
        sheetBinding.productStatus.setText(
            selectedStatusFilter?.getInvoiceFilterDisplayName(context),
            false
        )
    }

    private fun setDefaultComparisonOption(
        id: Int,
        selectedComparisonOption: String
    ) {
        comparisonOptionId = id
        sheetBinding.comparisonOptions.setText(selectedComparisonOption, false)
    }

    fun show() = bottomSheetDialog?.show()

    fun dismiss() = bottomSheetDialog?.dismiss()
}
