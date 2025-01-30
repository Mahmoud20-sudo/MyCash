package com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_bottom_sheet_adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.common.util.isSame
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemReportsTypeBinding
import com.plcoding.reports.data.enums.InvoiceType

class InvoiceTypeAdapter(
    private val selectedInvoiceType : Int?,
    private val invoiceTypeList: List<InvoiceType>,
    private val onItemClicked: (InvoiceType) -> Unit
) : RecyclerView.Adapter<InvoiceTypeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemReportsTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = invoiceTypeList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = invoiceTypeList[position]
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemReportsTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
        fun bind(item: InvoiceType) {
            binding.setUpViews(item)
        }

        private fun ItemReportsTypeBinding.setUpViews(item: InvoiceType) {
            setTypeViews(item)
            println(selectedInvoiceType)
            root.setOnClickListener {
                val selectedItem = if (item.value isSame selectedInvoiceType) InvoiceType.NONE else item
                onItemClicked(selectedItem)
            }
            root.setBackgroundColor(
                context.resources.getColor(
                    if (item.value isSame selectedInvoiceType) R.color.purple else
                        R.color.white, null
                )
            )
        }

        private fun ItemReportsTypeBinding.setTypeViews(item: InvoiceType) {
            typeLogoIv.isVisible = true
            typeTv.text = getReportType(item).first
            typeLogoIv.setImageDrawable(getReportType(item).second)

        }

        private fun getReportType(item: InvoiceType) = when (item) {
            InvoiceType.TAX -> getStringAndDrawableRes(
                R.string.reports_with_tax, R.drawable.ic_percentage_circle
            )
            else -> getStringAndDrawableRes(
                R.string.reports_without_tax, R.drawable.ic_empty_circle
            )
        }


        private fun getStringAndDrawableRes(
            @StringRes value: Int, @DrawableRes drawable: Int
        ): Pair<String, Drawable?> {
            val stringRes = context.getString(value)
            val drawableRes = ContextCompat.getDrawable(context, drawable)
            return Pair(stringRes, drawableRes)
        }
    }
}