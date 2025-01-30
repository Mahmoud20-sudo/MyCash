package com.codeIn.myCash.ui.home.reports.reports_fragment.dialog.repoorts_borrom_sheet

import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.LayoutReportsBootmSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class ReportsBottomSheet @Inject constructor(activity: Activity) {

    private lateinit var sheetBinding: LayoutReportsBootmSheetBinding
    private var bottomSheetDialog: BottomSheetDialog? = null

    init {
        buildBottomSheetDialog(activity)
    }

    private fun buildBottomSheetDialog(activity: Activity): BottomSheetDialog? {
        sheetBinding = LayoutReportsBootmSheetBinding.inflate(activity.layoutInflater)
        bottomSheetDialog = BottomSheetDialog(activity, R.style.reports_bottom_sheet_style)
        bottomSheetDialog?.setContentView(sheetBinding.root)
        sheetBinding.closeIv.setOnClickListener { dismissBottomSheet() }
        return bottomSheetDialog
    }

    fun showBottomSheetDialog() = bottomSheetDialog?.show()


    fun dismissBottomSheet() = bottomSheetDialog?.dismiss()


    fun setTypeTitleAndLogoIv(pair: Pair<Int, Int>) {
        sheetBinding.reportLogoIv.setImageDrawable(
            ContextCompat.getDrawable(sheetBinding.root.context, pair.first)
        )
        sheetBinding.titleTv.text = sheetBinding.root.context.getString(pair.second)
    }

    fun setRecyclerAdapter(adapter: RecyclerView.Adapter<*>?) {
        sheetBinding.reportTypeRv.adapter = adapter
    }

    fun setUpInvoiceTypeViews(setUpInvoiceTypeAdapter : () -> Unit) {
        val resource = Pair(R.drawable.ic_filter, R.string.invoice_filter)
        showBottomSheetDialog()
        setTypeTitleAndLogoIv(resource)
        setUpInvoiceTypeAdapter()
    }

    fun setUpDateTypeViews(setUpDateTypeAdapter :() -> Unit) {
        val resource = Pair(R.drawable.ic_calender_min_black, R.string.duration_time)
        showBottomSheetDialog()
        setTypeTitleAndLogoIv(resource)
        showBottomSheetDialog()
        setUpDateTypeAdapter()
    }

     fun setUpReportFilterTypeViews(setUpReportTypeAdapter :() -> Unit) {
        val resource = Pair(R.drawable.ic_status_up, R.string.reports)
        showBottomSheetDialog()
        setTypeTitleAndLogoIv(resource)
        setUpReportTypeAdapter()
    }


}