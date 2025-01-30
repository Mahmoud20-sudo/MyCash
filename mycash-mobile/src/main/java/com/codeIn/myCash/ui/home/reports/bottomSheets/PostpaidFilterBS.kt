package com.codeIn.myCash.ui.home.reports.bottomSheets

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import com.codeIn.common.data.ReceiptStatusFilter
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.LayoutPostpaidFilterBottomSheetBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.ui.getInvoiceFilterDisplayName
import com.codeIn.myCash.ui.home.reports.adapters.BranchesAutoCompleteAdapter
import com.codeIn.myCash.ui.home.reports.adapters.ReceiptStatusAutoCompleteAdapter
import com.codeIn.myCash.utilities.onClearFocusAndHideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.plcoding.reports.data.enums.TypeOfDate
import java.util.Calendar
import java.util.TimeZone

class PostpaidFilterBS(private val context: Context) : OnDateSetListener {

    private lateinit var sheetBinding: LayoutPostpaidFilterBottomSheetBinding
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var dateType = TypeOfDate.FROM
    private var branchId: Int? = null
    private var dateFrom: String? = null
    private var dateTo: String? = null
    private var receiptStatusId: Int? = null
    private var defaultSelectedBranchPosition: Int = 0
    private var selectedBranchPosition: Int = 0
    private lateinit var branches: List<BranchesDTO.Data.Data>

    private var receiptStatusListener: ((
        branchId: Int?,
        selectedBranchPosition: Int,
        dateFrom: String?,
        dateTo: String?,
        invoiceNum: String?,
        receiptNum: String?,
        receiptStatusId: Int?
    ) -> Unit)? = null

    private val calendar = Calendar.getInstance(TimeZone.getDefault())
    private lateinit var datePicker: DatePickerDialog
    private lateinit var adapterBranches: BranchesAutoCompleteAdapter

    fun getReceiptStatus(
        receiptStatus: (
            branchId: Int?,
            selectedBranchPosition: Int,
            dateFrom: String?,
            dateTo: String?,
            invoiceNum: String?,
            receiptNum: String?,
            receiptStatusId: Int?
        ) -> Unit
    ) {
        this.receiptStatusListener = receiptStatus
    }

    init {
        buildBottomSheetDialog()
    }

    private fun buildBottomSheetDialog(): BottomSheetDialog? {
        val inflater = LayoutInflater.from(context)
        sheetBinding = LayoutPostpaidFilterBottomSheetBinding.inflate(inflater)
        bottomSheetDialog = BottomSheetDialog(context, R.style.reports_bottom_sheet_style)
        bottomSheetDialog?.setContentView(sheetBinding.root)
        sheetBinding.tvClose.setOnClickListener { dismiss() }
        onClicks()
        return bottomSheetDialog
    }

    private fun onClicks() {
        sheetBinding.apply {

            initBranch()

            receiptNumberEt.onClearFocusAndHideKeyboard(context)

            dateFromEt.setOnClickListener {
                datePicker = DatePickerDialog(
                    context, R.style.datePicker, this@PostpaidFilterBS,
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
                    context, R.style.datePicker,this@PostpaidFilterBS,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
                dateType = TypeOfDate.TO
            }

            initReceiptStatus()

            applyBtn.setOnClickListener {
                receiptStatusListener?.invoke(
                    branchId,
                    selectedBranchPosition,
                    dateFrom,
                    dateTo,
                    sheetBinding.invoiceNumberEt.text.toString(),
                    sheetBinding.receiptNumberEt.text.toString(),
                    receiptStatusId
                )

                dismiss()
            }
            reset.setOnClickListener {
                clearState()
            }
        }
    }

    private fun LayoutPostpaidFilterBottomSheetBinding.clearState() {
        setDefaultBranch(branches,defaultSelectedBranchPosition)
        invoiceNumberEt.setText("")
        receiptNumberEt.setText("")
        dateFromEt.setText("")
        dateToEt.setText("")
        setDefaultReceiptStatus(0,ReceiptStatusFilter.ALL)
        receiptStatusListener?.invoke(branchId, defaultSelectedBranchPosition, null, null, null, null,receiptStatusId)
    }

    private fun initBranch() {
        sheetBinding.branch.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                val selectedBranch = adapterView.getItemAtPosition(i) as BranchesDTO.Data.Data
                branchId = selectedBranch.id
                sheetBinding.branch.setText(selectedBranch.name, false)

                // Clear focus to ensure dropdown is reset for next click
                sheetBinding.branch.clearFocus()
            }
        sheetBinding.branch.setOnClickListener {
            sheetBinding.branch.showDropDown()
        }
    }

    fun setBranches(branches: List<BranchesDTO.Data.Data>, selectedBranchPosition: Int) {
        this.branches = branches
        this.defaultSelectedBranchPosition = selectedBranchPosition
        adapterBranches = BranchesAutoCompleteAdapter(context, branches)
        setDefaultBranch(branches,selectedBranchPosition)
        sheetBinding.branch.setAdapter(adapterBranches)
    }

    private fun setDefaultBranch(branches: List<BranchesDTO.Data.Data>, selectedBranchPosition: Int) {
        this.selectedBranchPosition = selectedBranchPosition
        branchId = branches[selectedBranchPosition].id
        sheetBinding.branch.setText(branches[selectedBranchPosition].name, false)

    }

    private fun initReceiptStatus() {

        val receiptStatusAdapter = ReceiptStatusAutoCompleteAdapter(
            context, ReceiptStatusFilter.entries
        )
        sheetBinding.receiptStatus.setAdapter(receiptStatusAdapter)
        setDefaultReceiptStatus(0,ReceiptStatusFilter.ALL)
        sheetBinding.receiptStatus.setOnItemClickListener { _, _, position, _ ->
            val selectedFilter = receiptStatusAdapter.getItem(position)
            setDefaultReceiptStatus(position,selectedFilter)
            // Clear focus to ensure dropdown is reset for next click
            sheetBinding.receiptStatus.clearFocus()
        }

        sheetBinding.receiptStatus.setOnClickListener {
            sheetBinding.receiptStatus.showDropDown()
        }

    }
    private fun setDefaultReceiptStatus(id: Int, receiptStatusFilter: ReceiptStatusFilter? ) {
        receiptStatusId = id
        sheetBinding.receiptStatus.setText(receiptStatusFilter?.getInvoiceFilterDisplayName(context), false)
    }

    fun show() = bottomSheetDialog?.show()

    fun dismiss() = bottomSheetDialog?.dismiss()

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$dayOfMonth/${month + 1}/$year"
        when (dateType) {
            TypeOfDate.FROM -> {
                dateFrom = date
                sheetBinding.dateFromEt.setText(date)
            }

            TypeOfDate.TO -> {
                dateTo = date
                sheetBinding.dateToEt.setText(date)
            }

        }
    }
}
