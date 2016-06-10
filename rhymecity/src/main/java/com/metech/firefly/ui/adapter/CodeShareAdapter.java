package com.metech.firefly.ui.adapter;

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

import com.metech.firefly.R;
import com.metech.firefly.api.obj.FlightInfo;
import com.metech.firefly.ui.activity.BookingFlight.CodeShareFlightListFragment;

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
    private String selectedType;
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
        @InjectView(R.id.txtOperatedBy) TextView txtOperatedBy;

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

        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.codeshare_flight_list, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }




        /*CheckBox*/
        if(position==selected_position)
        {

            //checkbox per row
            if(obj.get(position).getEconomy_promo_class().getChecked() != null){
                if(obj.get(position).getEconomy_promo_class().getChecked()){
                    vh.economyPromoCheckBox.setChecked(true);
                }else{
                    vh.economyPromoCheckBox.setChecked(false);
                }
            }

            if(obj.get(position).getEconomy_class().getChecked() != null){
                if(obj.get(position).getEconomy_class().getChecked()){
                    vh.economyCheckBox.setChecked(true);
                }else{
                    vh.economyCheckBox.setChecked(false);
                }
            }

            if(obj.get(position).getBusiness_class().getChecked() != null){
                if(obj.get(position).getBusiness_class().getChecked()){
                    vh.businessCheckBox.setChecked(true);
                }else{
                    vh.businessCheckBox.setChecked(false);
                }
            }

            if(selectedType.equals("ECONOMY_PROMO")){
                if(obj.get(position).getEconomy_promo_class().getChecked()){
                    vh.economyPromoCheckBox.setChecked(true);
                }else{
                    vh.economyPromoCheckBox.setChecked(false);
                }
            }else if(selectedType.equals("ECONOMY")){
                if(obj.get(position).getEconomy_class().getChecked()){
                    vh.economyCheckBox.setChecked(true);
                }else{
                    vh.economyCheckBox.setChecked(false);
                }
            }else if(selectedType.equals("BUSINESS")){
                if(obj.get(position).getBusiness_class().getChecked()){
                    vh.businessCheckBox.setChecked(true);
                }else{
                    vh.businessCheckBox.setChecked(false);
                }
            }

            /*if(obj.get(position).getEconomy_promo_class().getStatus().equals("active")){

            }

            if(obj.get(position).getEconomy_class().getStatus().equals("active")){

            }

            if(obj.get(position).getBusiness_class().getStatus().equals("active")){

            }*/
        }else
        {
            vh.economyPromoCheckBox.setChecked(false);
            vh.economyCheckBox.setChecked(false);
            vh.businessCheckBox.setChecked(false);
            Log.e("Not Checked","true");
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
                       selectedType = "ECONOMY_PROMO";

                       //clearSelected();
                       obj.get(position).getEconomy_promo_class().setChecked(true);
                       obj.get(position).getEconomy_class().setChecked(false);
                       obj.get(position).getBusiness_class().setChecked(false);


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
                        selectedType = "ECONOMY";

                        //clearSelected();
                        obj.get(position).getEconomy_class().setChecked(true);
                        obj.get(position).getBusiness_class().setChecked(false);
                        obj.get(position).getEconomy_promo_class().setChecked(false);

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
                        selectedType = "BUSINESS";

                        //clearSelected();
                        obj.get(position).getEconomy_class().setChecked(false);
                        obj.get(position).getBusiness_class().setChecked(true);
                        obj.get(position).getEconomy_promo_class().setChecked(false);
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


        vh.txtFlightNo.setText("FLIGHT NO. FY "+ obj.get(position).getFlight_number());
        vh.txtArrivalTime.setText(obj.get(position).getArrival_time());
        vh.txtDepartureTime.setText(obj.get(position).getDeparture_time());
        vh.txtDepartureAirport.setText(departureAirport);
        vh.txtDepartureAirport.setText(departureAirport);
        vh.txtArrivalAirport.setText(arrivalAirport);
        vh.txtOperatedBy.setText("Operated by Malaysia Airlines (MH"+obj.get(position).getMh_flight_number()+")" );

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

    public void clearSelected(){

        int x = 0;
        for (FlightInfo pic : obj)
        {
            Log.e("Loop",Integer.toString(x));

            obj.get(x).getEconomy_promo_class().setChecked(false);
            obj.get(x).getBusiness_class().setChecked(false);
            obj.get(x).getEconomy_class().setChecked(false);

            x++;
        }
    }

    public void invalidateSelected(){
        selected_position = -1;
        notifyDataSetChanged();
    }
}
