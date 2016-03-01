package com.fly.firefly.ui.adapter;

import android.content.Context;
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

public class PassengerSeatAdapterV3 extends BaseAdapter {

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

    public PassengerSeatAdapterV3(Context context, List<PasssengerInfoV2> passengers, MF_SeatSelectionFragment fragment) {
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

        if(obj.get(position).isSelected()){
            vh.passengerLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }else{
            vh.passengerLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        vh.passengerName.setText(obj.get(position).getTitle()+" "+obj.get(position).getFirst_name()+" "+obj.get(position).getLast_name());
        vh.passengerSeatNo.setText(obj.get(position).getSeat());
       // Log.e("Title", obj.get(position).getTitle());
       // Log.e("Seat",obj.get(position).getSeat());

        vh.removeSeatNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(obj.get(position).isSelected()){

                    if(obj.get(position).getSeat() != null){
                        frag.clearSelectedOnFragmentV1(obj.get(position).getSeat());
                        obj.get(position).setSeat(null);
                        notifyDataSetChanged();
                    }

                }else{
                    //PassengerNoT Selected - Remove on if the passenger is selected
                }


            }
        });






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
        frag.clearSeatTag1(passengerPosition);
        obj.get(passengerPosition).setSelected(true);
        obj.get(passengerPosition).setActive(true);

        notifyDataSetChanged();
    }

}
