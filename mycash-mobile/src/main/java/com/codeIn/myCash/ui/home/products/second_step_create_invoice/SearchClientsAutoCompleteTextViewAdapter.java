package com.codeIn.myCash.ui.home.products.second_step_create_invoice;



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
import com.codeIn.myCash.features.sales.data.clients.remote.response.ClientModel;

import java.util.ArrayList;
import java.util.List;

public class SearchClientsAutoCompleteTextViewAdapter extends ArrayAdapter<ClientModel> {
    private List<ClientModel> clientsListFull;

    public SearchClientsAutoCompleteTextViewAdapter(@NonNull Context context, @NonNull List<ClientModel> clientsList) {
        super(context, 0, clientsList);
        clientsListFull = new ArrayList<>(clientsList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return clientsFilter;
    }

    public void clearData(){
        clear();
        notifyDataSetChanged();
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

        ClientModel clientItem = getItem(position);

        if (clientItem != null) {
            textViewName.setText(clientItem.getPhone());
        }

        if (position == (clientsListFull.size() - 1))
            line.setVisibility(View.GONE);
        else
            line.setVisibility(View.VISIBLE);
        return convertView;
    }

    private Filter clientsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<ClientModel> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(clientsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ClientModel item : clientsListFull) {
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
            return ((String) ((ClientModel)resultValue).getPhone());
        }
    };
}