package com.fly.firefly.ui.activity.BookingFlight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightInfo;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.ManageFlight.CommitChangeActivity;
import com.fly.firefly.ui.adapter.CodeShareAdapter;
import com.fly.firefly.ui.adapter.FlightDetailAdapter;
import com.fly.firefly.ui.module.CodeShareFlightModule;
import com.fly.firefly.ui.module.SelectFlightModule;
import com.fly.firefly.ui.object.SelectChangeFlight;
import com.fly.firefly.ui.object.SelectFlight;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.ExpandAbleGridView;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CodeShareFlightListFragment extends BaseFragment implements BookingPresenter.ListFlightView {

    @Inject BookingPresenter presenter;
    @InjectView(R.id.btnListFlight)Button btnListFlight;
    @InjectView(R.id.flightDeparture)ExpandAbleGridView flightDeparture;
    @InjectView(R.id.flightArrival)ExpandAbleGridView flightArrival;
    @InjectView(R.id.codeShareDeparture)ExpandAbleGridView codeShareDeparture;

    @InjectView(R.id.returnFlightBlock)LinearLayout returnFlightBlock;
    @InjectView(R.id.goingFlightBlock)LinearLayout goingFlightBlock;
    @InjectView(R.id.returnBasicPremier)LinearLayout returnBasicPremier;

    @InjectView(R.id.txtDepartAirport)TextView txtDepartAirport;
    @InjectView(R.id.txtFlightType)TextView txtFlightType;
    @InjectView(R.id.txtDepartureDate)TextView txtDepartureDate;
    @InjectView(R.id.txtReturnType)TextView txtReturnType;
    @InjectView(R.id.txtReturnAirport)TextView txtReturnAirport;
    @InjectView(R.id.txtReturnDate)TextView txtReturnDate;

    @InjectView(R.id.btnBasic)LinearLayout btnBasic;
    @InjectView(R.id.btnPremier)LinearLayout btnPremier;
    @InjectView(R.id.btnBasicReturn)LinearLayout btnBasicReturn;
    @InjectView(R.id.btnPremierReturn)LinearLayout btnPremierReturn;

    @InjectView(R.id.premierFlightDeparture)ExpandAbleGridView premierFlightDeparture;
    @InjectView(R.id.premierFlightArrival)ExpandAbleGridView premierFlightArrival;
    @InjectView(R.id.basicPremierLayout)LinearLayout basicPremierLayout;


    private int fragmentContainerId;
    private CodeShareAdapter codeShareDepart,departListPremier, returnListBasic,returnListPremier;
    private String departPort,departDatePlain,arrivalPort,departPortCode,flightType;
    private String returnDepartPort,returnArrivalPort,returnDatePlain,arrivalPortCode;
    private String adult,infant;

    private String departClass,returnClass;

    private final String DEPART_BASIC = "DEPART_BASIC";
    private final String DEPART_PREMIER = "DEPART_PREMIER";
    private final String RETURN_PREMIER = "RETURN_PREMIER";
    private final String RETURN_BASIC = "RETURN_BASIC";

    private final String ECONOMY_PROMO = "ECONOMY_PROMO";
    private final String ECONOMY = "ECONOMY";


    private final String PREMIER = "PREMIER";
    private static final String SCREEN_LABEL = "Flight Detail";
    private final String FLIGHT_TYPE = "FLIGHT_TYPE";
    private final String ADULT = "ADULT";
    private final String INFANT = "INFANT";
    private final String DEPARTURE_DATE = "DEPARTURE_DATE";
    private final String RETURN_DATE = "RETURN_DATE";
    private final String RETURN = "RETURN";
    private final String DEPART = "DEPART";
    private Boolean proceed = false;
    private SharedPrefManager pref;
    private SearchFlightReceive obj;
    private String status1 = "N",status2 = "N";

    private String departFlightNumber,departFlightDepartureTime,departFlightArrivalTime,departFlightJourneyKey,
            departFlightFareSellKey;
    private String returnFlightNumber,returnFlightDepartureTime,returnFlightArrivalTime,returnFlightJourneyKey,
            returnFlightFareSellKey;
    private String pnr,bookingId,changeFlightType;

    public static CodeShareFlightListFragment newInstance(Bundle bundle) {

        CodeShareFlightListFragment fragment = new CodeShareFlightListFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new CodeShareFlightModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flight_detail, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        flightType = bundle.getString(FLIGHT_TYPE);
        adult = bundle.getString(ADULT);
        infant = bundle.getString(INFANT);
        departDatePlain = bundle.getString(DEPARTURE_DATE);
        returnDatePlain = bundle.getString(RETURN_DATE);
        basicPremierLayout.setVisibility(View.GONE);
        returnBasicPremier.setVisibility(View.GONE);
        try {
            pnr = bundle.getString("PNR");
            bookingId = bundle.getString("BOOKING_ID");
        }catch (Exception e){

        }


        pref = new SharedPrefManager(getActivity());


        String dataFlight2 = bundle.getString("FLIGHT_OBJ");
        Gson gson = new Gson();
        obj = gson.fromJson(dataFlight2, SearchFlightReceive.class);

        //Check From ManageFlight
        if(obj.getJourneys().get(0).getFlights().size() == 0){
            goingFlightBlock.setVisibility(View.GONE);
        }
        /*Departure*/
        List<FlightInfo> departFlight = obj.getJourneys().get(0).getFlights();

        departPortCode = obj.getJourneys().get(0).getDeparture_station_code();
        arrivalPortCode = obj.getJourneys().get(0).getArrival_station_code();

        codeShareDepart = new CodeShareAdapter(getActivity(),departFlight,departPort,arrivalPort,DEPART,this);
        codeShareDeparture.setAdapter(codeShareDepart);

         /*Return If Available*/
        if(obj.getJourneys().size() > 1){
            List<FlightInfo> returnFlight = obj.getJourneys().get(1).getFlights();
            returnFlightBlock.setVisibility(View.VISIBLE);
            returnBasicPremier.setVisibility(View.VISIBLE);


            //Return Airport
            returnDepartPort = obj.getJourneys().get(1).getDeparture_station_name();
            returnArrivalPort = obj.getJourneys().get(1).getArrival_station_name();
            String returnType = obj.getJourneys().get(1).getType();
            txtReturnAirport.setText(returnDepartPort + " - " + returnArrivalPort);
            txtReturnType.setText(returnType);

            //Reformat Date
            String returnDate = obj.getJourneys().get(1).getDeparture_date();
            txtReturnDate.setText(returnDate);

            returnListBasic = new CodeShareAdapter(getActivity(),returnFlight,returnDepartPort,returnArrivalPort,RETURN,this);
            flightArrival.setAdapter(returnListBasic);


        }

        btnListFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "Select Flight");
                //check if flight checked
                if(flightType.equals("0")){
                    if(departFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please Check Departure Flight 1");
                    }else{
                        proceed = true;
                    }
                }else if(flightType.equals("1")){
                    if(departFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please Check Departure Flight 2");
                    }else if(returnFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please Check Return Flight");
                    }else{
                        proceed = true;
                    }
                }else{

                    proceed = true;
                    changeFlightType = obj.getType();
                    status1 = obj.getGoing_flight().getStatus();
                    status2 = obj.getReturn_flight().getStatus();
                    Log.e("status2",status2);
                    if(status1.equals("Y")){
                        if(departFlightNumber == null){
                            Utils.toastNotification(getActivity(),"Please Check Departure Flight 3");
                            proceed = false;
                        }
                    }
                    if(status2.equals("Y")){
                        if(returnFlightNumber == null){
                            Utils.toastNotification(getActivity(),"Please Check Return Flight");
                            proceed = false;
                        }
                    }

                }
                if(proceed){

                   goPersonalDetail();

                }
            }
        });


        return view;
    }

    /*Inner Func*/
    public void goPersonalDetail()
    {

        initiateLoading(getActivity());

        SelectFlight selectFlightObj = new SelectFlight();
        selectFlightObj.setType(flightType);

        selectFlightObj.setDeparture_station(departPortCode);
        selectFlightObj.setArrival_station(arrivalPortCode);
        selectFlightObj.setDeparture_date(departDatePlain);
        selectFlightObj.setReturn_date(returnDatePlain);
        selectFlightObj.setAdult(adult);
        selectFlightObj.setInfant(infant);
        selectFlightObj.setUsername(""); //Get Username from Pref manager

        selectFlightObj.setFlight_number_1(departFlightNumber);
        selectFlightObj.setDeparture_time_1(departFlightDepartureTime);
        selectFlightObj.setArrival_time_1(departFlightArrivalTime);
        selectFlightObj.setJourney_sell_key_1(departFlightJourneyKey);
        selectFlightObj.setFare_sell_key_1(departFlightFareSellKey);

        selectFlightObj.setFlight_number_2(returnFlightNumber);
        selectFlightObj.setDeparture_time_2(returnFlightDepartureTime);
        selectFlightObj.setArrival_time_2(returnFlightArrivalTime);
        selectFlightObj.setJourney_sell_key_2(returnFlightJourneyKey);
        selectFlightObj.setFare_sell_key_2(returnFlightFareSellKey);

        presenter.selectFlight(selectFlightObj);

        AnalyticsApplication.sendEvent("StartActivity", "Passenger Information");
    }

    @Override
    public void onSeletFlightReceive(SelectFlightReceive obj) {

        dismissLoading();
        pref.setBookingID(obj.getFlightObj().getBookingId());
        Log.e("BookingID",obj.getFlightObj().getBookingId());

        Boolean status = Controller.getRequestStatus(obj.getFlightObj().getStatus(), obj.getFlightObj().getMessage(), getActivity());
        if (status
                ) {
            Intent passengerInfo = new Intent(getActivity(), PersonalDetailActivity.class);
            passengerInfo.putExtra(ADULT, adult);
            passengerInfo.putExtra(INFANT, infant);
            getActivity().startActivity(passengerInfo);
        }
    }

    @Override
    public void onChangeFlightSuccess(ManageChangeContactReceive obj) {

        dismissLoading();

        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
            Log.e("Success", "true");
            Intent intent = new Intent(getActivity(), CommitChangeActivity.class);
            intent.putExtra("COMMIT_UPDATE", (new Gson()).toJson(obj));
            getActivity().startActivity(intent);
        }
    }

    public void selectedInfo(FlightInfo obj,String type,String way) {

        if (way.equals(DEPART)) {

            Log.e("Here",obj.getFlight_number());
            departFlightNumber = obj.getFlight_number();
            departFlightDepartureTime = obj.getDeparture_time();
            departFlightArrivalTime = obj.getArrival_time();
            departFlightJourneyKey = obj.getJourney_sell_key();
            if (type.equals(ECONOMY_PROMO)) {
                departFlightFareSellKey = obj.getEconomy_promo_class().getFare_sell_key();
            } else if(type.equals(ECONOMY)){
                departFlightFareSellKey = obj.getEconomy_class().getFare_sell_key();
            }
        } else if (way.equals(RETURN)) {

            returnFlightNumber = obj.getFlight_number();
            returnFlightDepartureTime = obj.getDeparture_time();
            returnFlightArrivalTime = obj.getArrival_time();
            returnFlightJourneyKey = obj.getJourney_sell_key();

            if (type.equals(ECONOMY_PROMO)) {
                returnFlightFareSellKey = obj.getEconomy_promo_class().getFare_sell_key();
            } else if(type.equals(ECONOMY)){
                returnFlightFareSellKey = obj.getEconomy_class().getFare_sell_key();
            }
        }
    }

    public void alertNotAvailable(){
        Utils.toastNotification(getActivity(),"Not Available");
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
