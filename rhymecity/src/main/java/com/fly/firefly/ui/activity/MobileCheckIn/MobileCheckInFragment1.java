package com.fly.firefly.ui.activity.MobileCheckIn;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.BookingListAdapter;
import com.fly.firefly.ui.module.MobileCheckInModule1;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MobileCheckInFragment1 extends BaseFragment implements MobileCheckInPresenter.MobileCheckInView,Validator.ValidationListener {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.mobileCheckInNext1) Button mobileCheckInNext1;

    @NotEmpty
    @InjectView(R.id.editPnr) EditText editPnr;

    @NotEmpty
    @InjectView(R.id.txtDeparture) TextView txtDeparture;

    @NotEmpty
    @InjectView(R.id.txtArrive) TextView txtArrive;

    @InjectView(R.id.mobileCheckInPNRLayout)
    LinearLayout mobileCheckInPNRLayout;

    @InjectView(R.id.listView)
    ListView listView;

    private ArrayList<DropDownItem> dataFlightDeparture;
    private static ArrayList<DropDownItem> dataFlightArrival;
    private SharedPrefManager pref;
    private String DEPARTURE_FLIGHT = "Please choose your departure airport";
    private String ARRIVAL_FLIGHT = "Please choose your arrival airport";
    private static final String SCREEN_LABEL = "Mobile Check In";
    private Validator mValidator;
    private String loginStatus;
    private String storeUsername;
    private int fragmentContainerId;
    private BookingListAdapter adapter;
    private String signatureFromLocal;

    public static MobileCheckInFragment1 newInstance() {

        MobileCheckInFragment1 fragment = new MobileCheckInFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new MobileCheckInModule1(this)).inject(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.mobile_checkin_1, container, false);
        ButterKnife.inject(this, view);

        /*Preference Manager*/
        pref = new SharedPrefManager(MainFragmentActivity.getContext());

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> initLogin = pref.getLoginStatus();
        loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);

        HashMap<String, String> initUsername = pref.getUserEmail();
        storeUsername = initUsername.get(SharedPrefManager.USER_EMAIL);

        HashMap<String, String> initPassword = pref.getUserPassword();
        String storePassword = initPassword.get(SharedPrefManager.PASSWORD);


        if(loginStatus != null && loginStatus.equals("Y")) {
            if(Controller.connectionAvailable(getActivity())){
                initiateLoading(getActivity());
                presenter.getUserPNR(storeUsername,storePassword,"check_in");
                mobileCheckInNext1.setVisibility(View.GONE);
            }else{
                //Display No Internet connection
            }
        }else{
            mobileCheckInPNRLayout.setVisibility(View.VISIBLE);

            txtDeparture.setTag(DEPARTURE_FLIGHT);
            txtArrive.setTag(ARRIVAL_FLIGHT);

            /*Retrieve All Flight Data From Preference Manager - Display Flight Data*/
            JSONArray jsonFlight = getFlight(getActivity());
            dataFlightDeparture = new ArrayList<>();

            ArrayList<String> tempFlight = new ArrayList<>();

            /*Get All Airport - remove redundant*/
            List<String> al = new ArrayList<>();
            Set<String> hs = new LinkedHashSet<>();
            for (int i = 0; i < jsonFlight.length(); i++) {
                JSONObject row = (JSONObject) jsonFlight.opt(i);
                al.add(row.optString("location")+"-"+row.optString("location_code"));
            }
            hs.addAll(al);
            al.clear();
            al.addAll(hs);

            /*Display Airport*/
            for (int i = 0; i < al.size(); i++)
            {
                String flightSplit = al.get(i).toString();
                String[] str1 = flightSplit.split("-");
                String p1 = str1[0];
                String p2 = str1[1];

                DropDownItem itemFlight = new DropDownItem();
                itemFlight.setText(p1);
                itemFlight.setCode(p2);
                itemFlight.setTag("FLIGHT");
                dataFlightDeparture.add(itemFlight);

            }

        }

        /*Set PNR auto caps*/
        editPnr.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

       /*Departure Flight Clicked*/
        txtDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "txtDeparture");
                popupSelection(dataFlightDeparture, getActivity(), txtDeparture,true,view);
                txtArrive.setText("");
                //txtDeparture.setText("ARRIVAL AIRPORT");
            }
        });

         /*Arrival Flight Clicked*/
        txtArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnArrivalFlight");
                if(txtDeparture.getTag().toString().equals("NOT SELECTED"))
                {
                    popupAlert("Select Departure Airport First");
                }else{
                    popupSelection(dataFlightArrival, getActivity(), txtArrive,true,view);
                }
            }
        });

        mobileCheckInNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();

            }
        });
        return view;
    }

    @Override
    public void onUserPnrList(final ListBookingReceive obj){

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
            adapter = new BookingListAdapter(getActivity(),obj.getObj().getList_booking());
            listView.setAdapter(adapter);
            pref.setSignatureToLocalStorage(obj.getObj().getSignature());
            //pnrLayout.setVisibility(View.GONE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                ListBookingReceive.ListBooking selectedFromList = (ListBookingReceive.ListBooking) (listView.getItemAtPosition(myItemInt));

                Log.e("Selected PNR", selectedFromList.getPnr());
                Log.e("Username", pref.getUserEmail().toString());
                initiateLoading(getActivity());

                // ManageFlightObj manageFlightObj = new ManageFlightObj();
                // manageFlightObj.setPnr(selectedFromList.getPnr());
                // manageFlightObj.setUsername(storeUsername);

                // presenter.checkinFlight(flightObj);

                // presenter.onSendPNRV1(manageFlightObj);

                MobileCheckinObj flightObj = new MobileCheckinObj();
                flightObj.setPnr(selectedFromList.getPnr());
                flightObj.setUser_id(obj.getObj().getUser_id());
                flightObj.setDeparture_station(selectedFromList.getDeparture_station_code());
                flightObj.setArrival_station(selectedFromList.getArrival_station_code());
                flightObj.setSignature(obj.getObj().getSignature());

                searchFlightFragment(flightObj);

            }
        });

    }


    @Override
    public void onValidationSucceeded() {
        checkinFlight();
        Log.e("Success", "True");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);
             /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof TextView) {
                ((TextView) view).setError(splitErrorMsg[0]);
            }
            else if (view instanceof EditText){
                ((EditText) view).setError(splitErrorMsg[0]);
            }

            Log.e("Validation Failed",splitErrorMsg[0]);

        }

    }

    public void checkinFlight(){

        initiateLoading(getActivity());



        MobileCheckinObj flightObj = new MobileCheckinObj();
        flightObj.setPnr(editPnr.getText().toString());
        flightObj.setDeparture_station(txtDeparture.getTag().toString());
        flightObj.setArrival_station(txtArrive.getTag().toString());
        //flightObj.setSignature(signatureFromLocal);
        searchFlightFragment(flightObj);
                    /*FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_fragment_container, BF_FlightDetailFragment.newInstance(), "FLIGHT_DETAIL");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/

    }

    public void searchFlightFragment(MobileCheckinObj flightObj){

        presenter.checkinFlight(flightObj);
    }

    /*Filter Arrival Airport*/
    public static void filterArrivalAirport(String code) {
        Log.e("Filter", "TRUE");

        JSONArray jsonFlight = getFlight(MainFragmentActivity.getContext());
        dataFlightArrival = new ArrayList<>();

            /*Display Arrival*/
        for (int i = 0; i < jsonFlight.length(); i++)
        {
            JSONObject row = (JSONObject) jsonFlight.opt(i);
            if(code.equals(row.optString("location_code")) && row.optString("status").equals("Y")) {
                Log.e(code,row.optString("location_code"));

                DropDownItem itemFlight = new DropDownItem();
                itemFlight.setText(row.optString("travel_location"));
                itemFlight.setCode(row.optString("travel_location_code" + ""));
                itemFlight.setTag("FLIGHT_DEPARTURE");
                dataFlightArrival.add(itemFlight);

            }
        }
        Log.e("Arrive", dataFlightArrival.toString());

}

    @Override
    public void onCheckindataReceive(MobileCheckinReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
            Intent flight = new Intent(getActivity(), MobileCheckInActivity2.class);
            flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));
            //flight.putExtra("PNR", editPnr.getText().toString());
            //flight.putExtra("DEPARTURE_CODE", txtDeparture.getTag().toString());
            //flight.putExtra("ARRIVAL_CODE", txtArrive.getTag().toString());

            getActivity().startActivity(flight);
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
        Log.e("RESUME", "TRUE");
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
