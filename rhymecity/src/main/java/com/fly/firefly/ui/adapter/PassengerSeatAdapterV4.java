package com.fly.firefly.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.R;
import com.fly.firefly.ui.activity.BookingFlight.FireflyFlightListFragment;
import com.fly.firefly.ui.activity.ManageFlight.MF_SeatSelectionFragment;
import com.fly.firefly.ui.object.PasssengerInfoV2;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PassengerSeatAdapterV4 extends BaseAdapter {

    private final Context context;
    private final List<PasssengerInfoV2> obj;
    private String departureAirport;
    private String arrivalAirport;
    private String flightClass;
    private Integer selected_position = -1;
    private FireflyFlightListFragment fragment;
    private String flightWay;
    private Boolean active = false;
    MF_SeatSelectionFragment frag;

    public PassengerSeatAdapterV4(Context context, List<PasssengerInfoV2> passengers, MF_SeatSelectionFragment fragment) {
        this.context = context;
        this.obj = passengers;
        this.frag = fragment;
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
        @InjectView(R.id.passengerSeatNo) TextView passengerSeatNo;
        @InjectView(R.id.passengerName) TextView passengerName;
        @InjectView(R.id.passengerLinearLayout) LinearLayout passengerLinearLayout;
        @InjectView(R.id.removeSeatNo) LinearLayout removeSeatNo;


    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        Log.e("Invalidate","True");
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.passenger_seat_list, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.removeSeatNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(obj.get(position).isSelected()){

                    if(obj.get(position).getSeat() != null){

                        obj.get(position).setSelected(true);
                        frag.clearSelectedOnFragmentV2(obj.get(position).getSeat());
                        obj.get(position).setSeat(null);
                        notifyDataSetChanged();
                    }

                    notifyDataSetChanged();
                }else{
                    //PassengerNoT Selected - Remove on if the passenger is selected
                }


            }
        });

        vh.passengerName.setText(obj.get(position).getTitle()+" "+obj.get(position).getFirst_name()+" "+obj.get(position).getLast_name());
        vh.passengerSeatNo.setText(obj.get(position).getSeat());

        if(obj.get(position).isSelected()){
            vh.passengerLinearLayout.setBackgroundColor(Color.parseColor("#FFD504"));
        }else{
            vh.passengerLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        if(obj.get(position).getCheckedIn().equals("Y")) {
            vh.removeSeatNo.setVisibility(View.INVISIBLE);
            vh.passengerLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        return view;

    }

    public void invalidateSelected(){
        selected_position = -1;
        notifyDataSetChanged();
    }

    public void clearSelected(){

        int x = 0;
        for (PasssengerInfoV2 pic : obj)
        {

            pic.setSelected(false);
            Log.e(Boolean.toString(obj.get(x).isSelected()), "False");
            x++;
        }

        notifyDataSetChanged();
    }

    public void setSelectedPasssengerSeat(String seatNumber){

        int x = 0;
        for (PasssengerInfoV2 pic : obj)
        {
            if(obj.get(x).isSelected()){
                obj.get(x).setSeat(seatNumber);
            }
            x++;
        }
        notifyDataSetChanged();
    }

    public void setSelectedCompartmentSeat(String seatNumber){

        int x = 0;
        for (PasssengerInfoV2 pic : obj)
        {
            if(obj.get(x).isSelected()){
                obj.get(x).setCompartment(seatNumber);
            }
            x++;
        }
        notifyDataSetChanged();
    }

    public String getSelected(int data){

        int seatNumber = data;
        String data2 = obj.get(seatNumber).getSeat();

        //Log.e("Seat",data2);

        return data2;
        //notifyDataSetChanged();
    }

    public String getSelectedCompartment(int data){

        int seatNumber = data;
        String data2 = obj.get(seatNumber).getCompartment();

        //Log.e("Seat",data2);

        return data2;
        //notifyDataSetChanged();
    }

    public void setNextPassengerSelected(int passengerPosition){

        int x = 0;
        for (PasssengerInfoV2 pic : obj)
        {
            obj.get(x).setSelected(false);
            x++;
        }

        if(obj.get(passengerPosition).getCheckedIn().equals("N")){
            frag.clearSeatTag2(passengerPosition);
            obj.get(passengerPosition).setSelected(true);
            obj.get(passengerPosition).setActive(true);
        }else{
            try{
                frag.clearSeatTag2(passengerPosition+1);
                obj.get(passengerPosition+1).setSelected(true);
                obj.get(passengerPosition+1).setActive(true);
            }catch (Exception e){

            }
        }

        notifyDataSetChanged();
    }

    public void autoSelectReturnPassenger(){

        int x = 0;
        for (PasssengerInfoV2 pic : obj)
        {
            obj.get(x).setSelected(false);
            x++;
        }

        for(int t = 0; t < obj.size(); t++){
            if(obj.get(t).getCheckedIn().equals("N")){
                obj.get(t).setSelected(true);
                obj.get(t).setActive(true);
                frag.clearSeatTag2(t);
                break;
            }
        }
        notifyDataSetChanged();
    }

}
