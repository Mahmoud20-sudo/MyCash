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
import com.codeIn.myCash.features.stock.domain.category.model.CategoryModelSearch;

import java.util.ArrayList;
import java.util.List;

public class SearchCategoryAutoCompleteTextViewAdapter extends ArrayAdapter<CategoryModelSearch> {
    private List<CategoryModelSearch> categoryListFull;

    public SearchCategoryAutoCompleteTextViewAdapter(@NonNull Context context, @NonNull List<CategoryModelSearch> list) {
        super(context, 0, list);
        categoryListFull = new ArrayList<>(list);
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

        CategoryModelSearch categoryModelSearchItem = getItem(position);

        if (categoryModelSearchItem != null) {
            textViewName.setText(categoryModelSearchItem.getName());
        }

        if (position == (categoryListFull.size() - 1))
            line.setVisibility(View.GONE);
        else
            line.setVisibility(View.VISIBLE);

        return convertView;
    }

    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<CategoryModelSearch> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(categoryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CategoryModelSearch item : categoryListFull) {
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
            return ((CategoryModelSearch)resultValue).getName();
        }
    };
}