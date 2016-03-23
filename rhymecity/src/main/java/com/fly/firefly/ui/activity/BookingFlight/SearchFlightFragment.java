package com.fly.firefly.ui.activity.BookingFlight;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.BusProvider;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Register.RegisterActivity;
import com.fly.firefly.ui.module.SearchFlightModule;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.DatePickerObj;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.AESCBC;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.LocalNotification;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import io.realm.Realm;
import io.realm.RealmResults;

public class SearchFlightFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,BookingPresenter.SearchFlightView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.btnReturn) LinearLayout btnReturn;
    @InjectView(R.id.btnOneWay) LinearLayout btnOneWay;
    @InjectView(R.id.returnDateBlock) LinearLayout returnDateBlock;

    @InjectView(R.id.btnAdultIncrease) LinearLayout btnAdultIncrease;
    @InjectView(R.id.btnAdultDecrease) LinearLayout btnAdultDecrease;

    @InjectView(R.id.btnChildIncrease) ImageView btnChildIncrease;
    @InjectView(R.id.btnChildDecrease) ImageView btnChildDecrease;

    @InjectView(R.id.btnInfantIncrease) LinearLayout btnInfantIncrease;
    @InjectView(R.id.btnInfantDecrease) LinearLayout btnInfantDecrease;

    @InjectView(R.id.txtAdultTotal) TextView txtAdultTotal;
    @InjectView(R.id.txtChildTotal) TextView txtChildTotal;
    @InjectView(R.id.txtInfantTotal) TextView txtInfantTotal;

    @InjectView(R.id.btnSearchFlight) Button btnSearchFlight;

    @InjectView(R.id.btnDepartureFlight)
    LinearLayout btnDepartureFlight;

    @InjectView(R.id.btnArrivalFlight)
    LinearLayout btnArrivalFlight;

    @InjectView(R.id.txtArrivalFlight)
    TextView txtArrivalFlight;

    @InjectView(R.id.txtDepartureFlight)
    TextView txtDepartureFlight;

    @InjectView(R.id.departureBlock)
    LinearLayout departureBlock;

    @InjectView(R.id.bookFlightDepartureDate)
    TextView bookFlightDepartureDate;

    @InjectView(R.id.bookFlightReturnDate)
    TextView bookFlightReturnDate;

    private final String RETURN = "RETURN";
    private final String ONEWAY = "ONEWAY";
    private final String ADULT = "ADULT";
    private final String CHILDREN = "CHILDREN";
    private final String INFANT = "INFANT";
    private String flightType = "1";
    private static final String SCREEN_LABEL = "Search Flight";

    private int totalAdult = 1;
    private int totalChildren = 0;
    private int totalInfant = 0;

    private int fragmentContainerId;
    private boolean blockAdult = false;
    private boolean blockAdultNumber = false;

    private boolean blockChild = false;
    private boolean blockChildNumber = false;

    private boolean blockInfant = false;
    private boolean blockInfantNumber = false;
    private SharedPrefManager pref;
    private ArrayList<DropDownItem> dataFlightDeparture;
    private static ArrayList<DropDownItem> dataFlightArrival;
    private DatePickerObj date;
    private int a,b,c,d = 0;

    private String DEPARTURE_FLIGHT = "Please choose your departure airport";
    private String ARRIVAL_FLIGHT = "Please choose your arrival airport";
    private String DEPARTURE_FLIGHT_DATE = "Please choose your departure date.";
    private String ARRIVAL_FLIGHT_DATE = "Please choose your return date.";
    private String FLIGHT_OBJECT = "FLIGHT_OBJECT";

    private String DEPARTURE_DATE_PICKER = "DEPARTURE_DATE_PICKER";
    private String RETURN_DATE_PICKER = "RETURN_DATE_PICKER";
    private String PICKER;
    public static final String DATEPICKER_TAG = "datepicker";
    private AlertDialog dialog;
    private Validator mValidator;

    private SearchFlightObj flightObj;

    public static SearchFlightFragment newInstance() {

        SearchFlightFragment fragment = new SearchFlightFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SearchFlightModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.search_flight, container, false);
        ButterKnife.inject(this, view);

        /*DatePicker Setup - Failed to make it global*/
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);

        //NEED TO CREATE DIFFERENT INSTANCE FOR EACH CALENDAR TO AVOID DISPLAYING PREVIOUS SELECTED.
        final DatePickerDialog datePickerDialog1 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final DatePickerDialog datePickerDialog2 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        /*Preference Manager*/
        pref = new SharedPrefManager(getActivity());

        txtDepartureFlight.setTag(DEPARTURE_FLIGHT);
        txtArrivalFlight.setTag(ARRIVAL_FLIGHT);

        bookFlightDepartureDate.setTag(DEPARTURE_FLIGHT_DATE);
        bookFlightReturnDate.setTag(ARRIVAL_FLIGHT_DATE);

        /*Retrieve All Flight Data From Preference Manager - Display Flight Data*/
        JSONArray jsonFlight = getFlight(getActivity());
        dataFlightDeparture = new ArrayList<>();

        ArrayList<String> tempFlight = new ArrayList<>();

        /*Get All Airport - remove redundant*/
        List<String> al = new ArrayList<>();
        Set<String> hs = new LinkedHashSet<>();
        for (int i = 0; i < jsonFlight.length(); i++) {
            JSONObject row = (JSONObject) jsonFlight.opt(i);
            Log.e("status", row.optString("status"));
            if(!row.optString("status").equals("N")){
                al.add(row.optString("location")+"-"+row.optString("location_code"));
            }
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
            itemFlight.setText(p1+ " ("+p2+")");
            itemFlight.setCode(p2);
            itemFlight.setTag("FLIGHT");
            dataFlightDeparture.add(itemFlight);

        }

        /*Departure Flight Clicked*/
        btnDepartureFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnDepartureFlight");
                popupSelection(dataFlightDeparture, getActivity(), txtDepartureFlight,true,view);
                txtArrivalFlight.setText("ARRIVAL AIRPORT");
            }
        });

        /*Arrival Flight Clicked*/
        btnArrivalFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnArrivalFlight");
                if(txtDepartureFlight.getTag().toString().equals("NOT SELECTED"))
                {
                    popupAlert("Select Departure Airport First");
                }else{
                    popupSelection(dataFlightArrival, getActivity(), txtArrivalFlight,true,view);
                }
            }
        });

        /*Arrival Date Clicked*/
        departureBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "departureBlock");

                datePickerDialog1.setYearRange(year, year + 1);
                datePickerDialog1.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);

                PICKER = DEPARTURE_DATE_PICKER;
            }
        });

        /*Departure Date Clicked*/
        returnDateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog2.setYearRange(year, year + 1);
                datePickerDialog2.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);

                PICKER = RETURN_DATE_PICKER;
                AnalyticsApplication.sendEvent("Click", "returnDateblock");

            }
        });

        /*Initial*/
        btnOneWay.setBackgroundColor(getResources().getColor(R.color.grey));
        txtAdultTotal.setText("1");
        txtChildTotal.setText("0");

        //txtInfantTotal.setText("0");

        /*Return Button Clicked*/
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnReturn");
                switchWay(RETURN);
            }
        });

        /*One Way Button Clicked*/
        btnOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnOneWay");
                switchWay(ONEWAY);
            }
        });

        /*Add & Remove ADULT Button */
        btnAdultIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnAdultIncrease");
                if(!blockAdult && totalAdult < 9){
                   totalAdult++;
                   setPassengerTotal(ADULT);
               }else{
                    Utils.toastNotification(getActivity(), "Adult limit is 9");
                }
            }
        });

        btnAdultDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnAdultDecrease");
                if(totalAdult == 1){
                    blockAdultNumber = true;
                }

                Log.e(Integer.toString(totalAdult),Integer.toString(totalInfant));
                if((totalAdult-1) < totalInfant){
                    Utils.toastNotification(getActivity(),"Adult cannot be less than infant");
                }else{
                    if(!blockAdultNumber){
                        totalAdult--;
                        setPassengerTotal(ADULT);
                    }else if(blockAdultNumber && totalAdult > 9){
                        totalAdult--;
                        totalAdult--;
                        setPassengerTotal(ADULT);
                        Log.e("blockChildNumber", "True");
                        Log.e("totalChildren", Integer.toString(totalAdult));
                    }
                }


            }
        });

        /* END ADULT*/

        /*Add & Remove CHILDREN Button */
        btnChildIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnChildIncrease");
                if(!blockChild && totalChildren <= 9){
                    totalChildren++;
                    setPassengerTotal(CHILDREN);
                }
            }
        });

        btnChildDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnChildDecrease");

                if(totalChildren == 0){
                    blockChildNumber = true;
                }

                if(!blockChildNumber){

                    totalChildren--;
                    setPassengerTotal(CHILDREN);

                }else if(blockChildNumber && totalChildren > 9){
                    totalChildren--;
                    totalChildren--;
                    setPassengerTotal(CHILDREN);

                }

            }
        });

        /* END - CHILDREN */

        /*Add & Remove INFANT Button */
        btnInfantIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnInfantIncrease");

                if(totalInfant < totalAdult ){
                    if(!blockInfant && totalInfant < 4){
                        totalInfant++;
                        setPassengerTotal(INFANT);
                    }else{
                        Utils.toastNotification(getActivity(),"Infant limit is 4");
                    }
                }else{
                    Utils.toastNotification(getActivity(),"Infant cannot be more than adult");
                }



            }
        });

        btnInfantDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnInfantDecrease");
                if(totalInfant == 0){
                    blockInfantNumber = true;
                }
                if(!blockInfantNumber){
                    totalInfant--;
                    setPassengerTotal(INFANT);
                }else if(blockInfantNumber && totalInfant > 9){
                    totalInfant--;
                    totalInfant--;
                    setPassengerTotal(INFANT);

                }
            }
        });

        /* END - INFANT */

        btnSearchFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnSearchFlight");
                String df = txtDepartureFlight.getTag().toString();
                String af = txtArrivalFlight.getTag().toString();

                String d1 = bookFlightDepartureDate.getTag().toString();
                String d2 = bookFlightReturnDate.getTag().toString();

                if(df.equals(DEPARTURE_FLIGHT)){

                    popupAlert(DEPARTURE_FLIGHT);

                }else if (af.equals(ARRIVAL_FLIGHT )){

                    popupAlert(ARRIVAL_FLIGHT);

                }else if (d1.equals(DEPARTURE_FLIGHT_DATE)){

                    popupAlert(DEPARTURE_FLIGHT_DATE);

                }else if (d2.equals(ARRIVAL_FLIGHT_DATE) && flightType.equals("1")){

                    popupAlert(ARRIVAL_FLIGHT_DATE);

                }else if(LocalNotification.dateCompare(d1)){
                    Utils.toastNotification(getActivity(), "Departure date should not be earlier than today.");
                }else if(!d2.equals(ARRIVAL_FLIGHT_DATE) && LocalNotification.dateCompare(d2) && flightType.equals("1")){
                        Utils.toastNotification(getActivity(), "Return date should not be earlier than today.");
                }else if(!d1.equals(DEPARTURE_FLIGHT_DATE) && !d2.equals(ARRIVAL_FLIGHT_DATE) && flightType.equals("1")){
                    if(LocalNotification.compare(d1,d2)){
                        Utils.toastNotification(getActivity(), "Return date cannot be earlier than departure date");
                    }else{
                        searchFlight();
                    }
                    Log.e("3","3");
                }else{
                    searchFlight();
                }

            }
        });


        return view;
    }

    public void searchFlight(){

        initiateLoading(getActivity());

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        String signatureFromLocal = initSignature.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> initUserEmail = pref.getUserEmail();
        String userEmail = initUserEmail.get(SharedPrefManager.USER_EMAIL);

        HashMap<String, String> initUserPassword = pref.getUserPassword();
        String userPassword = initUserPassword.get(SharedPrefManager.PASSWORD);

        flightObj = new SearchFlightObj();
        flightObj.setAdult(txtAdultTotal.getText().toString());
        flightObj.setChildren(txtChildTotal.getText().toString());
        flightObj.setInfant(txtInfantTotal.getText().toString());
        flightObj.setType(flightType);
        flightObj.setDeparture_station(txtDepartureFlight.getTag().toString());
        flightObj.setDeparture_date(bookFlightDepartureDate.getTag().toString());
        flightObj.setArrival_station(txtArrivalFlight.getTag().toString());
        if( userEmail == null && userPassword == null){
            userEmail = "";
            userPassword = "";
        }
        flightObj.setUsername(userEmail);
        flightObj.setPassword(userPassword);

        /*Return Flight*/
        String returnDate;
        if(flightType.equals("1")){
            returnDate = bookFlightReturnDate.getTag().toString();
        }else{
            returnDate = "";
        }
        //String returnDate = flightType.equals("1") ? bookFlightReturnDate.getTag().toString() : "";
        flightObj.setReturn_date(returnDate);

        //flightObj.setSignature(signatureFromLocal);
        searchFlightFragment(flightObj);

                    /*FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_fragment_container, BF_FlightDetailFragment.newInstance(), "FLIGHT_DETAIL");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/

    }

    public void searchFlightFragment(SearchFlightObj flightObj){

       presenter.searchFlight(flightObj);
    }


    /*Filter Arrival Airport*/
    public static void filterArrivalAirport(String code) {
        Log.e("Filter","TRUE");

        JSONArray jsonFlight = getFlight(MainFragmentActivity.getContext());
        dataFlightArrival = new ArrayList<>();

            /*Display Arrival*/
            for (int i = 0; i < jsonFlight.length(); i++)
            {
                JSONObject row = (JSONObject) jsonFlight.opt(i);
                if(code.equals(row.optString("location_code")) && row.optString("status").equals("Y")) {
                    Log.e(code, row.optString("location_code"));

                    DropDownItem itemFlight = new DropDownItem();
                    itemFlight.setText(row.optString("travel_location")+" ("+row.optString("travel_location_code")+")");
                    itemFlight.setCode(row.optString("travel_location_code" +""));
                    itemFlight.setTag("FLIGHT_DEPARTURE");
                    dataFlightArrival.add(itemFlight);

                }
            }
        Log.e("Arrive", dataFlightArrival.toString());

    }

    /*Public-Inner Func*/
    public void goRegisterPage() {
        Intent loginPage = new Intent(getActivity(), RegisterActivity.class);
        getActivity().startActivity(loginPage);
    }

    public void goFlightDetailPage() {
        Intent flightDetail = new Intent(getActivity(), FireflyFlightListActivity.class);
        getActivity().startActivity(flightDetail);
    }

    //Switch Flight Type
    public void switchWay(String way) {
        if(way == RETURN) {
            returnDateBlock.setVisibility(View.VISIBLE);
            btnReturn.setBackgroundColor(getResources().getColor(R.color.white));
            btnOneWay.setBackgroundColor(getResources().getColor(R.color.grey));

            flightType = "1";
        }else {
            returnDateBlock.setVisibility(View.GONE);
            btnReturn.setBackgroundColor(getResources().getColor(R.color.grey));
            btnOneWay.setBackgroundColor(getResources().getColor(R.color.white));
            flightType = "0";

        }
    }

    public void setPassengerTotal(String passenger) {

        int totalPassenger = totalAdult+totalChildren+totalInfant;

        Log.e("totalPassenger",Integer.toString(totalPassenger));

        if(totalPassenger > 14){


            if(passenger == ADULT){ totalAdult--; }
            else if(passenger == CHILDREN){ totalChildren--; }
            else{ totalInfant--; }

            Utils.toastNotification(getActivity(), "13" + " Passenger Per Booking");

        }else{

            if (passenger == ADULT) {
                if(totalAdult < 10 && totalAdult > 0) {
                    txtAdultTotal.setText(Integer.toString(totalAdult));
                    blockAdult = false;
                    blockAdultNumber = false;
                }
                else if(totalAdult == 9) {
                    Utils.toastNotification(getActivity(), "Limit is 9");
                    blockAdult = true;
                }
                else
                {
                    blockAdultNumber = true;
                }

            }
            else if(passenger == CHILDREN)
            {
                if(totalChildren < 10 && totalChildren >= 0) {
                    Log.e("A",Integer.toString(totalChildren));
                    txtChildTotal.setText(Integer.toString(totalChildren));
                    blockChild = false;
                    blockChildNumber = false;
                }else if(totalChildren == 9) {
                    Utils.toastNotification(getActivity(), "Limit is 9");
                    Log.e("Reach Limit","children");
                    blockChild = true;
                }
                else
                {
                    blockChildNumber = true;
                }
            }
            else if(passenger == INFANT)
            {
                if(totalInfant <= 4  && totalInfant >= 0) {
                    txtInfantTotal.setText(Integer.toString(totalInfant));
                    blockInfant = false;
                    blockInfantNumber = false;
                    Log.e("?",Integer.toString(totalInfant));

                }
                else if(totalInfant == 5) {
                    Utils.toastNotification(getActivity(), "Infant limit is 4");
                    Log.e("??", Integer.toString(totalInfant));
                    blockInfant = true;
                }
                else
                {
                    Log.e("???",Integer.toString(totalInfant));
                    blockInfantNumber = true;
                }
            }
        }
    }

    @Override
    public void onBookingDataReceive(SearchFlightReceive obj) {

        dismissLoading();
        Log.e("XXX", "XXX");

        Gson gson = new Gson();
        String countryList = gson.toJson(obj);
        pref.setFlightType(obj.getType());

        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //Log.e("Status",obj.getStatus());
            pref.setSignatureToLocalStorage(obj.getSignature());
            Log.e("siganature", obj.getSignature());
            Intent flight = null;
            if(obj.getType().equals("MH")){
                   flight = new Intent(getActivity(), CodeShareFlightListActivity.class);

            }else{
               flight = new Intent(getActivity(), FireflyFlightListActivity.class);
            }
            flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));
            flight.putExtra("FLIGHT_TYPE", flightType );
            flight.putExtra("ADULT", txtAdultTotal.getText().toString() );
            flight.putExtra("INFANT", txtInfantTotal.getText().toString());
            flight.putExtra("DEPARTURE_DATE", bookFlightDepartureDate.getTag().toString());
            String date;
            if(flightType.equals("0")){
                date = "";
            }else{
                date = bookFlightReturnDate.getTag().toString();
            }
            flight.putExtra("RETURN_DATE", date);
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
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(result.size() > 0){
            Log.e("x","1");
            Gson gson = new Gson();
            SearchFlightReceive obj = gson.fromJson(result.get(0).getCachedResult(), SearchFlightReceive.class);
            onBookingDataReceive(obj);
        }else{
            Log.e("x","2");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        Log.e("Screen Paused", "True");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Screen Stop", "True");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 10) {
            varMonth = "0";
        }
        if(day < 10){
            varDay = "0";
        }
        if(PICKER.equals(DEPARTURE_DATE_PICKER)) {
            bookFlightDepartureDate.setText(day + "/"+varMonth + "" + (month+1)+ "/" + year);
            bookFlightDepartureDate.setTag(year + "-" + varMonth + "" + (month+1) + "-" + varDay + "" + day);
        }else if(PICKER.equals(RETURN_DATE_PICKER)){
            bookFlightReturnDate.setText(day + "/"+varMonth + "" + (month+1)+ "/" + year);
            bookFlightReturnDate.setTag(year + "-" + varMonth + "" + (month+1) + "-" + varDay + "" + day);
        }else{
            //DeadBlock
        }
    }
}
