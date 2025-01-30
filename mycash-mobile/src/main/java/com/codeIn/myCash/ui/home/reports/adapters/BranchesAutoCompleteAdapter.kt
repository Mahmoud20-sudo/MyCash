package com.codeIn.myCash.ui.home.reports.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.codeIn.common.domain.model.AutoCompleteModelSearch
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO

class BranchesAutoCompleteAdapter(
    context: Context,
    private val filters: List<BranchesDTO.Data.Data>
) : ArrayAdapter<BranchesDTO.Data.Data>(
    context,
    android.R.layout.simple_dropdown_item_1line,
    filters
) {

    override fun getItem(position: Int): BranchesDTO.Data.Data? {
        return filters.getOrNull(position)
    }

    override fun getItemId(position: Int): Long {
        return filters[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        val filter = getItem(position)
        textView.text = filter?.name

        view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))

        return view
    }

}

