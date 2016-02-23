package com.fly.firefly.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightInfo;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.ui.activity.BookingFlight.FlightDetailFragment;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlightFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OfflineBookingListAdapter extends BaseAdapter {

    private final Context context;
    private final List<MobileConfirmCheckInPassengerReceive.BoardingPass> obj;
    private String departureAirport;
    private String arrivalAirport;
    private String flightClass;
    private Integer selected_position = -1;
    private ManageFlightFragment fragment;
    private String flightWay;
    private Boolean active = false;

    public OfflineBookingListAdapter(Context context, List<MobileConfirmCheckInPassengerReceive.BoardingPass> paramObj) {
        this.context = context;
        this.obj = paramObj;
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

        vh.txtPNR.setText(obj.get(position).getRecordLocator());
        vh.txtDate.setText(obj.get(position).getDepartureDate());

        return view;

    }

}
