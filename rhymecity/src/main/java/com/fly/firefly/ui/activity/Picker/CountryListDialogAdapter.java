package com.fly.firefly.ui.activity.Picker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.utils.DropDownItem;

import java.util.ArrayList;

public class CountryListDialogAdapter extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<DropDownItem> countries;
    ArrayList<DropDownItem> stringCountryFilter;
    TextView tvCountry;
    DropDownItem currentItem;
    ValueFilter valueFilter;

    public CountryListDialogAdapter(Context context, ArrayList<DropDownItem> countries) {
        this.context = context;
        this.countries = countries;
        stringCountryFilter = countries;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return countries.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        currentItem = (DropDownItem) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_country, parent, false);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);
        tvCountry.setText(currentItem.getText());

        return view;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<DropDownItem> filterList = new ArrayList<DropDownItem>();
                for (int i = 0 ; i < stringCountryFilter.size() ; i++) {
                    if (stringCountryFilter.get(i).getText().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        DropDownItem dropDownItem = stringCountryFilter.get(i);

                        filterList.add(dropDownItem);
                    }
                }
                filterResults.count = filterList.size();
                filterResults.values = filterList;
            } else {
                filterResults.count = stringCountryFilter.size();
                filterResults.values = stringCountryFilter;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countries = (ArrayList<DropDownItem>) results.values;
            notifyDataSetChanged();
        }
    }
}
