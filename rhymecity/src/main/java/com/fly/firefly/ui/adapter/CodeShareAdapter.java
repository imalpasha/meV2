package com.fly.firefly.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightInfo;
import com.fly.firefly.ui.activity.BookingFlight.CodeShareFlightListFragment;
import com.fly.firefly.ui.activity.BookingFlight.FireflyFlightListFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CodeShareAdapter extends BaseAdapter {

    private final Context context;
    private final List<FlightInfo> obj;
    private String departureAirport;
    private String arrivalAirport;
    private String flightClass;
    private Integer selected_position = -1;
    private CodeShareFlightListFragment fragment;
    private String flightWay;
    private Boolean active = false;

    public CodeShareAdapter(Context context, List<FlightInfo> paramObj,String depart, String arrival,String flightWay,CodeShareFlightListFragment frag) {
        this.context = context;
        this.obj = paramObj;
        this.departureAirport = depart;
        this.arrivalAirport = arrival;
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
       // @InjectView(R.id.txtFarePrice) TextView txtFarePrice;
       // @InjectView(R.id.checkBox) CheckBox checkBox;

        @InjectView(R.id.txtBusiness) TextView txtBusiness;
        @InjectView(R.id.txtEco) TextView txtEco;
        @InjectView(R.id.txtEcoPromo) TextView txtEcoPromo;

        @InjectView(R.id.economyPromoCheckBox) CheckBox economyPromoCheckBox;
        @InjectView(R.id.economyCheckBox) CheckBox economyCheckBox;
        @InjectView(R.id.businessCheckBox) CheckBox businessCheckBox;
        @InjectView(R.id.listFlightIcon) ImageView listFlightIcon;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        Log.e("Invalidate","True");
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.codeshare_flight_list, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        Log.e("E",obj.get(position).getEconomy_class().getStatus());
        /*CheckBox*/
        if(position==selected_position)
        {
            if(obj.get(position).getEconomy_promo_class().getStatus().equals("active")){
                if(obj.get(position).getEconomy_promo_class().getChecked()){
                    vh.economyPromoCheckBox.setChecked(true);
                }else{
                    vh.economyPromoCheckBox.setChecked(false);
                }
            }

            if(obj.get(position).getEconomy_class().getStatus().equals("active")){
                if(obj.get(position).getEconomy_class().getChecked()){
                    vh.economyCheckBox.setChecked(true);
                }else{
                    vh.economyCheckBox.setChecked(false);
                }
            }

            if(obj.get(position).getBusiness_class().getStatus().equals("active")){
                if(obj.get(position).getBusiness_class().getChecked()){
                    vh.economyCheckBox.setChecked(true);
                }else{
                    vh.economyCheckBox.setChecked(false);
                }
            }
        }
        else
        {
            vh.economyPromoCheckBox.setChecked(false);
            vh.economyCheckBox.setChecked(false);

        }

        //EconomyPromo CheckBox
        vh.economyPromoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //Check Available Or Not
                    if(obj.get(position).getEconomy_promo_class().getStatus().equals("active")){
                       active = true;
                       selected_position =  position;
                        obj.get(position).getEconomy_promo_class().setChecked(true);
                    }else{
                        obj.get(position).getEconomy_promo_class().setChecked(false);
                    }

                    //True
                    if(active){
                        fragment.selectedInfo(obj.get(position),"ECONOMY_PROMO",flightWay);
                    }else{
                        fragment.alertNotAvailable();
                    }
                }
                notifyDataSetChanged();
            }
        });

        //Economy CheckBox
        vh.economyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //Check Available Or Not
                    if(obj.get(position).getEconomy_class().getStatus().equals("active")){
                        active = true;
                        selected_position =  position;
                        obj.get(position).getEconomy_class().setChecked(true);
                    }
                    //True
                    if(active){
                        fragment.selectedInfo(obj.get(position),"ECONOMY",flightWay);
                    }else{
                        fragment.alertNotAvailable();
                    }
                }
                notifyDataSetChanged();
            }
        });

        //Business CheckBox
        vh.businessCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //Check Available Or Not
                    if(obj.get(position).getBusiness_class().getStatus().equals("active")){
                        active = true;
                        selected_position =  position;
                        obj.get(position).getBusiness_class().setChecked(true);
                    }
                    //True
                    if(active){
                        fragment.selectedInfo(obj.get(position),"BUSINESS",flightWay);
                    }else{
                        fragment.alertNotAvailable();
                    }
                }
                notifyDataSetChanged();
            }
        });


        vh.txtFlightNo.setText("FLIGHT NO. MH "+ obj.get(position).getFlight_number());
        vh.txtArrivalTime.setText(obj.get(position).getArrival_time());
        vh.txtDepartureTime.setText(obj.get(position).getDeparture_time());
        vh.txtDepartureAirport.setText(departureAirport);
        vh.txtDepartureAirport.setText(departureAirport);
        vh.txtArrivalAirport.setText(arrivalAirport);

        if(obj.get(position).getEconomy_promo_class().getStatus().equals("active")){
            vh.txtBusiness.setText(obj.get(position).getEconomy_promo_class().getTotal_fare()+" MYR");
        }else{
            vh.txtEcoPromo.setText(obj.get(position).getEconomy_promo_class().getStatus());
            vh.economyPromoCheckBox.setVisibility(View.GONE);
        }

        if(obj.get(position).getEconomy_class().getStatus().equals("active")){
            vh.txtEco.setText(obj.get(position).getEconomy_class().getTotal_fare()+" MYR");
        }else{
            vh.txtEco.setText(obj.get(position).getEconomy_class().getStatus());
            vh.economyCheckBox.setVisibility(View.GONE);
        }

        if(obj.get(position).getBusiness_class().getStatus().equals("active")){
            vh.txtBusiness.setText(obj.get(position).getBusiness_class().getTotal_fare()+" MYR");
        }else{
            vh.txtBusiness.setText(obj.get(position).getBusiness_class().getStatus());
            vh.businessCheckBox.setVisibility(View.GONE);
        }

        if(flightWay.equals("DEPART")){
            vh.listFlightIcon.setBackgroundResource(R.drawable.departure_icon);
        }else{
            vh.listFlightIcon.setBackgroundResource(R.drawable.arrival_icon);
        }

        return view;

    }


    public void invalidateSelected(){
        selected_position = -1;
        notifyDataSetChanged();
    }
}
