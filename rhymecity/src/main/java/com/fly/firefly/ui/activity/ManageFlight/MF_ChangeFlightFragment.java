package com.fly.firefly.ui.activity.ManageFlight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ChangeSearchFlightReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.BookingFlight.CodeShareFlightListActivity;
import com.fly.firefly.ui.activity.BookingFlight.FireflyFlightListActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.ManageChangeFlightDate;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.ChangeFlight;
import com.fly.firefly.ui.object.GetChangeFlight;
import com.fly.firefly.ui.object.GetFlightAvailability;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.LocalNotification;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class MF_ChangeFlightFragment extends BaseFragment implements  DatePickerDialog.OnDateSetListener,ManageFlightPrenter.GetFlightView {

    @Inject
    ManageFlightPrenter presenter;

    //@InjectView(R.id.search_edit_text) EditText searchEditText;
    //@InjectView(R.id.txtChangeFlightDatePicker1)
    //EditText txtChangeFlightDatePicker1;

    @InjectView(R.id.txtDepartureFlight)
    TextView txtDepartureFlight;

    @InjectView(R.id.txtArrivalFlight)
    TextView txtArrivalFlight;

    @InjectView(R.id.txtReturnDeparture)
    TextView txtReturnDeparture;

    @InjectView(R.id.txtReturnArrival)
    TextView txtReturnArrival;

    @InjectView(R.id.txtDepartureDate)
    TextView txtDepartureDate;

    @InjectView(R.id.txtReturnDepartureDate)
    TextView txtReturnDepartureDate;

    @InjectView(R.id.returnLayout)
    LinearLayout returnLayout;

    @InjectView(R.id.departureDateBlock)
    LinearLayout departureDateBlock;

    @InjectView(R.id.returnDateBlock)
    LinearLayout returnDateBlock;

    @InjectView(R.id.btnSearchFlight)
    Button btnSearchFlight;

    @InjectView(R.id.checkGoing)
    CheckBox checkGoing;

    @InjectView(R.id.checkReturn)
    CheckBox checkReturn;

    private String DEPARTURE_FLIGHT_DATE = "Please choose your departure date.";
    private String ARRIVAL_FLIGHT_DATE = "Please choose your return date.";
    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String pnr,username,bookingId,signature;
    private String DEPARTURE_DATE_PICKER = "DEPARTURE_DATE_PICKER";
    private String RETURN_DATE_PICKER = "RETURN_DATE_PICKER";
    private String PICKER;
    private static final String DATEPICKER_TAG = "datepicker";
    private DatePickerDialog datePickerDialog1,datePickerDialog2;
    private ChangeSearchFlightReceive localObj;
    private boolean disableDeparture = false;
    private boolean disableReturn = false;
    private boolean retrieveFlightInfo = false;
    private int year;
    public String SCREEN_LABEL;

    public static MF_ChangeFlightFragment newInstance(Bundle bundle) {

        MF_ChangeFlightFragment fragment = new MF_ChangeFlightFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageChangeFlightDate(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.change_flight, container, false);
        ButterKnife.inject(this, view);
        SCREEN_LABEL = "Edit Search Flight";

        //checkGoing


        /*Retrieve bundle data*/
        Bundle bundle = getArguments();
        String flightSummary = bundle.getString("ITINENARY_INFORMATION");
        pref = new SharedPrefManager(getActivity());

        /*DatePicker Setup - Failed to make it global*/
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);


        //adult = bundle.getString(ADULT);
        //infant = bundle.getString(INFANT);
        //pref = new SharedPrefManager(getActivity());
        Gson gson = new Gson();
        final FlightSummaryReceive obj = gson.fromJson(flightSummary, FlightSummaryReceive.class);

        pnr = obj.getItenerary_information().getPnr();
        bookingId = obj.getBooking_id();
        username = obj.getContact_information().getEmail();

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        //txtDepartureDate.setTag(DEPARTURE_FLIGHT_DATE);
        //txtReturnDepartureDate.setTag(ARRIVAL_FLIGHT_DATE);

        GetFlightAvailability getObj = new GetFlightAvailability();
        getObj.setSignature(signature);
        getObj.setBooking_id(bookingId);
        getObj.setPnr(pnr);
        initiateLoading(getActivity());
        presenter.onGetFlightAvailability(getObj);

        checkGoing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                      if (isChecked) {
                                                          if (disableDeparture) {
                                                              BaseFragment.setAlertDialog(getActivity(), "Unable to change flight date.","Flight Change");
                                                              checkGoing.setChecked(false);
                                                          }
                                                      }
                                                  }
                                              }
        );


        checkReturn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                      if(isChecked){
                                                          if(disableReturn){
                                                              BaseFragment.setAlertDialog(getActivity(),"Unable to change flight date.","Flight Change");
                                                              checkReturn.setChecked(false);
                                                          }
                                                      }
                                                  }
                                              }
        );

        btnSearchFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkGoing.isChecked() || checkReturn.isChecked()){
                    requestFlight();
                }else{
                    croutonAlert(getActivity(), "No flight checked!");
                }

            }
        });




        return view;
    }

    public void requestFlight(){

        retrieveFlightInfo = true;

        GetChangeFlight getFlightObj = new GetChangeFlight();

        ChangeFlight goingObj = new ChangeFlight();
        goingObj.setDeparture_station(localObj.getJourneys().get(0).getDeparture_station());
        goingObj.setArrival_station(localObj.getJourneys().get(0).getArrival_station());
        goingObj.setDeparture_date(txtDepartureDate.getTag().toString());
        if(checkGoing.isChecked()){
            goingObj.setStatus("Y");
            Log.e("Checked","true");
        }else{
            goingObj.setStatus("N");
            Log.e("Checked", "False");

        }

        ChangeFlight returnObj = new ChangeFlight();
        if (localObj.getJourneys().size() > 1) {
            returnObj.setDeparture_station(localObj.getJourneys().get(1).getDeparture_station());
            returnObj.setArrival_station(localObj.getJourneys().get(1).getArrival_station());
            returnObj.setDeparture_date(txtReturnDepartureDate.getTag().toString());
            if(checkReturn.isChecked()){
                returnObj.setStatus("Y");
            }else{
                returnObj.setStatus("N");
            }
            getFlightObj.setReturn_flight(returnObj);
        }
        getFlightObj.setReturn_flight(returnObj);

        getFlightObj.setPnr(pnr);
        getFlightObj.setBooking_id(bookingId);
        getFlightObj.setSignature(signature);
        getFlightObj.setGoing_flight(goingObj);

        initiateLoading(getActivity());
        presenter.onNewFlightDate(getFlightObj);
    }

    @Override
    public void onGetFlightSuccess(ChangeSearchFlightReceive obj) {
        dismissLoading();
        localObj = obj;
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //modify date
            String departureDate = obj.getJourneys().get(0).getDeparture_date();
            String[] splitDate = departureDate.split("/");

            datePickerDialog1 = DatePickerDialog.newInstance(this, Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[1])-1, Integer.parseInt(splitDate[0]));
            datePickerDialog1.setYearRange(year, year + 1);

            if(obj.getJourneys().get(0).getFlight_status().equals("Available")){
                /*Arrival Date Clicked*/
                departureDateBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //AnalyticsApplication.sendEvent("Click", "departureBlock");
                        datePickerDialog1.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
                        PICKER = DEPARTURE_DATE_PICKER;
                    }
                });
            }else{
                disableDeparture = true;
            }

            txtDepartureFlight.setText(obj.getJourneys().get(0).getDeparture_station_name());
            txtDepartureFlight.setTag(obj.getJourneys().get(0).getDeparture_station());

            txtArrivalFlight.setText(obj.getJourneys().get(0).getArrival_station_name());
            txtArrivalFlight.setTag(obj.getJourneys().get(0).getArrival_station());

            txtDepartureDate.setText(obj.getJourneys().get(0).getDeparture_date());

            txtDepartureDate.setTag(reformatDOB2(obj.getJourneys().get(0).getDeparture_date()));
            if (obj.getJourneys().size() > 1) {

                String returnDate = obj.getJourneys().get(1).getDeparture_date();
                String[] splitReturn = returnDate.split("/");

                datePickerDialog2 = DatePickerDialog.newInstance(this, Integer.parseInt(splitReturn[2]), Integer.parseInt(splitReturn[1])-1, Integer.parseInt(splitReturn[0]));
                datePickerDialog2.setYearRange(year, year + 1);


                if(obj.getJourneys().get(0).getFlight_status().equals("Available")) {
                /*Departure Date Clicked*/
                    returnDateBlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            datePickerDialog2.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
                            PICKER = RETURN_DATE_PICKER;
                            AnalyticsApplication.sendEvent("Click", "returnDateblock");



                        }
                    });
                }else{
                    disableReturn = true;
                }
                returnLayout.setVisibility(View.VISIBLE);
                txtReturnDeparture.setText(obj.getJourneys().get(1).getDeparture_station_name());
                txtReturnArrival.setText(obj.getJourneys().get(1).getArrival_station_name());
                txtReturnDepartureDate.setText(obj.getJourneys().get(1).getDeparture_date());
                txtReturnDepartureDate.setTag(reformatDOB2(obj.getJourneys().get(1).getDeparture_date()));

            }
        }
     }

    @Override
    public void onNewFlightReceive(SearchFlightReceive obj) {
        dismissLoading();
        Log.e("New Flight", obj.getStatus());
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //String flightType = obj.getType();
            Intent flight = null;
            Log.e("TYPE",obj.getType());
            if(obj.getFlight_type().equals("MH")){
                flight = new Intent(getActivity(), CodeShareFlightListActivity.class);
            }else{
                flight = new Intent(getActivity(), FireflyFlightListActivity.class);
            }
            flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));
            flight.putExtra("FLIGHT_TYPE", "" );
            flight.putExtra("ADULT", "" );
            flight.putExtra("INFANT", "" );
            flight.putExtra("PNR", pnr );
            flight.putExtra("BOOKING_ID", bookingId );
            flight.putExtra("DEPARTURE_DATE", txtDepartureDate.getTag().toString() );
            String date;

            //String flightType = "0";
            if(obj.getType().equals("0")){
                date = "";
            }else{
                date = txtReturnDepartureDate.getTag().toString();
            }
            flight.putExtra("RETURN_DATE", date );
            getActivity().startActivity(flight);

        }
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
            txtDepartureDate.setText(day + "/"+varMonth + "" + (month+1)+ "/" + year);
            txtDepartureDate.setTag(year + "-" + varMonth + "" + (month+1) + "-" + varDay + "" + day);
        }else if(PICKER.equals(RETURN_DATE_PICKER)){
            txtReturnDepartureDate.setText(day + "/"+varMonth + "" + (month+1)+ "/" + year);
            txtReturnDepartureDate.setTag(year + "-" + varMonth + "" + (month+1) + "-" + varDay + "" + day);
        }else{
            //DeadBlock
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

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if(!retrieveFlightInfo){
            if(result.size() > 0){
                Log.e("x","1");
                Gson gson = new Gson();
                ChangeSearchFlightReceive obj = gson.fromJson(result.get(0).getCachedResult(), ChangeSearchFlightReceive.class);
                onGetFlightSuccess(obj);
            }
        }else{
            if(result.size() > 0){
                Log.e("x","1");
                Gson gson = new Gson();
                SearchFlightReceive obj = gson.fromJson(result.get(0).getCachedResult(), SearchFlightReceive.class);
                onNewFlightReceive(obj);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


}
