package com.fly.firefly.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.ui.activity.ManageFlight.MF_Fragment;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class OfflineBookingListAdapter extends BaseAdapter {

    private final Context context;
    private final RealmResults<BoardingPassObj> obj;
    private String departureAirport;
    private String arrivalAirport;
    private String flightClass;
    private Integer selected_position = -1;
    private MF_Fragment fragment;
    private String flightWay;
    private Boolean active = false;

    public OfflineBookingListAdapter(Context context, RealmResults<BoardingPassObj> paramObj) {
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

        vh.txtPNR.setText(obj.get(position).getPnr());
        vh.txtDate.setText((new Gson()).fromJson(obj.get(position).getBoardingPassObj(),MobileConfirmCheckInPassengerReceive.class).getObj().getBoarding_pass().get(0).getDepartureDate());

        return view;

    }

}
