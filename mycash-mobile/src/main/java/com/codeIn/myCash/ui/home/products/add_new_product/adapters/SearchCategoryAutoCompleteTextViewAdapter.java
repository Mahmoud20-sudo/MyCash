package com.codeIn.myCash.ui.home.products.add_new_product.adapters;

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
import com.codeIn.common.domain.model.AutoCompleteModelSearch;

import java.util.ArrayList;
import java.util.List;

public class SearchCategoryAutoCompleteTextViewAdapter extends ArrayAdapter<AutoCompleteModelSearch> {
    private List<AutoCompleteModelSearch> resultListFull;

    public SearchCategoryAutoCompleteTextViewAdapter(@NonNull Context context, @NonNull List<AutoCompleteModelSearch> list) {
        super(context, 0, list);
        resultListFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return resultFilter;
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

        AutoCompleteModelSearch categoryModelSearchItem = getItem(position);

        if (categoryModelSearchItem != null) {
            textViewName.setText(categoryModelSearchItem.getName());
        }

        if (position == (resultListFull.size() - 1))
            line.setVisibility(View.GONE);
        else
            line.setVisibility(View.VISIBLE);

        return convertView;
    }

    private Filter resultFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<AutoCompleteModelSearch> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(resultListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (AutoCompleteModelSearch item : resultListFull) {
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
            try {
                clear();
                addAll((List) results.values);
                notifyDataSetChanged();
            }catch (Exception e){

            }
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((AutoCompleteModelSearch) resultValue).getName();
        }
    };
}