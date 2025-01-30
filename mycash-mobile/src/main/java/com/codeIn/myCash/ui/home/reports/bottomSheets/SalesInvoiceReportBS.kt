package com.codeIn.myCash.ui.home.reports.bottomSheets

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.SalesInvoiceFilterBottomSheetBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.ui.home.reports.adapters.BranchesAutoCompleteAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.plcoding.reports.data.enums.TypeOfDate
import java.util.Calendar
import java.util.TimeZone

class SalesInvoiceReportBS (private val context: Context) : OnDateSetListener {

    private lateinit var binding: SalesInvoiceFilterBottomSheetBinding
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var dateType = TypeOfDate.FROM
    private var branchId: Int = -1
    private var startDate: String? = null
    private var endDate: String? = null
    private var defaultSelectedBranchPosition: Int = 0
    private var selectedBranchPosition: Int = 0
    private lateinit var branches: List<BranchesDTO.Data.Data>

    private var receiptStatusListener: ((
        branchId: Int,
        selectedBranchPosition: Int,
        startDate: String?,
        endDate: String?
    ) -> Unit)? = null

    private val calendar = Calendar.getInstance(TimeZone.getDefault())
    private lateinit var datePicker: DatePickerDialog
    private lateinit var adapterBranches: BranchesAutoCompleteAdapter

    fun requestSalesInvoices(
        receiptStatus: (
            branchId: Int,
            selectedBranchPosition: Int,
            startDate: String?,
            endDate: String?
        ) -> Unit
    ) {
        this.receiptStatusListener = receiptStatus
    }

    init {
        buildBottomSheetDialog()
    }

    private fun buildBottomSheetDialog(): BottomSheetDialog? {
        val inflater = LayoutInflater.from(context)
        binding = SalesInvoiceFilterBottomSheetBinding.inflate(inflater)
        bottomSheetDialog = BottomSheetDialog(context, R.style.reports_bottom_sheet_style)
        bottomSheetDialog?.setContentView(binding.root)
        binding.tvClose.setOnClickListener { dismiss() }
        onClicks()
        return bottomSheetDialog
    }


    private fun onClicks() {
        binding.apply {

            initBranch()

            dateFromEt.setOnClickListener {
                datePicker = DatePickerDialog(
                    context, R.style.datePicker, this@SalesInvoiceReportBS,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
                dateType = TypeOfDate.FROM
            }

            dateToEt.setOnClickListener {
                if (dateFromEt.text.isEmpty()) {
                    Toast.makeText(
                        context,
                        R.string.please_select_date_from_first,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                datePicker = DatePickerDialog(
                    context, R.style.datePicker, this@SalesInvoiceReportBS,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
                dateType = TypeOfDate.TO
            }

            applyBtn.setOnClickListener {
                receiptStatusListener?.invoke(
                    branchId,
                    selectedBranchPosition,
                    startDate,
                    endDate

                )

                dismiss()
            }
            reset.setOnClickListener {
                clearState()
            }
        }
    }



    private fun SalesInvoiceFilterBottomSheetBinding.clearState() {
        setDefaultBranch(branches, defaultSelectedBranchPosition)
        dateFromEt.setText("")
        dateToEt.setText("")

        receiptStatusListener?.invoke(
            branchId,
            defaultSelectedBranchPosition,
            null,
            null,
        )
    }

    private fun initBranch() {
        binding.branch.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                val selectedBranch = adapterView.getItemAtPosition(i) as BranchesDTO.Data.Data
                branchId = selectedBranch.id
                binding.branch.setText(selectedBranch.name, false)
            }
        binding.branch.setOnClickListener {
            binding.branch.showDropDown()
        }
    }

    fun setBranches(branches: List<BranchesDTO.Data.Data>, selectedBranchPosition: Int) {
        this.branches = branches
        this.defaultSelectedBranchPosition = selectedBranchPosition
        adapterBranches = BranchesAutoCompleteAdapter(context, branches)
        setDefaultBranch(branches, selectedBranchPosition)
        binding.branch.setAdapter(adapterBranches)
    }

    private fun setDefaultBranch(
        branches: List<BranchesDTO.Data.Data>,
        selectedBranchPosition: Int
    ) {
        this.selectedBranchPosition = selectedBranchPosition
        branchId = branches[selectedBranchPosition].id
        binding.branch.setText(branches[selectedBranchPosition].name, false)

    }

    fun show() = bottomSheetDialog?.show()

    fun dismiss() = bottomSheetDialog?.dismiss()

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$dayOfMonth/${month + 1}/$year"
        when (dateType) {
            TypeOfDate.FROM -> {
                startDate = date
                binding.dateFromEt.setText(date)
            }

            TypeOfDate.TO -> {
                endDate = date
                binding.dateToEt.setText(date)
            }

        }
    }
}
