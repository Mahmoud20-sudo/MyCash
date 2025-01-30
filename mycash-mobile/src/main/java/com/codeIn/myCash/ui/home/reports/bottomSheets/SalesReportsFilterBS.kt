package com.codeIn.myCash.ui.home.reports.bottomSheets

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Toast
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.SalesReportsFilterBottomSheetBinding
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO
import com.codeIn.myCash.ui.home.reports.adapters.BranchesAutoCompleteAdapter
import com.codeIn.myCash.ui.home.reports.adapters.PerChunkFiltersAdapter
import com.codeIn.myCash.utilities.gone
import com.codeIn.myCash.utilities.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.plcoding.reports.data.enums.PerChunk
import com.plcoding.reports.data.enums.TypeOfDate
import java.util.Calendar
import java.util.TimeZone

class SalesReportsFilterBS(private val context: Context) : OnDateSetListener {

    private lateinit var binding: SalesReportsFilterBottomSheetBinding
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var dateType = TypeOfDate.FROM
    private var branchId: Int = -1
    private var startDate: String? = null
    private var endDate: String? = null
    private var defaultSelectedBranchPosition: Int = 0
    private var selectedBranchPosition: Int = 0
    private var perChunkType: PerChunk? = null
    private lateinit var branches: List<BranchesDTO.Data.Data>

    private var receiptStatusListener: ((
        branchId: Int,
        selectedBranchPosition: Int,
        startDate: String?,
        endDate: String?,
        perChunk: PerChunk?
    ) -> Unit)? = null

    private val calendar = Calendar.getInstance(TimeZone.getDefault())
    private lateinit var datePicker: DatePickerDialog
    private lateinit var adapterBranches: BranchesAutoCompleteAdapter

    fun getReceiptStatus(
        receiptStatus: (
            branchId: Int,
            selectedBranchPosition: Int,
            startDate: String?,
            endDate: String?,
            perChunk: PerChunk?
        ) -> Unit
    ) {
        this.receiptStatusListener = receiptStatus
    }

    init {
        buildBottomSheetDialog()
    }

    private fun buildBottomSheetDialog(): BottomSheetDialog? {
        val inflater = LayoutInflater.from(context)
        binding = SalesReportsFilterBottomSheetBinding.inflate(inflater)
        bottomSheetDialog = BottomSheetDialog(context, R.style.reports_bottom_sheet_style)
        bottomSheetDialog?.setContentView(binding.root)
        binding.tvClose.setOnClickListener { dismiss() }
        onClicks()
        return bottomSheetDialog
    }


    private fun onClicks() {
        binding.apply {

            initBranch()
            initTypeOfFilter()

            dateFromEt.setOnClickListener {
                datePicker = DatePickerDialog(
                    context, R.style.datePicker, this@SalesReportsFilterBS,
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
                    context, R.style.datePicker, this@SalesReportsFilterBS,
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
                    endDate,
                    perChunkType
                )

                dismiss()
            }
            reset.setOnClickListener {
                clearState()
            }
        }
    }

    private fun initTypeOfFilter() {
        binding.perDateFilterRG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.dateRB -> showDateFilter()
                R.id.perChunkRB -> showPerChunkFilter()
            }
        }
    }

    private fun showPerChunkFilter() {
        val chunksAdapter = PerChunkFiltersAdapter(PerChunk.entries).apply {
            setPerChunkClicked(object : PerChunkFiltersAdapter.PerChunkClickListener {
                override fun onPerChunkClicked(perChunk: PerChunk) {
                    perChunkType = perChunk
                }
            })
        }
        binding.perChunkRv.adapter = chunksAdapter
        binding.perChunkRv.visible()
        binding.selectDate.gone()
    }

    private fun showDateFilter() {
        binding.selectDate.visible()
        binding.perChunkRv.gone()
        perChunkType = null
    }

    private fun SalesReportsFilterBottomSheetBinding.clearState() {
        setDefaultBranch(branches, defaultSelectedBranchPosition)
        dateFromEt.setText("")
        dateToEt.setText("")
        binding.dateRB.isChecked = true

        receiptStatusListener?.invoke(
            branchId,
            defaultSelectedBranchPosition,
            null,
            null,
            null
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
