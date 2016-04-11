package com.fly.firefly.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.ui.activity.ManageFlight.MF_Fragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BookingListAdapter extends BaseAdapter {

    private final Context context;
    private final List<ListBookingReceive.ListBooking> obj;
    private String departureAirport;
    private String arrivalAirport;
    private String flightClass;
    private Integer selected_position = -1;
    private MF_Fragment fragment;
    private String flightWay;
    private Boolean active = false;
    private String module;

    public BookingListAdapter(Context context, List<ListBookingReceive.ListBooking> paramObj,String module) {
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
            vh.txtConfirmation.setText("Confirmation Code");
            vh.txtPNR.setText(obj.get(position).getPnr());
        }else{
            vh.txtConfirmation.setText("Station Code");
            vh.txtPNR.setText(obj.get(position).getDeparture_station_code()+" - "+obj.get(position).getArrival_station_code());
        }
        vh.txtDate.setText(obj.get(position).getDate());

        return view;

    }

}
