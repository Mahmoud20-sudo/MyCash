package com.codeIn.myCash.ui.home.reports.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.codeIn.common.data.ReceiptStatusFilter
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ReportsFiltersBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.ui.home.reports.adapters.BranchesAdapter
import com.codeIn.myCash.ui.home.reports.adapters.PerChunkFiltersAdapter
import com.codeIn.myCash.ui.home.reports.adapters.TopLevelReportsFiltersAdapter
import com.codeIn.myCash.ui.home.reports.bottomSheets.PostpaidFilterBS
import com.codeIn.myCash.ui.home.reports.bottomSheets.ProductsQuantityFilterBS
import com.codeIn.myCash.ui.home.reports.bottomSheets.ReturnInvoiceFilterBS
import com.codeIn.myCash.ui.home.reports.bottomSheets.SalesInvoiceReportBS
import com.codeIn.myCash.ui.home.reports.bottomSheets.SalesReportsFilterBS
import com.codeIn.myCash.utilities.gone
import com.codeIn.myCash.utilities.visible
import com.plcoding.reports.data.enums.PerChunk
import com.plcoding.reports.data.enums.ReportsFilterTypes
import com.plcoding.reports.data.reportRequest.ReportRequest

class ReportsFilterTypes(context: Context, attributes: AttributeSet? = null) :
    FrameLayout(context, attributes) {

    private var selectedBranchPosition: Int = 0
    private lateinit var onSelectFilterTypes: OnSelectFilterTypes
    private val _binding by lazy {
        ReportsFiltersBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val topLevelReportsFilters by lazy { ReportsFilterTypes.entries }
    private lateinit var chunksAdapter: PerChunkFiltersAdapter
    private lateinit var reportsTypeAdapter: TopLevelReportsFiltersAdapter
    private lateinit var branchesAdapter: BranchesAdapter
    private lateinit var postpaidBS: PostpaidFilterBS
    private lateinit var productsQuantityBS: ProductsQuantityFilterBS
    private lateinit var salesReportsFilterBS: SalesReportsFilterBS
    private lateinit var salesInvoiceReportsFilterBS: SalesInvoiceReportBS
    private lateinit var returnInvoiceReportsFilterBS: ReturnInvoiceFilterBS

    private var reportsFilterType = ReportsFilterTypes.SALES_REPORTS
    private var perChunkType: PerChunk? = null
    private var startDate: String? = null
    private var endDate: String? = null
    var branchId: Int = -1

    val binding = _binding

    init {
        setupAdapters()
        initViews()
        setupListeners()
    }

    private fun setupAdapters() {
        reportsTypeAdapter = TopLevelReportsFiltersAdapter(topLevelReportsFilters).apply {
            setTopLevelReportsListener(object :
                TopLevelReportsFiltersAdapter.TopLevelReportsListener {
                override fun onItemClicked(reportsFilterTypes: ReportsFilterTypes) {
                    reportsFilterType = reportsFilterTypes
                    resetDateFilters()
                    applyDefaultFilterViewVisibility(reportsFilterTypes)
                    checkReportType(reportsFilterTypes)
                }
            })
        }

        branchesAdapter = BranchesAdapter().apply {
            onBranchClickListener(object : BranchesAdapter.OnBranchClickListener {
                override fun onBranchClick(branch: BranchesDTO.Data.Data, position: Int) {
                    branchId = branch.id
                    updateBranchName(branch.name)
                    triggerFilterSelection(position)
                }
            })
        }

        chunksAdapter = PerChunkFiltersAdapter(PerChunk.entries).apply {
            setPerChunkClicked(object : PerChunkFiltersAdapter.PerChunkClickListener {
                override fun onPerChunkClicked(perChunk: PerChunk) {
                    perChunkType = perChunk
                    triggerFilterWithChunk()
                }
            })
        }
    }

    private fun checkReportType(reportsFilterTypes: ReportsFilterTypes) {
        when (reportsFilterTypes) {
            ReportsFilterTypes.PRODUCTS_QUANTITY -> {
                onSelectFilterTypes.onSelectReportType(
                    ReportRequest(
                        typeId = reportsFilterType.id,
                        branchPosition = selectedBranchPosition,
                        branchId = branchId,
                        productStatusId = ReceiptStatusFilter.ALL.value,
                        comparisonOptionId = 0,
                    )
                )
            }

            ReportsFilterTypes.POSTPAID -> {
                onSelectFilterTypes.onSelectReportType(
                    ReportRequest(
                        typeId = reportsFilterType.id,
                        branchPosition = selectedBranchPosition,
                        branchId = branchId,
                        receiptStatusId = ReceiptStatusFilter.ALL.value,
                    )
                )
            }

            else -> {
                onSelectFilterTypes.onSelectReportType(
                    ReportRequest(
                        typeId = reportsFilterType.id,
                        branchPosition = selectedBranchPosition,
                        branchId = branchId
                    )
                )
            }
        }
    }

    private fun setupListeners() {
        _binding.salesReportsFilter.selectDate.apply {
            getDateFrom { startDate = it }
            getDateTo { endDate = it; triggerFilterWithDate() }
        }

        _binding.salesAndReturnedInvoicesFilter.selectDate.apply {
            getDateFrom { startDate = it }
            getDateTo { endDate = it; triggerFilterWithDate() }
        }

        _binding.salesReportsFilter.perDateFilterRG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.dateRB -> showDateFilter()
                R.id.perChunkRB -> showPerChunkFilter()
            }
        }

        _binding.filter.setOnClickListener {
            showRelevantBottomSheet()
        }
        _binding.printReport.setOnClickListener {

        }


        salesReportsFilterBS.getReceiptStatus { branchId, selectedBranchPosition, startDate, endDate, perChunk ->
            this.selectedBranchPosition = selectedBranchPosition
            onSelectFilterTypes.onSelectReportType(
                ReportRequest(
                    typeId = reportsFilterType.id,
                    branchId = branchId,
                    branchPosition = selectedBranchPosition,
                    startDate = startDate,
                    endDate = endDate,
                    interval = perChunk
                )
            )
        }
        salesInvoiceReportsFilterBS.requestSalesInvoices { branchId, selectedBranchPosition, startDate, endDate ->
            this.selectedBranchPosition = selectedBranchPosition
            onSelectFilterTypes.onSelectReportType(
                ReportRequest(
                    typeId = reportsFilterType.id,
                    branchId = branchId,
                    branchPosition = selectedBranchPosition,
                    startDate = startDate,
                    endDate = endDate
                )
            )
        }
        returnInvoiceReportsFilterBS.requestReturnInvoices { branchId, selectedBranchPosition, startDate, endDate ->
            this.selectedBranchPosition = selectedBranchPosition
            onSelectFilterTypes.onSelectReportType(
                ReportRequest(
                    typeId = reportsFilterType.id,
                    branchId = branchId,
                    branchPosition = selectedBranchPosition,
                    startDate = startDate,
                    endDate = endDate
                )
            )
        }

        postpaidBS.getReceiptStatus { branchId, selectedBranchPosition, dateFrom, dateTo, invoiceNum, receiptNum, receiptStatusId ->
            this.selectedBranchPosition = selectedBranchPosition
            onSelectFilterTypes.onSelectReportType(
                ReportRequest(
                    typeId = reportsFilterType.id,
                    branchId = branchId ?: -1,
                    branchPosition = selectedBranchPosition,
                    startDate = dateFrom,
                    endDate = dateTo,
                    invoiceNum = invoiceNum,
                    receiptNum = receiptNum,
                    receiptStatusId = receiptStatusId
                )
            )
        }

        productsQuantityBS.getProductStatus { branchId: Int, selectedBranchPosition: Int, productStatusId: Int?, comparisonOptionId: Int?, quantities: Int? ->
            this.selectedBranchPosition = selectedBranchPosition

            onSelectFilterTypes.onSelectReportType(
                ReportRequest(
                    typeId = reportsFilterType.id,
                    branchPosition = selectedBranchPosition,
                    branchId = branchId,
                    productStatusId = productStatusId,
                    comparisonOptionId = comparisonOptionId,
                    quantities = quantities
                )
            )
        }

    }

    private fun initViews() {
        postpaidBS = PostpaidFilterBS(context)
        productsQuantityBS = ProductsQuantityFilterBS(context)
        salesReportsFilterBS = SalesReportsFilterBS(context)
        salesInvoiceReportsFilterBS=SalesInvoiceReportBS(context)
        returnInvoiceReportsFilterBS=ReturnInvoiceFilterBS(context)
    }

    private fun showDateFilter() {
        perChunkType = null
        _binding.salesReportsFilter.selectDate.visible()
        _binding.salesReportsFilter.perChunkRv.gone()
        triggerFilterWithDate()
    }

    private fun showPerChunkFilter() {
        _binding.salesReportsFilter.perChunkRv.adapter = chunksAdapter
        _binding.salesReportsFilter.perChunkRv.visible()
        _binding.salesReportsFilter.selectDate.gone()
    }

    private fun showRelevantBottomSheet() {
        when (reportsFilterType) {
            ReportsFilterTypes.SALES_REPORTS -> salesReportsFilterBS.show()
            ReportsFilterTypes.SALES_INVOICES -> salesInvoiceReportsFilterBS.show()
            ReportsFilterTypes.RETURN_INVOICES -> {returnInvoiceReportsFilterBS.show()}
            ReportsFilterTypes.POSTPAID -> postpaidBS.show()
            ReportsFilterTypes.PRODUCTS_QUANTITY -> productsQuantityBS.show()
        }
    }

    private fun updateBranchName(branchName: String?) {
        _binding.salesReportsFilter.branchName.text = branchName
        _binding.salesAndReturnedInvoicesFilter.branchName.text = branchName
    }

    private fun triggerFilterSelection(branchPosition: Int = 0) {
        val isPerChunkSelected = _binding.salesReportsFilter.perChunkRB.isChecked

        onSelectFilterTypes.onSelectReportType(
            ReportRequest(
                typeId = reportsFilterType.id,
                branchId = branchId,
                branchPosition = branchPosition,
                interval = perChunkType,
                startDate = if (isPerChunkSelected) null else startDate,
                endDate = if (isPerChunkSelected) null else endDate
            )
        )
    }

    private fun triggerFilterWithDate() {
        onSelectFilterTypes.onSelectReportType(
            ReportRequest(
                typeId = reportsFilterType.id,
                branchPosition = selectedBranchPosition,
                branchId = branchId,
                startDate = startDate,
                endDate = endDate,
            )
        )
    }

    private fun triggerFilterWithChunk() {
        onSelectFilterTypes.onSelectReportType(
            ReportRequest(
                typeId = reportsFilterType.id,
                branchPosition = selectedBranchPosition,
                branchId = branchId,
                interval = perChunkType
            )
        )
    }

    fun setBranches(
        branches: List<BranchesDTO.Data.Data>,
        selectedReportPosition: Int,
        selectedBranchPosition: Int
    ) {
        this.selectedBranchPosition = selectedBranchPosition
        branchId = if (selectedBranchPosition != 0) branches[selectedBranchPosition].id
        else branches.first().id

        updateBranchName(branches.first().name)

        branchesAdapter.submitBranches(branches, selectedBranchPosition)

        salesReportsFilterBS.setBranches(branches, selectedBranchPosition)
        productsQuantityBS.setBranches(branches, selectedBranchPosition)
        postpaidBS.setBranches(branches, selectedBranchPosition)
        salesInvoiceReportsFilterBS.setBranches(branches, selectedBranchPosition)
        returnInvoiceReportsFilterBS.setBranches(branches, selectedBranchPosition)

        reportsTypeAdapter.submitSelectedItem(selectedReportPosition)
        _binding.topLevelReportsTypesRv.adapter = reportsTypeAdapter
        _binding.topLevelReportsTypesRv.scrollToPosition(selectedReportPosition)
    }

    fun setSelectFilterTypes(onSelectFilterTypes: OnSelectFilterTypes) {
        this.onSelectFilterTypes = onSelectFilterTypes
    }

    private fun applyDefaultFilterViewVisibility(filterType: ReportsFilterTypes) {
        when (filterType) {
            ReportsFilterTypes.SALES_REPORTS -> {
//                _binding.salesAndReturnedInvoicesFilter.root.gone()
//                _binding.salesReportsFilter.root.visible()

                _binding.printReport.visible()
            }

            ReportsFilterTypes.SALES_INVOICES, ReportsFilterTypes.RETURN_INVOICES -> {
//                _binding.salesReportsFilter.root.gone()
//                _binding.salesAndReturnedInvoicesFilter.root.visible()

                _binding.printReport.visible()
            }

            ReportsFilterTypes.POSTPAID, ReportsFilterTypes.PRODUCTS_QUANTITY -> {
                _binding.printReport.gone()
            }
        }
    }

    fun onClearDatesSalesReports() {
        _binding.salesReportsFilter.selectDate.onClearDatesClicked {
            clearDates()
            triggerFilterSelection()
        }
    }

    fun onClearDateForSalesAndReturned() {
        _binding.salesAndReturnedInvoicesFilter.selectDate.onClearDatesClicked {
            clearDates()
            triggerFilterSelection()
        }
    }

    private fun clearDates() {
        startDate = null
        endDate = null
    }

    private fun resetDateFilters() {
        // Reset date filters
        _binding.salesReportsFilter.selectDate.clearDatesInputs()
        _binding.salesAndReturnedInvoicesFilter.selectDate.clearDatesInputs()
        clearDates()
    }


    interface OnSelectFilterTypes {
        fun onSelectReportType(request: ReportRequest)
    }
}
