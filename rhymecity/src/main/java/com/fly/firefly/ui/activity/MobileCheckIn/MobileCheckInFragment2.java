package com.fly.firefly.ui.activity.MobileCheckIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.MobileCheckInPassengerReceive;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Picker.CountryListDialogFragment;
import com.fly.firefly.ui.adapter.CheckInAdapter;
//import com.fly.firefly.ui.adapter.CheckInPassengerListAdapter;
import com.fly.firefly.ui.module.MobileCheckInModule2;
import com.fly.firefly.ui.object.MobileCheckInPassenger;
import com.fly.firefly.ui.object.PassengerInfo;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.RealmObjectController;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MobileCheckInFragment2 extends BaseFragment implements MobileCheckInPresenter.MobileCheckInView2 {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.mobileCheckInNext2) Button mobileCheckInNext2;

    //@InjectView(R.id.listview)ExpandAbleGridView listview;
    @InjectView(R.id.listview)ExpandableListView listview;

    //@InjectView(R.id.btnLogin) Button btnLogin;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Mobile Check In Detail";
    private MobileCheckinReceive obj;
    private String PNR;
    private String arrivalCode,departureCode;
    //private CheckInPassengerListAdapter adapter;
    private CheckInAdapter adapter;
    private Boolean allCheckIn = true;
    private View header;

    public static MobileCheckInFragment2 newInstance(Bundle bundle) {

        MobileCheckInFragment2 fragment = new MobileCheckInFragment2();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new MobileCheckInModule2(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mobile_checkin_2, container, false);
        ButterKnife.inject(this, view);

        //add header to listview
        header = inflater.inflate(R.layout.mobile_checkin_2_header, listview, false);
        listview.addHeaderView(header, null, false);

        Bundle bundle = getArguments();
        if(bundle.containsKey("FLIGHT_OBJ")){
            String flightSummary = bundle.getString("FLIGHT_OBJ");

            Gson gson = new Gson();
            obj = gson.fromJson(flightSummary, MobileCheckinReceive.class);

            PNR = obj.getPnr();
            departureCode = obj.getDeparture_station_code();
            arrivalCode = obj.getArrival_station_code();

            Log.e("Status",obj.getStatus());

            TextView flightdate = (TextView) header.findViewById(R.id.flightdate);
            TextView flightnumber = (TextView) header.findViewById(R.id.flightnumber);
            TextView stationcode = (TextView) header.findViewById(R.id.stationcode);
            TextView departuretime = (TextView) header.findViewById(R.id.departuretime);

            stationcode.setText(obj.getFlight_detail().getStation_code());
            flightdate.setText(obj.getFlight_detail().getFlight_date());
            flightnumber.setText(obj.getFlight_detail().getFlight_number());
            departuretime.setText(obj.getFlight_detail().getDeparture_time());

        }

        //adapter = new CheckInPassengerListAdapter(getActivity(),obj.getObj().getPassengers(),this,getActivity().getSupportFragmentManager());
        adapter = new CheckInAdapter(getActivity(),obj.getPassengers(),this,getActivity().getSupportFragmentManager());
        listview.setAdapter(adapter);
        listview.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        listview.setGroupIndicator(null);

        mobileCheckInNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean goNext = false;

                mobileCheckInNext2.requestFocus();
                //validate atleast one passenger is selected
                for(int i = 0 ; i < obj.getPassengers().size() ; i++) {
                    if(obj.getPassengers().get(i).getChecked() != null){
                        goNext = true;
                    }
                }

                if(goNext){
                    next();
                }else{
                    setAlertDialog(getActivity(),"Atleast one passenger selected","Check - In.");
                }

            }
        });


        for(int y = 0 ; y < obj.getPassengers().size() ; y++){
            allCheckIn = obj.getPassengers().get(y).getStatus().equals("Checked In");
        }
        if(allCheckIn){
            mobileCheckInNext2.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    /*Public-Inner Func*/
    public void next() {
        ArrayList<PassengerInfo> mainObj = new ArrayList<PassengerInfo>();

        initiateLoading(getActivity());
        for(int i = 0 ; i < obj.getPassengers().size() ; i++){

            PassengerInfo passengerObj = new PassengerInfo();
            passengerObj.setExpiration_date(obj.getPassengers().get(i).getExpiration_date());
            passengerObj.setDocument_number(obj.getPassengers().get(i).getDocument_number());
            passengerObj.setIssuing_country(obj.getPassengers().get(i).getIssuing_country());
            passengerObj.setTravel_document(obj.getPassengers().get(i).getTravel_document());
            passengerObj.setPassenger_number(obj.getPassengers().get(i).getPassenger_number());

            String bonuslink;
            if(obj.getPassengers().get(i).getBonuslink() == null){
                bonuslink = null;
            }else{
                bonuslink = obj.getPassengers().get(i).getBonuslink();
            }
            passengerObj.setBonusLink(bonuslink);

            if(obj.getPassengers().get(i).getChecked() == null){
                passengerObj.setStatus("N");
            }else{
                passengerObj.setStatus(obj.getPassengers().get(i).getChecked());
            }
            mainObj.add(passengerObj);

        }

        MobileCheckInPassenger obj2 = new MobileCheckInPassenger();
        obj2.setPassengers(mainObj);
        obj2.setPnr(PNR);
        obj2.setDeparture_station_code(departureCode);
        obj2.setArrival_station_code(arrivalCode);
        obj2.setSignature(obj.getSignature());

        presenter.checkInPassenger(obj2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {
                DropDownItem selectedCountry = data.getParcelableExtra(CountryListDialogFragment.EXTRA_COUNTRY);
                adapter.setSelectedCountry(selectedCountry.getText(),selectedCountry.getCode());
            }
        }
    }

    @Override
    public void onCheckInPassenger(MobileCheckInPassengerReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
          Intent next = new Intent(getActivity(), MobileCheckInActivity3.class);
          next.putExtra("MOBILE_CHECK_IN", (new Gson()).toJson(obj));
          getActivity().startActivity(next);
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

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
