package com.codeIn.myCash.ui.home.reports.salesDetailFragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.codeIn.common.offline.Constants
import com.codeIn.common.pager.onScrollEndsVertically
import com.codeIn.common.util.gone
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.FragmentSalesReportDetailBinding
import com.codeIn.myCash.ui.base.BaseFragment
import com.codeIn.myCash.ui.home.reports.adapters.SalesInvoicesAdapter
import com.codeIn.myCash.utilities.CustomToaster
import com.codeIn.myCash.utilities.currencyFormatter
import com.codeIn.myCash.utilities.goneIf
import com.codeIn.myCash.utilities.visibleIf
import com.plcoding.reports.data.salesorbuy.model.Report
import com.plcoding.reports.data.reportRequest.ReportRequest
import com.plcoding.reports.data.state.ReportsResponseResult
import com.plcoding.reports.data.salesorbuy.model.SalesOrBuyModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SalesReportDetailFragment :
    BaseFragment<FragmentSalesReportDetailBinding>(FragmentSalesReportDetailBinding::inflate) {

    private val viewModel: SalesReportDetailVM by viewModels()
    private val navArgs: SalesReportDetailFragmentArgs by navArgs()

    @Inject
    lateinit var salesInvoicesAdapter: SalesInvoicesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getSalesReportDetail(
            ReportRequest(
                startDate = navArgs.startDate,
                endDate = navArgs.endDate,
                branchId = navArgs.branchId
            )
        )
        lifecycleScope.launch {
            viewModel.salesInvoices.collectLatest { response: ReportsResponseResult ->
                when (response) {
                    is ReportsResponseResult.SuccessSalesInvoices -> {
                        binding.animationView.gone()

                        response.data?.invoices?.let { invoices ->
                            binding.emptyReports.visibleIf(invoices.data.isEmpty())
                            binding.summarySalesContainer.visibleIf(invoices.data.isNotEmpty())

                            initSalesInvoices(invoices.data)

                            invoices.pagination.count?.let { count ->
                                populateSummerySalesInvoices(response.data?.report, count)
                            }
                        }
                    }

                    is ReportsResponseResult.ServerError -> {
                        binding.animationView.gone()
                    }

                    ReportsResponseResult.Loading -> {
                        val flag = viewModel.page == 1 && !viewModel.isLastPage
                        binding.animationView.isVisible = flag
                        binding.emptyReports.goneIf(flag)
                    }

                    ReportsResponseResult.UnAuthorized -> {
                        viewModel.prefs.putValue(Constants.getToken(), "")
                        onLogout()
                    }

                    ReportsResponseResult.UnknownError -> {
                        binding.animationView.gone()
                        CustomToaster.show(
                            context = requireContext(),
                            message = resources.getString(R.string.unknown_error),
                            isSuccess = false
                        )
                    }
                    else -> {
                        binding.animationView.gone()
                    }
                }
            }
        }

        binding.reportRv.onScrollEndsVertically {
            viewModel.getNext()
        }
        onClicks()
    }

    private fun onClicks() {
        binding.backArrow.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.summarySalesContainer.setOnClickListener {
            isOpenSummerySales(binding.salesInvoiceRow.isVisible.not())
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

    private fun populateSummerySalesInvoices(invoice: Report?, salesInvoiceNum: Int) {
        invoice?.let {
            binding.salesInvoiceNumber.text = salesInvoiceNum.toString()
            binding.salesTotalWithoutTax.text = currencyFormatter(it.totalWithoutTax, viewModel.currency)
            binding.salesTotalTax.text = currencyFormatter(it.totalTax, viewModel.currency)
            binding.totalSales.text = currencyFormatter(it.totalPrice, viewModel.currency)
        }

    }

    private fun initSalesInvoices(salesInvoices: List<SalesOrBuyModel>) {
        binding.reportRv.apply {
            adapter = salesInvoicesAdapter
            salesInvoicesAdapter.submitInvoices(salesInvoices)
            salesInvoicesAdapter.onItemClickListener(object : SalesInvoicesAdapter
                .OnItemClickListener {
                override fun onItemClick(invoiceNumber: Int, position: Int) {
                    val action =
                        SalesReportDetailFragmentDirections.actionSalesReportDetailFragmentToReportsInvoiceFragment(
                            invoiceNumber.toString()
                        )
                    findNavController().navigate(action)

                }
            })
        }
    }
}