package com.codeIn.myCash.ui.home.reports.reportsFragment


import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.codeIn.common.domain.ErrorEntity
import com.codeIn.common.offline.Constants
import com.codeIn.common.pager.onScrollEndsVertically
import com.codeIn.common.util.gone
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentReportsBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.ui.base.BaseFragment
import com.codeIn.myCash.ui.home.reports.adapters.PostpaidReportAdapter
import com.codeIn.myCash.ui.home.reports.adapters.ProductsQuantitiesReportAdapter
import com.codeIn.myCash.ui.home.reports.adapters.ReturnReportAdapter
import com.codeIn.myCash.ui.home.reports.adapters.SalesInvoicesAdapter
import com.codeIn.myCash.ui.home.reports.adapters.SalesReportAdapter
import com.codeIn.myCash.ui.home.reports.adapters.SalesReportAdapter.OnItemClickListener
import com.codeIn.myCash.ui.home.reports.customViews.SelectDateView
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.currencyFormatter
import com.codeIn.myCash.utilities.getErrorMessage
import com.codeIn.myCash.utilities.goneIf
import com.codeIn.myCash.utilities.visible
import com.codeIn.myCash.utilities.visibleIf
import com.plcoding.reports.data.enums.ReportsFilterTypes
import com.plcoding.reports.data.postPaid.model.beans.Postpaid
import com.plcoding.reports.data.postPaid.model.response.PostpaidResponse
import com.plcoding.reports.data.productsQuantitiesReport.model.beans.ProductsQuantity
import com.plcoding.reports.data.productsQuantitiesReport.model.response.ProductsQuantityResponse
import com.plcoding.reports.data.returnReport.model.beans.ReturnInvoice
import com.plcoding.reports.data.returnReport.model.response.ReturnInvoicesReportResponse
import com.plcoding.reports.data.salesReport.model.beans.SalesReport
import com.plcoding.reports.data.salesorbuy.model.Report
import com.plcoding.reports.data.salesorbuy.model.SaleOrBuyData
import com.plcoding.reports.data.salesorbuy.model.SalesOrBuyModel
import com.plcoding.reports.data.state.ReportsResponseResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class ReportsFragment : BaseFragment<FragmentReportsBinding>(FragmentReportsBinding::inflate) {

    private val viewModel: ReportsViewModel by viewModels()

    @Inject
    lateinit var salesReportAdapter: SalesReportAdapter

    @Inject
    lateinit var salesInvoicesAdapter: SalesInvoicesAdapter

    @Inject
    lateinit var returnReportAdapter: ReturnReportAdapter

    @Inject
    lateinit var postpaidReportAdapter: PostpaidReportAdapter

    @Inject
    lateinit var productsQuantityAdapter: ProductsQuantitiesReportAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.requestBranches()
        collectBranchData()
        collectReportData()
        observeLoadingState()
        initializeUI()
    }

    private fun collectBranchData() {
        lifecycleScope.launch {
            viewModel.branches.collectLatest { branches ->
                if (branches.isNotEmpty()) {
                    setupBranchFilters(branches)
                    setupDateViews()
                    clearDatesFilters()
                }
            }
        }
    }

    private fun setupBranchFilters(branches: List<BranchesDTO.Data.Data>) {
        with(binding.containerOfFilters) {
            setBranches(
                branches,
                viewModel.savableState.value.selectedReportPosition,
                viewModel.savableState.value.selectedBranchPosition
            )
            setSelectFilterTypes(viewModel)
        }
    }

    private fun setupDateViews() {
        setupSelectDateViews(
            binding.containerOfFilters.binding.salesReportsFilter.selectDate,
            binding.containerOfFilters.binding.salesAndReturnedInvoicesFilter.selectDate,
        )
    }

    private fun setupSelectDateViews(vararg dateViews: SelectDateView) {
        dateViews.forEach { dateView ->
            dateView.setUpSelectDateViews(childFragmentManager)
        }
    }

    private fun clearDatesFilters() {
        with(binding.containerOfFilters) {
            onClearDatesSalesReports()
            onClearDateForSalesAndReturned()
        }
    }

    private fun initializeUI() {
        onClicks()
        onVisibilitySummeryUIControl()
        onScrollReportsEnd()
    }

    private fun observeLoadingState() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pagingProgressBar.isVisible = it && viewModel.page > 1
        }
    }

    private fun collectReportData() {
        lifecycleScope.launch {
            viewModel.reports.collectLatest { response ->
                when (response) {
                    is ReportsResponseResult.SuccessPostpaidReport -> handlePostpaidReport(response.result)
                    is ReportsResponseResult.SuccessSalesReport -> handleSalesReport(response.data)
                    is ReportsResponseResult.SuccessSalesInvoices -> handleSalesInvoices(response.data)
                    is ReportsResponseResult.SuccessReturnReport -> handleReturnReport(response.data)
                    is ReportsResponseResult.SuccessProductsQuantity -> handleProductsQuantity(
                        response.data
                    )

                    is ReportsResponseResult.ServerError -> handleServerError(response.error)
                    ReportsResponseResult.Loading -> handleLoading()
                    ReportsResponseResult.UnAuthorized -> handleUnauthorized()
                    ReportsResponseResult.UnknownError -> handleUnknownError()
                }
            }
        }
    }

    private fun handleProductsQuantity(productsQuantity: ProductsQuantityResponse?) {
        binding.animationView.gone()
        binding.reportRv.visible()

        productsQuantity?.let {
            initProductsQuantity(it.data.data)
            it.data.pagination.count?.let { count ->
                binding.containerOfFilters.binding.resultOfSearch.visible()
                binding.containerOfFilters.binding.countOfReceipt.visible()
                binding.containerOfFilters.binding.countOfReceipt.text = String.format(
                    Locale.ENGLISH,
                    "%s %s",
                    count,
                    resources.getString(R.string.product)
                )
            }
        }
    }

    private fun handlePostpaidReport(result: PostpaidResponse) {
        Timber.d("TEst te st tst -> ${result.data}")
        binding.animationView.gone()
        binding.reportRv.visible()
        binding.emptyReports.visibleIf(result.data.data.isEmpty())
        initPostpaidReport(result.data.data)

        result.data.pagination.count?.let { count ->
            binding.containerOfFilters.binding.resultOfSearch.visible()
            binding.containerOfFilters.binding.countOfReceipt.visible()
            binding.containerOfFilters.binding.countOfReceipt.text = String.format(
                Locale.ENGLISH,
                "%s %s",
                count,
                resources.getString(R.string.receipt)
            )
        }
    }

    private fun handleSalesReport(salesReport: List<SalesReport>?) {
        binding.containerOfFilters.binding.resultOfSearch.gone()
        binding.containerOfFilters.binding.countOfReceipt.gone()

        binding.animationView.gone()
        binding.reportRv.visible()

        salesReport?.let {
            binding.emptyReports.visibleIf(it.isEmpty())
            initSalesReport(it)
        }
    }

    private fun handleSalesInvoices(salesInvoices: SaleOrBuyData?) {
        binding.containerOfFilters.binding.countOfReceipt.gone()
        binding.containerOfFilters.binding.resultOfSearch.gone()
        binding.containerOfFilters.binding.printReport.visible()
        binding.animationView.gone()
        binding.reportRv.visible()

        salesInvoices?.invoices?.let { invoices ->
            binding.emptyReports.visibleIf(invoices.data.isEmpty())
            initSalesInvoices(invoices.data)
            invoices.pagination.count?.let { count ->
                populateSummerySalesInvoices(salesInvoices.report, count)
            }
        }
    }

    private fun handleReturnReport(returnReport: ReturnInvoicesReportResponse.ReturnInvoicesReportBody?) {
        binding.containerOfFilters.binding.countOfReceipt.gone()
        binding.containerOfFilters.binding.resultOfSearch.gone()
        binding.containerOfFilters.binding.printReport.visible()
        binding.animationView.gone()
        binding.reportRv.visible()

        returnReport?.let {
            binding.emptyReports.visibleIf(it.invoices.data.isEmpty())
            initReturnReport(it.invoices.data)
            it.invoices.pagination.count?.let { count ->
                populateSummeryReturnInvoices(count, it.totalReturnedAmount)
            }
        }
    }

    private fun handleServerError(error: ErrorEntity) {
        binding.containerOfFilters.binding.countOfReceipt.gone()
        binding.containerOfFilters.binding.resultOfSearch.gone()
        binding.animationView.gone()
        binding.reportRv.gone()

        CustomToaster.show(
            context = requireContext(),
            message = error.getErrorMessage(requireContext()),
            isSuccess = false
        )
    }

    private fun handleLoading() {
        binding.containerOfFilters.binding.countOfReceipt.gone()
        val flag = viewModel.page == 1 && !viewModel.isLastPage
        binding.animationView.isVisible = flag
        binding.emptyReports.goneIf(flag)
    }

    private fun handleUnauthorized() {
        binding.containerOfFilters.binding.countOfReceipt.gone()
        binding.animationView.gone()
        viewModel.prefs.putValue(Constants.getToken(), "")
        onLogout()
    }

    private fun handleUnknownError() {
        binding.containerOfFilters.binding.countOfReceipt.gone()
        binding.animationView.gone()
        binding.reportRv.gone()

        CustomToaster.show(
            context = requireContext(),
            message = resources.getString(R.string.unknown_error),
            isSuccess = false
        )
    }

    private fun onScrollReportsEnd() {
        binding.reportRv.onScrollEndsVertically {
            viewModel.getNext()
        }
    }

    private fun onClicks() {
        binding.apply {
            summarySalesContainer.setOnClickListener {
                isOpenSummerySales(binding.salesInvoiceRow.isVisible.not())
            }

            containerOfFilters.binding.printReport.setOnClickListener {
                viewModel.printReport()
            }
        }
    }

    private fun initPostpaidReport(returnInvoices: List<Postpaid>) {
        binding.reportRv.apply {
            adapter = postpaidReportAdapter
            postpaidReportAdapter.submitPostpaidReport(returnInvoices)
            postpaidReportAdapter.onPostPaidReportClickListener(object :
                PostpaidReportAdapter.OnPostPaidReportClickListener {
                override fun onPostPaidReportClick(receiptId: Int) {
                    // todo asmaa we should go to the receipt details
                }
            })
        }
    }

    private fun initProductsQuantity(productsQuantities: List<ProductsQuantity>) {
        binding.emptyReports.visibleIf(productsQuantities.isEmpty())

        binding.reportRv.apply {
            adapter = productsQuantityAdapter
            productsQuantityAdapter.submitProductsQuantities(productsQuantities)
            productsQuantityAdapter.onItemClickListener(object :
                ProductsQuantitiesReportAdapter.OnItemClickListener {
                override fun onEditProductClick(productId: Int) {
                    findNavController().navigate(
                        ReportsFragmentDirections.actionNavigationReportsFragmentToNavigationUpdateProductFragment(
                            productId
                        )
                    )
                }
            })
        }
    }

    private fun initSalesInvoices(salesInvoices: List<SalesOrBuyModel>) {
        binding.reportRv.apply {
            adapter = salesInvoicesAdapter
            salesInvoicesAdapter.submitInvoices(salesInvoices)
            salesInvoicesAdapter.onItemClickListener(object : SalesInvoicesAdapter
            .OnItemClickListener {
                override fun onItemClick(invoiceNumber: Int, position: Int) {
                    viewModel.saveVerticalPosition(position)
                    val action = ReportsFragmentDirections
                        .actionNavigationReportsFragmentToReportsInvoiceFragment(invoiceNumber.toString())
                    findNavController().navigate(action)
                }
            }
            )

            scrollToPosition(viewModel.savableState.value.selectedVerticalPosition)
        }
    }

    private fun initSalesReport(salesReport: List<SalesReport>) {
        binding.reportRv.apply {
            adapter = salesReportAdapter
            salesReportAdapter.submitInvoices(salesReport)
            salesReportAdapter.onItemClickListener(object : OnItemClickListener {
                override fun onItemClick(startDate: String, endDate: String, position: Int) {
                    viewModel.saveVerticalPosition(position)
                    val action =
                        ReportsFragmentDirections.actionNavigationReportsFragmentToSalesReportDetailFragment(
                            startDate,
                            endDate,
                            binding.containerOfFilters.branchId
                        )
                    findNavController().navigate(action)
                }
            })

            scrollToPosition(viewModel.savableState.value.selectedVerticalPosition)
        }
    }

    private fun initReturnReport(returnInvoice: List<ReturnInvoice>) {
        binding.reportRv.apply {
            adapter = returnReportAdapter
            returnReportAdapter.submitInvoices(returnInvoice)
            returnReportAdapter.onItemClickListener(object :
                ReturnReportAdapter.OnItemClickListener {
                override fun onReturnReportClick(invoiceNumber: String, position: Int) {
                    viewModel.saveVerticalPosition(position)
                    val action = ReportsFragmentDirections
                        .actionNavigationReportsFragmentToReportsInvoiceFragment(invoiceNumber)
                    findNavController().navigate(action)
                }
            })

            scrollToPosition(viewModel.savableState.value.selectedVerticalPosition)
        }
    }

    private fun populateSummerySalesInvoices(invoice: Report?, salesInvoiceNum: Int) {
        invoice?.let {
            binding.salesInvoiceNumber.text = salesInvoiceNum.toString()
            binding.salesTotalWithoutTax.text =
                currencyFormatter(it.totalWithoutTax, viewModel.currency)
            binding.salesTotalTax.text =
                currencyFormatter(it.totalTax, viewModel.currency)
            binding.totalSales.text = currencyFormatter(it.totalPrice, viewModel.currency)
        }

    }

    private fun populateSummeryReturnInvoices(count: Int, total: Double) {
        binding.returnedInvoiceNumber.text = count.toString()
        binding.totalReturned.text = currencyFormatter(
            total,
            viewModel.currency
        )
    }

    private fun onVisibilitySummeryUIControl() {
        lifecycleScope.launch {
            viewModel.summeryUIControl.collect { result: ReportsFilterTypes ->
                with(binding) {
                    when (result) {
                        ReportsFilterTypes.SALES_INVOICES -> {
                            summarySalesContainer.visible()
                            summaryReturnedContainer.gone()
                        }

                        ReportsFilterTypes.RETURN_INVOICES -> {
                            summaryReturnedContainer.visible()
                            summarySalesContainer.gone()
                        }

                        ReportsFilterTypes.SALES_REPORTS, ReportsFilterTypes.PRODUCTS_QUANTITY,
                        ReportsFilterTypes.POSTPAID -> {
                            summaryReturnedContainer.gone()
                            summarySalesContainer.gone()
                        }


                    }
                }
            }
        }
    }

    private fun isOpenSummerySales(isOpen: Boolean) {
        binding.salesInvoiceRow.isVisible = isOpen
        binding.salesTotalWithoutTaxRow.isVisible = isOpen
        binding.salesTotalTaxRow.isVisible = isOpen
        binding.openAndCloseSummeryIV.setImageResource(
            if (isOpen) R.drawable.ic_arrow_down_main_black
            else R.drawable.ic_arrow_up
        )
    }
}