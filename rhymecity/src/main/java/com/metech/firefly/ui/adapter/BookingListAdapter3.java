package com.metech.firefly.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.metech.firefly.R;
import com.metech.firefly.ui.activity.ManageFlight.MF_Fragment;
import com.metech.firefly.ui.object.ListFlight2;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BookingListAdapter3 extends BaseAdapter {

    private final Context context;
    private final List<ListFlight2> obj;
    private String departureAirport;
    private String arrivalAirport;
    private String flightClass;
    private Integer selected_position = -1;
    private MF_Fragment fragment;
    private String flightWay;
    private Boolean active = false;
    private String module;

    public BookingListAdapter3(Context context, List<ListFlight2> paramObj, String module) {
        this.context = context;
        this.obj = paramObj;
        this.module = module;
    }

    @Override
    public int getCount() {
        return obj == null ? 0 : obj.size();
    }

    @Override
    public Object getItem(int position) {
        return obj == null ? null : obj.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        @InjectView(R.id.txtPNR) TextView txtPNR;
        @InjectView(R.id.txtDate) TextView txtDate;
        @InjectView(R.id.txtConfirmation) TextView txtConfirmation;
        @InjectView(R.id.txtConfirmationNumber) TextView txtConfirmationNumber;
        @InjectView(R.id.txtCN) TextView txtCN;
        //@InjectView(R.id.txtArrival) TextView txtArrival;
        //@InjectView(R.id.txtDeparture) TextView txtDeparture;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.booking_list, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        if(module.equals("MF")){
            vh.txtCN.setVisibility(View.GONE);
            vh.txtConfirmationNumber.setVisibility(View.GONE);
            vh.txtConfirmation.setText("Confirmation Code");
            vh.txtPNR.setText(obj.get(position).getPnr());
        }
        vh.txtDate.setText(obj.get(position).getDate());

        return view;

    }

}
