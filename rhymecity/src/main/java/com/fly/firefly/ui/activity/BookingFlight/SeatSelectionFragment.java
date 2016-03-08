package com.fly.firefly.ui.activity.BookingFlight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.SeatSelectionReveice;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.PassengerSeatAdapterV1;
import com.fly.firefly.ui.adapter.PassengerSeatAdapterV2;
import com.fly.firefly.ui.module.SeatSelectionModule;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.ManageSeatInfo;
import com.fly.firefly.ui.object.PasssengerInfoV2;
import com.fly.firefly.ui.object.SeatInfo;
import com.fly.firefly.ui.object.SeatSelect;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.SeatSetup;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.ExpandAbleGridView;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.RealmResults;

public class SeatSelectionFragment extends BaseFragment implements BookingPresenter.SeatSelectionView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.btnSeat)
    Button btnSeat ;

    @InjectView(R.id.seatListDepart)
    LinearLayout seatListDepart;

    @InjectView(R.id.seatListReturn)
    LinearLayout seatListReturn;

    @InjectView(R.id.listPassengerDepart)
    ExpandAbleGridView listPassengerDepart;

    @InjectView(R.id.listPassengerReturn)
    ExpandAbleGridView listPassengerReturn;

    @InjectView(R.id.txtSeatDeparture)
    TextView txtSeatDeparture;

    @InjectView(R.id.txtSeatReturn)
    TextView txtSeatReturn;

    @InjectView(R.id.passengerSeatListReturn)
    LinearLayout passengerSeatListReturn;

    private SharedPrefManager pref;
    private PassengerSeatAdapterV1 passengerSeatListV1;
    private PassengerSeatAdapterV2 passengerSeatListV2;

    private int fragmentContainerId;
    private List<String> seatTag1;
    private List<String> seatTag2;

    private List<String> selectedSeatTag;

    //newLogin2/22
    private int passengerNoV1 = 0;
    private int passengerNoV2;
    private Boolean next1 = false;
    private Boolean next2 = false;

    private int passengerSize;

    private View view;
    private String bookingID,signature;
    private List<SeatInfo> seatInfoReturn;
    private List<SeatInfo> seatInfoDepart;
    private boolean twoWay = false;

    public static SeatSelectionFragment newInstance(Bundle bundle) {

        SeatSelectionFragment fragment = new SeatSelectionFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SeatSelectionModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.seat_selection, container, false);
        ButterKnife.inject(this, view);

        /*Preference Manager*/
        pref = new SharedPrefManager(MainFragmentActivity.getContext());

        HashMap<String, String> init = pref.getSeat();
        String seatHash = init.get(SharedPrefManager.SEAT);
        /*Booking Id*/
        HashMap<String, String> initBookingID = pref.getBookingID();
        bookingID = initBookingID.get(SharedPrefManager.BOOKING_ID);

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

       // Bundle bundle = getArguments();
       // String seatGSON = bundle.getString("SEAT_INFORMATION");

        /*Initiate Seat Row*/
        Gson gson = new Gson();
        ContactInfoReceive obj = gson.fromJson(seatHash, ContactInfoReceive.class);

        seatInfoDepart = obj.getObj().getJourneys().get(0).getSeat_info();
        List<ContactInfoReceive.Journeys> journeys = obj.getObj().getJourneys();

        /*Set Passenger to adapter*/
        final List<ContactInfoReceive.PasssengerInfo> passengers = obj.getObj().getPassengers();

        /*Create New Passenger Obj*/
        final List<PasssengerInfoV2> objV2 = new ArrayList<PasssengerInfoV2>();
        for(int v = 0 ; v < passengers.size() ; v++){
            PasssengerInfoV2 obj2 = new PasssengerInfoV2();
            obj2.setFirst_name(passengers.get(v).getFirst_name());
            obj2.setLast_name(passengers.get(v).getLast_name());
            obj2.setTitle(passengers.get(v).getTitle());
            objV2.add(obj2);
        }

        final List<PasssengerInfoV2> objV3 = new ArrayList<PasssengerInfoV2>();
        for(int v = 0 ; v < passengers.size() ; v++){
            PasssengerInfoV2 obj3 = new PasssengerInfoV2();
            obj3.setFirst_name(passengers.get(v).getFirst_name());
            obj3.setLast_name(passengers.get(v).getLast_name());
            obj3.setTitle(passengers.get(v).getTitle());
            objV3.add(obj3);
        }

        //set total passenger
        passengerSize = objV2.size();
        Log.e("passengerSize",Integer.toString(passengerSize));

        setSeat1(seatListDepart, seatInfoDepart);
        setPassenger1("DEPART", listPassengerDepart, txtSeatDeparture, objV2, journeys.get(0).getDeparture_station(), journeys.get(0).getArrival_station());
        seatListReturn.setVisibility(View.GONE);

        if(journeys.size() > 1){

            Log.e("ReturnSeat","True");
            twoWay = true;
            seatInfoReturn = obj.getObj().getJourneys().get(1).getSeat_info();
            setPassenger2("RETURN",listPassengerReturn,txtSeatReturn,objV3,journeys.get(1).getDeparture_station(),journeys.get(1).getArrival_station());
            setSeat2(seatListReturn, seatInfoReturn);
            passengerSeatListReturn.setVisibility(View.VISIBLE);
        }


        listPassengerDepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                PasssengerInfoV2 selectedFromList = (PasssengerInfoV2) (listPassengerDepart.getItemAtPosition(myItemInt));
                //setSeat(seatInfoDepart);
                seatListReturn.setVisibility(View.GONE);
                seatListDepart.setVisibility(View.VISIBLE);
                //oneWay = true;
                //Clear Previous
                passengerSeatListV1.clearSelected();

                if(twoWay){
                    passengerSeatListV2.clearSelected();
                }
                seatTag1 = new ArrayList<>(1);

                if(myItemInt < passengerSize-1){
                    next1 = false;
                }else{
                    next1 = true;
                }

                passengerNoV1 = myItemInt;
                Log.e("passengerNoV1",Integer.toString(passengerNoV1));
                //Set selected
                LinearLayout clickedPassenger = (LinearLayout) myView.findViewById(R.id.passengerLinearLayout);
                clickedPassenger.setBackgroundColor(getResources().getColor(R.color.blue));
                selectedFromList.setSelected(true);
                selectedFromList.setActive(true);

            }
        });

        listPassengerReturn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                PasssengerInfoV2 selectedFromList = (PasssengerInfoV2) (listPassengerReturn.getItemAtPosition(myItemInt));

                seatListReturn.setVisibility(View.VISIBLE);
                seatListDepart.setVisibility(View.GONE);

                //setSeat(seatInfoReturn);
                //Clear Previous
                passengerSeatListV2.clearSelected();
                passengerSeatListV1.clearSelected();

                seatTag2 = new ArrayList<>(1);

                if(myItemInt < passengerSize-1){
                    next2 = false;
                }else{
                    next2 = true;
                }

                passengerNoV2 = myItemInt;
                //Set selected
                LinearLayout clickedPassenger = (LinearLayout) myView.findViewById(R.id.passengerLinearLayout);
                clickedPassenger.setBackgroundColor(getResources().getColor(R.color.blue));
                selectedFromList.setSelected(true);
                selectedFromList.setActive(true);


            }
        });


        btnSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<SeatSelect> goingSeat = new ArrayList<SeatSelect>();
                ArrayList<SeatSelect> returnSeat = new ArrayList<SeatSelect>();
                Boolean oneWayProceed = true;
                Boolean twoWayProceed = true;

                for(int x = 0 ; x < passengers.size() ; x++){

                    if(objV2.get(x).getSeat() != null)
                    {
                        SeatSelect obj = new SeatSelect();
                        obj.setCompartment_designator(objV2.get(x).getCompartment());
                        obj.setSeat_number(objV2.get(x).getSeat());
                        goingSeat.add(obj);
                        oneWayProceed = true;

                    }else{
                        oneWayProceed = false;
                    }

                }
                if(twoWay && oneWayProceed){
                    for(int x = 0 ; x < passengers.size() ; x++){

                        if(objV3.get(x).getSeat() != null){
                            SeatSelect obj = new SeatSelect();
                            obj.setCompartment_designator(objV3.get(x).getCompartment());
                            obj.setSeat_number(objV3.get(x).getSeat());
                            returnSeat.add(obj);
                            twoWayProceed = true;
                        }else{
                            twoWayProceed = false;
                        }

                    }
                }

                if(!oneWayProceed || !twoWayProceed){
                    Crouton.makeText(getActivity(), "Please select seat", Style.ALERT).show();
                }else{
                    //Validate
                    //Validate
                    SeatSelection seatObj = new SeatSelection();
                    seatObj.setBooking_id(bookingID);
                    seatObj.setSignature(signature);
                    seatObj.setGoing(goingSeat);
                    seatObj.setReturnFlight(returnSeat);

                    seatSelect(seatObj);
                }
            }
        });

        return view;
    }

    public void setPassenger1(String type,ExpandAbleGridView list,TextView txtSeat,List<PasssengerInfoV2> passengers,String depart,String arrival){

       // if(type.equals("DEPART")){
           passengers.get(0).setSelected(true);
         //   Log.e(type, type);

        //}
        txtSeat.setText(depart + " - " + arrival);
        passengerSeatListV1 = new PassengerSeatAdapterV1(getActivity(),passengers,this);
        list.setAdapter(passengerSeatListV1);

    }

    public void setPassenger2(String type,ExpandAbleGridView list,TextView txtSeat,List<PasssengerInfoV2> passengers,String depart,String arrival){

        // if(type.equals("DEPART")){
        //    passengers.get(0).setSelected(true);
        //   Log.e(type, type);

        //}
        Log.e("Seaat","return");
        txtSeat.setText(depart + " - " + arrival);
        passengerSeatListV2 = new PassengerSeatAdapterV2(getActivity(),passengers,this);
        list.setAdapter(passengerSeatListV2);

    }

    public void setSeat1(LinearLayout seatList,List<SeatInfo> seatInfo){

        int seatSize = seatInfo.size();
        int seatCount = 0;
        /*Set Seat Row*/
        List<String> tempSeatStorage = new ArrayList<String>();
        List<SeatSetup> tempSeatValue = new ArrayList<SeatSetup>();

        for(int seatNumber = 0 ; seatNumber < seatSize ; seatNumber++){

            boolean injectSeatSetup;

            String seatNumberEx = seatInfo.get(seatNumber).getSeat_number();
            String seatNumberWithoutAlphabet = seatNumberEx.substring(0, seatNumberEx.length()-1);
            String lastSeatCharacter = seatNumberEx.substring(seatNumberEx.length() - 1);

            if (tempSeatStorage.contains(seatNumberWithoutAlphabet)){
                /*SKIP*/
                injectSeatSetup = false;
            }else {
                tempSeatStorage.add(seatNumberWithoutAlphabet);
                seatCount++;
                injectSeatSetup = true;
            }

            if(injectSeatSetup){
                /*SeatNumberByRow*/
                ArrayList<SeatInfo> seat = new ArrayList<SeatInfo>();

                for(int seatNumberRow = 0 ; seatNumberRow < seatSize ; seatNumberRow++){

                    String seatNumberExx = seatInfo.get(seatNumberRow).getSeat_number();
                    String seatNumberWithoutAlphabex = seatNumberExx.substring(0, seatNumberExx.length() - 1);

                    if(tempSeatStorage.get(seatCount-1).equals(seatNumberWithoutAlphabex)){
                        SeatInfo rebuildSeatInfo = new SeatInfo();
                        rebuildSeatInfo.setCompartment_designator(seatInfo.get(seatNumberRow).getCompartment_designator());
                        rebuildSeatInfo.setSeat_number(seatInfo.get(seatNumberRow).getSeat_number());
                        rebuildSeatInfo.setSeat_type(seatInfo.get(seatNumberRow).getSeat_type());
                        rebuildSeatInfo.setStatus(seatInfo.get(seatNumberRow).getStatus());
                        seat.add(rebuildSeatInfo);
                    }
                }

                SeatSetup seatSetup = new SeatSetup();
                seatSetup.setSeatRow(tempSeatStorage.get(seatCount - 1));
                seatSetup.setSeatRowArray(seat);
                tempSeatValue.add(seatSetup);
            }

        }

        seatTag1 = new ArrayList<>(1);
        selectedSeatTag = new ArrayList<>(2);

        for (int label = 0; label < tempSeatStorage.size(); label++)
        {
            LinearLayout seatRow = new LinearLayout(getActivity());
            seatRow.setOrientation(LinearLayout.HORIZONTAL);
            seatRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);
            seatRow.setWeightSum(1);


            for (int x = 1; x < tempSeatValue.get(label).getSeatRowArray().size()+1; x++)
            {

                String seatNumber = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_number();
                String seatType = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_type();
                String seatStatus = tempSeatValue.get(label).getSeatRowArray().get(x-1).getStatus();
                final String compartment = tempSeatValue.get(label).getSeatRowArray().get(x-1).getCompartment_designator();

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.25f);


                final TextView txtDetailList = new TextView(getActivity());
                txtDetailList.setText(seatNumber);
                txtDetailList.setGravity(Gravity.CENTER);
                txtDetailList.setTextColor(getResources().getColor(R.color.white));
                txtDetailList.setPadding(5, 20, 5, 20);
                txtDetailList.setTag(seatNumber);
                txtDetailList.setBackgroundColor(getResources().getColor(R.color.grey_background));

                txtDetailList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (seatTag1.size() == 1) {

                            //TextView seatToRemove = (TextView) view.findViewWithTag(seatTag.get(0));
                            TextView seatToRemove = (TextView) view.findViewWithTag(passengerSeatListV1.getSelected(passengerNoV1));

                            seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                            seatToRemove.setClickable(true);
                            //seatToRemove.setTextColor(getResources().getColor(R.color.white));

                            seatTag1.remove(0);
                            seatTag1.add(txtDetailList.getText().toString());

                            txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                            txtDetailList.setClickable(false);

                            passengerSeatListV1.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                            passengerSeatListV1.setSelectedCompartmentSeat(compartment);

                            //passengerSeatListDepart.setSelectedSeatCompartment(passengerSeatListDepart.getSelected(passengerNo));
                            //selectedSeatTag.add(txtDetailList.getText().toString());
                            Log.e("X","X");
                        } else {

                            if(passengerSeatListV1.getSelected(passengerNoV1) != null) {
                                TextView seatToRemove = (TextView) view.findViewWithTag(passengerSeatListV1.getSelected(passengerNoV1));
                                seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                                seatToRemove.setClickable(true);
                            }

                            seatTag1.add(txtDetailList.getText().toString());
                            txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                            txtDetailList.setClickable(false);

                            passengerSeatListV1.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                            passengerSeatListV1.setSelectedCompartmentSeat(compartment);

                            Log.e("Y","Y");

                        }

                        //newLogic
                        //move to next passenger
                        //if(passengerSize < passengerNoV1 ){
                            if(next1){
                                Log.e(Integer.toString(passengerNoV1),Integer.toString(passengerSize));

                                if(passengerNoV1 < passengerSize-1){
                                    passengerSeatListV1.setNextPassengerSelected(passengerNoV1+1);
                                }else{
                                    if(twoWay){
                                        autoSelectReturnPassenger();
                                        passengerSeatListV2.autoSelectReturnPassenger();
                                        Log.e("Error", "True");
                                    }
                                }
                            }else{
                                if(passengerNoV1 < passengerSize-1){
                                    passengerSeatListV1.setNextPassengerSelected(passengerNoV1+1);
                                }
                            }
                        //}else{
                        //}

                        //seatTag1 = new ArrayList<>(1);
                        //selectedSeatTag = new ArrayList<>(2);
                    }

                });


                //"seat_type":"standard",
                //Set color and clickable

                if(seatType.equals("standard")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_standard));

                }else if(seatType.equals("preferred")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_preferred));

                }else if(seatType.equals("desired")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_desired));
                }

                if(!seatStatus.equals("available")){
                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.red));
                    txtDetailList.setClickable(false);
                }

                /*Only for 4 seat row flight - change accordingly*/
                if(x == 2){
                    param.setMargins(5, 5,20,5);
                }
                else if(x == 3){
                    param.setMargins(20, 5,5,5);
                }
                else if(x == 1){
                    param.setMargins(20,5,5,5);
                }
                else if(x == 4){
                    param.setMargins(5,5,20,5);
                }
                txtDetailList.setLayoutParams(param);


                seatRow.addView(txtDetailList);
            }

            seatList.addView(seatRow);

        }
    }

    public void setSeat2(LinearLayout seatList,List<SeatInfo> seatInfo){


        Log.e("Seatt","Return");

        int seatSize = seatInfo.size();
        int seatCount = 0;
        /*Set Seat Row*/
        List<String> tempSeatStorage = new ArrayList<String>();
        List<SeatSetup> tempSeatValue = new ArrayList<SeatSetup>();

        for(int seatNumber = 0 ; seatNumber < seatSize ; seatNumber++){

            boolean injectSeatSetup;

            String seatNumberEx = seatInfo.get(seatNumber).getSeat_number();
            String seatNumberWithoutAlphabet = seatNumberEx.substring(0, seatNumberEx.length()-1);
            String lastSeatCharacter = seatNumberEx.substring(seatNumberEx.length() - 1);

            if (tempSeatStorage.contains(seatNumberWithoutAlphabet)){
                /*SKIP*/
                injectSeatSetup = false;
            }else {
                tempSeatStorage.add(seatNumberWithoutAlphabet);
                seatCount++;
                injectSeatSetup = true;
            }

            if(injectSeatSetup){
                /*SeatNumberByRow*/
                ArrayList<SeatInfo> seat = new ArrayList<SeatInfo>();

                for(int seatNumberRow = 0 ; seatNumberRow < seatSize ; seatNumberRow++){

                    String seatNumberExx = seatInfo.get(seatNumberRow).getSeat_number();
                    String seatNumberWithoutAlphabex = seatNumberExx.substring(0, seatNumberExx.length() - 1);

                    if(tempSeatStorage.get(seatCount-1).equals(seatNumberWithoutAlphabex)){
                        SeatInfo rebuildSeatInfo = new SeatInfo();
                        rebuildSeatInfo.setCompartment_designator(seatInfo.get(seatNumberRow).getCompartment_designator());
                        rebuildSeatInfo.setSeat_number(seatInfo.get(seatNumberRow).getSeat_number());
                        rebuildSeatInfo.setSeat_type(seatInfo.get(seatNumberRow).getSeat_type());
                        rebuildSeatInfo.setStatus(seatInfo.get(seatNumberRow).getStatus());
                        seat.add(rebuildSeatInfo);
                    }
                }

                SeatSetup seatSetup = new SeatSetup();
                seatSetup.setSeatRow(tempSeatStorage.get(seatCount - 1));
                seatSetup.setSeatRowArray(seat);
                tempSeatValue.add(seatSetup);
            }

        }

        seatTag2 = new ArrayList<>(1);
        selectedSeatTag = new ArrayList<>(2);

        for (int label = 0; label < tempSeatStorage.size(); label++)
        {
            LinearLayout seatRow = new LinearLayout(getActivity());
            seatRow.setOrientation(LinearLayout.HORIZONTAL);
            seatRow.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);
            seatRow.setWeightSum(1);


            for (int x = 1; x < tempSeatValue.get(label).getSeatRowArray().size()+1; x++)
            {

                String seatNumber = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_number();
                String seatType = tempSeatValue.get(label).getSeatRowArray().get(x-1).getSeat_type();
                String seatStatus = tempSeatValue.get(label).getSeatRowArray().get(x-1).getStatus();
                final String compartment = tempSeatValue.get(label).getSeatRowArray().get(x-1).getCompartment_designator();

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.25f);


                final TextView txtDetailList = new TextView(getActivity());
                txtDetailList.setText(seatNumber);
                txtDetailList.setGravity(Gravity.CENTER);
                txtDetailList.setTextColor(getResources().getColor(R.color.white));
                txtDetailList.setPadding(5, 20, 5, 20);
                txtDetailList.setTag("RETURN" + "_" + seatNumber);
                txtDetailList.setBackgroundColor(getResources().getColor(R.color.grey_background));

                txtDetailList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (seatTag2.size() == 1) {

                            //TextView seatToRemove = (TextView) view.findViewWithTag(seatTag.get(0));
                            TextView seatToRemove = (TextView) view.findViewWithTag("RETURN"+"_"+passengerSeatListV2.getSelected(passengerNoV2));

                            seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                            seatToRemove.setClickable(true);
                            //seatToRemove.setTextColor(getResources().getColor(R.color.white));

                            seatTag2.remove(0);
                            seatTag2.add(txtDetailList.getText().toString());

                            txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                            txtDetailList.setClickable(false);

                        //    if(oneWay){
                            passengerSeatListV2.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                            passengerSeatListV2.setSelectedCompartmentSeat(compartment);
                         //   }else{
                         //       passengerSeatList.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                         //       passengerSeatList.setSelectedCompartmentSeat(compartment);
                         //   }

                            //passengerSeatListDepart.setSelectedSeatCompartment(passengerSeatListDepart.getSelected(passengerNo));
                            //selectedSeatTag.add(txtDetailList.getText().toString());
                            Log.e("X","X");
                        } else {

                            if(passengerSeatListV2.getSelected(passengerNoV2) != null){
                                TextView seatToRemove = (TextView) view.findViewWithTag("RETURN"+"_"+
                                        passengerSeatListV2.getSelected(passengerNoV2));
                                seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
                                seatToRemove.setClickable(true);
                            }

                            seatTag2.add(txtDetailList.getText().toString());
                            txtDetailList.setBackgroundColor(getResources().getColor(R.color.bright_green));
                            txtDetailList.setClickable(false);

                            passengerSeatListV2.setSelectedPasssengerSeat(txtDetailList.getText().toString());
                            passengerSeatListV2.setSelectedCompartmentSeat(compartment);
                            Log.e("Y", "Y");

                            if(next2){
                                Log.e(Integer.toString(passengerNoV2),Integer.toString(passengerSize));

                                if(passengerNoV2 < passengerSize-1){
                                    passengerSeatListV2.setNextPassengerSelected(passengerNoV2+1);
                                }
                            }else{
                                passengerSeatListV2.setNextPassengerSelected(passengerNoV2+1);
                            }
                        }

                    }

                });


                //"seat_type":"standard",
                //Set color and clickable

                if(seatType.equals("standard")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_standard));

                }else if(seatType.equals("preferred")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_preferred));

                }else if(seatType.equals("desired")){
                    txtDetailList.setClickable(true);
                    seatRow.setBackgroundColor(getResources().getColor(R.color.seat_desired));
                }

                if(!seatStatus.equals("available")){
                    txtDetailList.setBackgroundColor(getResources().getColor(R.color.red));
                    txtDetailList.setClickable(false);
                }

                /*Only for 4 seat row flight - change accordingly*/
                if(x == 2){
                    param.setMargins(5, 5,20,5);
                }
                else if(x == 3){
                    param.setMargins(20, 5,5,5);
                }
                else if(x == 1){
                    param.setMargins(20,5,5,5);
                }
                else if(x == 4){
                    param.setMargins(5,5,20,5);
                }
                txtDetailList.setLayoutParams(param);


                seatRow.addView(txtDetailList);
            }

            //Log.e("seatTag", seatTag.toString());
            seatList.addView(seatRow);

        }
    }

    public void clearSelectedOnFragmentV1(String seat){

        TextView seatToRemove = (TextView) view.findViewWithTag(seat);
        seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
        seatToRemove.setClickable(true);
        seatTag1 = new ArrayList<>(1);

    }

    public void clearSelectedOnFragmentV2(String seat){

        TextView seatToRemove = (TextView) view.findViewWithTag("RETURN_"+seat);
        seatToRemove.setBackgroundColor(getResources().getColor(R.color.grey_background));
        seatToRemove.setClickable(true);
        seatTag2 = new ArrayList<>(1);

    }

    public void clearSeatTag1(int passed){
        seatTag1 = new ArrayList<>(1);
        passengerNoV1 = passed;
        next1 = true;
        //passengerSeatListV1.clearSelected();
        //if(twoWay){
        //    passengerSeatListV2.clearSelected();
        //}
    }

    public void clearSeatTag2(int passed){

        Log.e("passed",Integer.toString(passed));
        seatTag2 = new ArrayList<>(1);
        passengerNoV2 = passed;
        next2 = true;
        //passengerSeatListV1.clearSelected();
        //if(twoWay){
        //    passengerSeatListV2.clearSelected();
        //}
    }

    public void autoSelectReturnPassenger(){

        seatListReturn.setVisibility(View.VISIBLE);
        seatListDepart.setVisibility(View.GONE);

        //setSeat(seatInfoReturn);
        //Clear Previous
        passengerSeatListV2.clearSelected();
        passengerSeatListV1.clearSelected();

        seatTag2 = new ArrayList<>(1);

    }

    public void goPaymentPage()
    {
        Intent paymentPage = new Intent(getActivity(), PaymentFlightActivity.class);
        getActivity().startActivity(paymentPage);
    }

    public void seatSelect(SeatSelection obj){

        initiateLoading(getActivity());
        presenter.seatSelect(obj);
    }

    @Override
    public void onSeatSelect(SeatSelectionReveice obj){
        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
           Intent intent = new Intent(getActivity(), ItinenaryActivity.class);
           intent.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
           getActivity().startActivity(intent);
       }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(result.size() > 0){
            Log.e("x","1");
            Gson gson = new Gson();
            SeatSelectionReveice obj = gson.fromJson(result.get(0).getCachedResult(), SeatSelectionReveice.class);
            onSeatSelect(obj);
        }else{
            Log.e("x","2");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
