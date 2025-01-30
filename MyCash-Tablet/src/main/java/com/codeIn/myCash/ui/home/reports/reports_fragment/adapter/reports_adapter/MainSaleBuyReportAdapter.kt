package com.codeIn.myCash.ui.home.reports.reports_fragment.adapter.reports_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.codeIn.myCash.R
import com.codeIn.myCash.databinding.ItemSaleOrBuyReportBinding
import com.plcoding.reports.data.enums.SaleBuyType
import com.plcoding.reports.data.salesorbuy.model.SalesOrBuyModel

class MainSaleBuyReportAdapter : RecyclerView.Adapter<MainSaleBuyReportAdapter.MainSaleBuyReportVh>() {
    class MainSaleBuyReportVh(var binding: ItemSaleOrBuyReportBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    private val diffUtil = object : DiffUtil.ItemCallback<SalesOrBuyModel>() {
        override fun areItemsTheSame(oldItem: SalesOrBuyModel, newItem: SalesOrBuyModel):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SalesOrBuyModel, newItem: SalesOrBuyModel):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData( dataResponse: List<SalesOrBuyModel>){
        val currentList = asyncListDiffer.currentList.toMutableList()
        currentList.addAll(dataResponse)
        asyncListDiffer.submitList(currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MainSaleBuyReportVh{
        val binding =
            ItemSaleOrBuyReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainSaleBuyReportVh(binding)}

    override fun onBindViewHolder(holder: MainSaleBuyReportVh, position: Int) {
        val item = asyncListDiffer.currentList[position]
        val context = holder.binding.root.context
        holder.binding.apply {
            reportTitleTv.text = context.getString(getReportTitle(item))
            reportCountTv.text = "#${item.invoiceOrder}"
            invoiceNumberValueTv.text = "${item.invoiceNumber}"
            totalValueTv.text = context.getString(R.string.string_sar, item.totalWithoutTax)
            taxValueTv.text = context.getString(R.string.string_sar, item.taxPrice)
            priceIncludesTaxValueTv.text = context.getString(R.string.string_sar, item.totalPriceWithTax)
            item.client?.name?.let { clientNameValueTv.text = it }
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    private fun getReportTitle(item: SalesOrBuyModel) = when (item.saleOrBuy) {
        SaleBuyType.NONE.value -> R.string.mainReports

        SaleBuyType.SALE.value -> R.string.salesInvoice

        SaleBuyType.BUY.value -> R.string.purchasingInvoice

        else -> R.string.mainReports
    }

    fun clear() {
        asyncListDiffer.submitList(null)
    }
}