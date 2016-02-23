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
import com.fly.firefly.ui.activity.BookingFlight.FlightDetailFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FlightDetailAdapter extends BaseAdapter {

    private final Context context;
    private final List<FlightInfo> obj;
    private String departureAirport;
    private String arrivalAirport;
    private String flightClass;
    private Integer selected_position = -1;
    private FlightDetailFragment fragment;
    private String flightWay;
    private Boolean active = false;

    public FlightDetailAdapter(Context context, List<FlightInfo> paramObj,String depart, String arrival,String flightClass,String flightWay,FlightDetailFragment frag) {
        this.context = context;
        this.obj = paramObj;
        this.departureAirport = depart;
        this.arrivalAirport = arrival;
        this.flightClass = flightClass;
        this.fragment = frag;
        this.flightWay = flightWay;
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
        @InjectView(R.id.txtFlightNo) TextView txtFlightNo;
        @InjectView(R.id.txtArrivalTime) TextView txtArrivalTime;
        @InjectView(R.id.txtDepartureTime) TextView txtDepartureTime;
        @InjectView(R.id.txtDepartureAirport) TextView txtDepartureAirport;
        @InjectView(R.id.txtArrivalAirport) TextView txtArrivalAirport;
        @InjectView(R.id.txtFarePrice) TextView txtFarePrice;
        @InjectView(R.id.checkBox) CheckBox checkBox;


    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        Log.e("Invalidate","True");
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.flight_detail_list, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }


        /*CheckBox*/
        if(position==selected_position)
        {
            vh.checkBox.setChecked(true);
        }
        else
        {
            vh.checkBox.setChecked(false);
        }

        vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    //Check Available Or Not
                    if(flightClass.equals("PREMIER")){
                        if(obj.get(position).getFlexObj().getStatus().equals("active")){
                            active = true;
                            selected_position =  position;

                        }
                    }else if(flightClass.equals("BASIC")){
                        if(obj.get(position).getBasicObj().getStatus().equals("active")){
                            active = true;
                            selected_position =  position;
                        }
                    }
                    //True
                    if(active){
                        fragment.selectedInfo(obj.get(position),flightClass,flightWay);
                    }else{
                        fragment.alertNotAvailable();
                    }

                }
                notifyDataSetChanged();
            }
        });

        String totalFare = "";
        if(flightClass.equals("PREMIER")){
            totalFare = "SOLD OUT";
            //totalFare = obj.get(position).getFlexObj().getTotal_fare();

        }else{
            totalFare = "MYR "+obj.get(position).getBasicObj().getTotal_fare();
        }

        vh.txtFlightNo.setText("FLIGHT NO. "+ obj.get(position).getFlight_number());
        vh.txtArrivalTime.setText(obj.get(position).getArrival_time());
        vh.txtDepartureTime.setText(obj.get(position).getDeparture_time());
        vh.txtDepartureAirport.setText(departureAirport);
        vh.txtDepartureAirport.setText(departureAirport);
        vh.txtArrivalAirport.setText(arrivalAirport);
        vh.txtFarePrice.setText(totalFare);
        return view;

    }


    public void invalidateSelected(){
        selected_position = -1;
        notifyDataSetChanged();
    }
}
