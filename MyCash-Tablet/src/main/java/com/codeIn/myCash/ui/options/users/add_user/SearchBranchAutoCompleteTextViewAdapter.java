package com.codeIn.myCash.ui.options.users.add_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codeIn.myCash.R;
import com.codeIn.myCash.features.sales.data.branch.model.response.BranchesDTO;
import java.util.ArrayList;
import java.util.List;

public class SearchBranchAutoCompleteTextViewAdapter extends ArrayAdapter<BranchesDTO.Data.Data> {
    private List<BranchesDTO.Data.Data> branchesListFull;

    public SearchBranchAutoCompleteTextViewAdapter(@NonNull Context context, @NonNull List<BranchesDTO.Data.Data> list) {
        super(context, 0, list);
        branchesListFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return categoryFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_search, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.tvAutocompleteListItem);
        View line = convertView.findViewById(R.id.line);

        BranchesDTO.Data.Data categoryModelSearchItem = getItem(position);

        if (categoryModelSearchItem != null) {
            textViewName.setText(categoryModelSearchItem.getName());
        }

        if (position == (branchesListFull.size() - 1))
            line.setVisibility(View.GONE);
        else
            line.setVisibility(View.VISIBLE);

        return convertView;
    }

    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<BranchesDTO.Data.Data> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(branchesListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (BranchesDTO.Data.Data item : branchesListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           clear();
           addAll((List) results.values);
           notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((BranchesDTO.Data.Data)resultValue).getName();
        }
    };
}