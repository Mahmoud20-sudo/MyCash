package com.codeIn.myCash.ui.home.reports.reports_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentReportsBinding
import com.codeIn.myCash.databinding.LayoutTaxReportBinding
import com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_adapter.ExpensesReportAdapter
import com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_adapter.MainSaleBuyReportAdapter
import com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_adapter.ProductInventoryReportAdapter
import com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_bottom_sheet_adapter.DateTypeAdapter
import com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_bottom_sheet_adapter.InvoiceTypeAdapter
import com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_bottom_sheet_adapter.ReportsTypeAdapter
import com.codeIn.myCash.ui.home.reports.reports_fragment.dialog.repoorts_borrom_sheet.ReportsBottomSheet
import com.codeIn.myCash.ui.home.reports.reports_fragment.view_model.ReportsViewModel
import com.codeIn.myCash.utilities.getReportTitle
import com.codeIn.myCash.utilities.gone
import com.codeIn.common.pager.onScrollEndsVertically
import com.plcoding.reports.data.enums.DateType.Companion.dateTypeList
import com.plcoding.reports.data.enums.InvoiceType.Companion.invoiceTypeList
import com.plcoding.reports.data.enums.ReportsTypes
import com.plcoding.reports.data.enums.ReportsTypes.Companion.reportTypeList
import com.plcoding.reports.data.expense.model.ExpenseModel
import com.plcoding.reports.data.expense.model.ExpenseReport
import com.plcoding.reports.data.inventory.model.InventoryModel
import com.plcoding.reports.data.salesorbuy.model.Report
import com.plcoding.reports.data.salesorbuy.model.SalesOrBuyModel
import com.plcoding.reports.data.tax.model.TaxReportData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    val viewModel: ReportsViewModel by viewModels()

    @Inject
    lateinit var reportsBottomSheet: ReportsBottomSheet

    private var reportsTypeAdapter: ReportsTypeAdapter? = null
    private var dateTypeAdapter: DateTypeAdapter? = null
    private var invoiceTypeAdapter: InvoiceTypeAdapter? = null

    private var mainSaleBuyReportAdapter: MainSaleBuyReportAdapter? = null
    private var expensesReportAdapter: ExpensesReportAdapter? = null
    private var productInventoryReportAdapter: ProductInventoryReportAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        initAdapters()
        initObservables()
        initListeners()

        binding.apply {
            setReportTitleTv(getString(viewModel.getReportType().getReportTitle()))
            binding.selectReportTypeView.setReportFilterTv(
                getString(
                    viewModel.getReportType().getReportTitle()
                )
            )
            reportRv.visibility = View.VISIBLE
        }
    }

    private fun initListeners() {
        binding.apply {
            selectDateView.setUpSelectDateViews(childFragmentManager)
            selectReportTypeView.setDateTypeFilterTv(viewModel.getDateType())

            reportRv.onScrollEndsVertically {
                viewModel.getNext()
            }

            selectDateView.getDateFrom {
                viewModel.setDateFrom(it)
                selectReportTypeView.restDateType()
            }

            selectDateView.getDateTo {
                viewModel.setDateTo(it)
            }

            selectReportTypeView.setOnReportFilterTvClicked {
                reportsBottomSheet.setUpReportFilterTypeViews {
                    reportsBottomSheet.setUpReportTypeAdapter()
                }
            }

            selectReportTypeView.setOnDateTypeFilterTvClicked {
                reportsBottomSheet.setUpDateTypeViews {
                    reportsBottomSheet.setUpDateTypeFilterAdapter()
                }
            }

            selectDateView.setOnFilterIvClicked {
                setOnFilterClicked()
            }
        }
    }

    private fun setOnFilterClicked() =
        reportsBottomSheet.setUpInvoiceTypeViews {
            invoiceTypeAdapter =
                InvoiceTypeAdapter(viewModel.getInvoiceType().value, invoiceTypeList) {
                    reportsBottomSheet.dismissBottomSheet()
                    viewModel.onInvoiceTypeChange(it)
                }
            reportsBottomSheet.setRecyclerAdapter(invoiceTypeAdapter)
        }

    private fun ReportsBottomSheet.setUpReportTypeAdapter() {
        reportsTypeAdapter =
            ReportsTypeAdapter(viewModel.getReportType(), reportTypeList) { type, stringValue ->
                dismissBottomSheet()
                if (type == viewModel.getReportType()) return@ReportsTypeAdapter
                viewModel.onReportChangeType(type)
                binding.apply {
                    setReportTitleTv(stringValue)
                    selectReportTypeView.setReportFilterTv(stringValue)
                }
            }
        setRecyclerAdapter(reportsTypeAdapter)
    }

    private fun ReportsBottomSheet.setUpDateTypeFilterAdapter() {
        dateTypeAdapter =
            DateTypeAdapter(viewModel.getDateType().value, dateTypeList) { type, stringValue ->
                viewModel.onDateChange(type)
                dismissBottomSheet()
                binding.apply {
                    selectDateView.clearDatesInputs()
                    selectReportTypeView.setDateTypeFilterTv(type)
                }
            }
        setRecyclerAdapter(dateTypeAdapter)
    }

    private fun FragmentReportsBinding.setReportTitleTv(value: String) {
        reportBar.titleTv.text = value
    }

    private fun initAdapters() {
        mainSaleBuyReportAdapter = MainSaleBuyReportAdapter()
        productInventoryReportAdapter = ProductInventoryReportAdapter()
        expensesReportAdapter = ExpensesReportAdapter()
    }

    private fun setupAdapter() {
        binding.reportRv.adapter = when (viewModel.getReportType()) {
            ReportsTypes.EXPENSE_REPORTS -> expensesReportAdapter
            ReportsTypes.PRODUCT_INVENTORY_REPORTS -> productInventoryReportAdapter
            else -> mainSaleBuyReportAdapter
        }
    }

    private fun initObservables() {
        viewModel.apply {
            salesOrBuyList.observe(viewLifecycleOwner) { list ->
                handleSaleOrBuyResponses(list)
            }

            expenses.observe(viewLifecycleOwner) { response ->
                handleExpenseResponses(response)
            }

            inventoryReports.observe(viewLifecycleOwner) { response ->
                handleInventoryResponses(response)
            }

            taxReport.observe(viewLifecycleOwner) { response ->
                handleTaxResponses(response)
            }

            isLoading.observe(viewLifecycleOwner) {
                binding.pagingProgressBar.isVisible = it && viewModel.page > 1
            }

            isPaginationLoading.observe(viewLifecycleOwner) {
                binding.animationView.isVisible = it && viewModel.page == 1
            }
        }
    }

    private fun handleSaleOrBuyResponses(data: ArrayList<SalesOrBuyModel>) {
        setUpReportView(viewModel.report.value)
        binding.apply {
            if (data.isEmpty())
                mainSaleBuyReportAdapter?.clear()
            else
                mainSaleBuyReportAdapter?.saveData(data)
            handleEmptyStateVisibility(data.isNotEmpty())
            setCountTv(viewModel.total)
            setupAdapter()
        }
    }

    private fun handleInventoryResponses(data: ArrayList<InventoryModel>) {
        binding.apply {
            if (data.isEmpty())
                productInventoryReportAdapter?.clear()
            else
                productInventoryReportAdapter?.saveData(data)
            handleEmptyStateVisibility(data.isNotEmpty())
            setCountTv(viewModel.total)
            setupAdapter()
        }
    }

    private fun handleExpenseResponses(data: ArrayList<ExpenseModel>) {
        handleExpensesReportView(viewModel.expensesReport.value)
        binding.apply {
            if (data.isEmpty())
                expensesReportAdapter?.clear()
            else
                expensesReportAdapter?.saveData(data)
            handleEmptyStateVisibility(data.isNotEmpty())
            setCountTv(viewModel.total)
            setupAdapter()
        }
    }

    private fun handleTaxResponses(response: TaxReportData) {
        binding.setUpTaxViews(response)
    }

    private fun FragmentReportsBinding.handleEmptyStateVisibility(isShow: Boolean) {
        //isShow
        //NOT_TAX and isShow
        reportRv.isVisible =
            viewModel.itemCount > 0 && viewModel.getReportType() != ReportsTypes.TAX_REPORTS
        //TAX and isShow
        taxReportIL.root.isVisible = isShow && viewModel.getReportType() == ReportsTypes.TAX_REPORTS

        emptyStateIv.isVisible = if(viewModel.getReportType() == ReportsTypes.TAX_REPORTS) !isShow else viewModel.itemCount == 0

        binding.reportBar.countTv.isVisible = !emptyStateIv.isVisible
        //
        binding.selectDateView.showFilter(viewModel.getReportType() != ReportsTypes.EXPENSE_REPORTS)
        //reportview shown in all expect inventory and tax
        val reportViewVisibility =
            viewModel.getReportType() == ReportsTypes.TAX_REPORTS || viewModel.getReportType() == ReportsTypes.PRODUCT_INVENTORY_REPORTS
        binding.reportView.isVisible = !reportViewVisibility && !emptyStateIv.isVisible
    }

    private fun FragmentReportsBinding.setUpTaxViews(data: TaxReportData?) =
        binding.taxReportIL.apply {
            handleEmptyStateVisibility(data != null)
            setUpTaxReportViews(data)
            binding.reportBar.countTv.gone()
        }

    private fun LayoutTaxReportBinding.setUpTaxReportViews(data: TaxReportData?) {
        val saleInvoice = data?.saleInvoices ?: return
        val buyInvoice = data.buyInvoices
        val expense = data.expenses
        totalSaleIncludesTaxValueTv.text =
            getString(R.string.sar_place_holder, saleInvoice.totalPrice)
        totalSaleWithOutTaxValueTv.text =
            getString(R.string.sar_place_holder, saleInvoice.totalWithoutTax)
        totalSalesValueTv.text = getString(R.string.sar_place_holder, saleInvoice.totalTax)
        totalBuyIncludesTaxValueTv.text =
            getString(R.string.sar_place_holder, buyInvoice?.totalPrice)
        totalBuyWithOutTaxValueTv.text =
            getString(R.string.sar_place_holder, buyInvoice?.totalWithoutTax)
        totalBuysTaxValueTv.text = getString(R.string.sar_place_holder, buyInvoice?.totalTax)
        totalExpensesIncludesTaxValueTv.text =
            getString(R.string.sar_place_holder, expense?.totalPrice)
        totalExpensesWithOutTaxValueTv.text =
            getString(R.string.sar_place_holder, expense?.totalWithoutTax)
        totalExpensesTaxValueTv.text = getString(R.string.sar_place_holder, expense?.totalTax)
        totalRequiredTaxValueTv.text =
            getString(R.string.sar_place_holder, data.taxTotalPrice)
        setOnTaxPrintClicked(data)
//        viewModel.clearTaxReportState()
    }

    private fun LayoutTaxReportBinding.setOnTaxPrintClicked(data: TaxReportData?) =
        printTaxReportTv.setOnClickListener {
            //TODO HANDLE PRINT TAX HERE
        }

    @SuppressLint("SetTextI18n")
    private fun handleExpensesReportView(report: ExpenseReport?) = binding.apply {
        //NOT NEEDED TO BE HANDLED IN OTHER REPORTS
        if (viewModel.getReportType() != ReportsTypes.EXPENSE_REPORTS) return@apply
        handleEmptyStateVisibility(report?.totalPrice != 0.0)
        totalReportWithOutTaxTv.text = getString(R.string.quantity_of_expenses)
        totalReportWithTaxTv.text = getString(R.string.total_expenses)
        totalReportWithOutTaxValueTv.text = "${viewModel.total}"
        totalReportWithTaxValueTv.text =
            getString(R.string.sar_place_holder, report?.totalPriceWithTax)
    }

    private fun setUpReportView(report: Report?) = binding.apply {
        //NOT NEEDED TO BE HANDLED IN OTHER REPORTS
        if (viewModel.getReportType() == ReportsTypes.TAX_REPORTS || viewModel.getReportType() == ReportsTypes.EXPENSE_REPORTS) return@apply
        handleEmptyStateVisibility(report?.totalPrice != 0.0)
        totalReportWithOutTaxTv.text = getString(R.string.total_invoice_without_tax)
        totalReportWithTaxTv.text = getString(R.string.total_invoice_with_tax)
        totalReportWithOutTaxValueTv.text =
            getString(R.string.sar_place_holder, report?.totalWithoutTax)
        totalReportWithTaxValueTv.text =
            getString(R.string.sar_place_holder, report?.totalPriceWithTax)
    }

    private fun setCountTv(value: Int?) {
        value?.let {
            binding.reportBar.countTv.text = value.toString()
        }
    }
}