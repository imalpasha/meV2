package com.fly.firefly.ui.activity.BookingFlight;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.FlightInfo;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.ManageFlight.CommitChangeActivity;
import com.fly.firefly.ui.adapter.FlightDetailAdapter;
import com.fly.firefly.ui.module.SelectFlightModule;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.object.SelectChangeFlight;
import com.fly.firefly.ui.object.SelectFlight;
import com.fly.firefly.ui.presenter.BookingPresenter;
import com.fly.firefly.utils.AESCBC;
import com.fly.firefly.utils.App;
import com.fly.firefly.utils.ExpandAbleGridView;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.fly.firefly.utils.Utils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class FireflyFlightListFragment extends BaseFragment implements BookingPresenter.ListFlightView {

    @Inject BookingPresenter presenter;
    @InjectView(R.id.btnListFlight)Button btnListFlight;
    @InjectView(R.id.flightDeparture)ExpandAbleGridView flightDeparture;
    @InjectView(R.id.flightArrival)ExpandAbleGridView flightArrival;
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

    @InjectView(R.id.returnFlightNA)LinearLayout returnFlightNA;
    @InjectView(R.id.goingFlightNA)LinearLayout goingFlightNA;
    @InjectView(R.id.basicPremierLayout)LinearLayout basicPremierLayout;

    @InjectView(R.id.fareRulesChkBox)CheckBox fareRulesChkBox;
    //@InjectView(R.id.fareRuleLayout)LinearLayout fareRuleLayout;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Flight Details";
    private static final String SCREEN_LABEL_MANAGE = "Edit Flight Detail";
    //public String SCREEN_NAME;
    //public int popup = 0;
    private FlightDetailAdapter departListBasic,departListPremier, returnListBasic,returnListPremier;
    private String departPort,departDatePlain,arrivalPort,departPortCode,flightType;
    private String returnDepartPort,returnArrivalPort,returnDatePlain,arrivalPortCode;
    private String adult,infant;

    private String departClass,returnClass;

    private final String DEPART_BASIC = "DEPART_BASIC";
    private final String DEPART_PREMIER = "DEPART_PREMIER";
    private final String RETURN_PREMIER = "RETURN_PREMIER";
    private final String RETURN_BASIC = "RETURN_BASIC";
    private final String BASIC = "BASIC";
    private final String PREMIER = "PREMIER";
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
    private AlertDialog dialog;

    private String departFlightNumber,departFlightDepartureTime,departFlightArrivalTime,departFlightJourneyKey,
            departFlightFareSellKey;
    private String returnFlightNumber,returnFlightDepartureTime,returnFlightArrivalTime,returnFlightJourneyKey,
            returnFlightFareSellKey;
    private String pnr,bookingId,changeFlightType;
    private String storeUsername;
    private String storePassword;
    private SelectFlight selectFlightObj;
    private String loginStatus;
    private boolean normalFlightList = false;

    public static FireflyFlightListFragment newInstance(Bundle bundle) {

        FireflyFlightListFragment fragment = new FireflyFlightListFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SelectFlightModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flight_detail, container, false);
        ButterKnife.inject(this, view);



          /*Preference Manager*/
        pref = new SharedPrefManager(getActivity());

            /* If Passenger Already Login - Auto display necessary data */
        HashMap<String, String> initLogin = pref.getLoginStatus();
        loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);
        /*Realm Obj Test*/
        //Realm realm = Realm.getInstance(getActivity());
        //RealmResults<BoardingPassObj> result2 = realm.where(BoardingPassObj.class).findAll();
        /* ------------ */

        Bundle bundle = getArguments();
        flightType = bundle.getString(FLIGHT_TYPE);
        adult = bundle.getString(ADULT);
        infant = bundle.getString(INFANT);
        departDatePlain = bundle.getString(DEPARTURE_DATE);
        returnDatePlain = bundle.getString(RETURN_DATE);

        try {
            pnr = bundle.getString("PNR");
            bookingId = bundle.getString("BOOKING_ID");
        }catch (Exception e){

        }

        pref = new SharedPrefManager(getActivity());

        btnPremier.setBackgroundColor(getResources().getColor(R.color.grey));
        btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.grey));

        String dataFlight2 = bundle.getString("FLIGHT_OBJ");
        Gson gson = new Gson();
        obj = gson.fromJson(dataFlight2, SearchFlightReceive.class);


        //Check From ManageFlight
        if(obj.getJourneys().get(0).getFlights().size() == 0){
            //goingFlightBlock.setVisibility(View.GONE);
            goingFlightNA.setVisibility(View.VISIBLE);
            basicPremierLayout.setVisibility(View.GONE);
        }else{
            goingFlightNA.setVisibility(View.GONE);
            basicPremierLayout.setVisibility(View.VISIBLE);
        }

        //mix change flight availability
        if(pnr != null){
            if (obj.getGoing_flight().getStatus().equals("N")) {
                goingFlightBlock.setVisibility(View.GONE);
                goingFlightNA.setVisibility(View.GONE);
            }
        }


        /*Departure*/
        List<FlightInfo> departFlight = obj.getJourneys().get(0).getFlights();

        //Depart Airport
        departPort = obj.getJourneys().get(0).getDeparture_station_name();
        arrivalPort = obj.getJourneys().get(0).getArrival_station_name();

        departPortCode = obj.getJourneys().get(0).getDeparture_station_code();
        arrivalPortCode = obj.getJourneys().get(0).getArrival_station_code();

        String type = "("+obj.getJourneys().get(0).getType()+")";
        Log.e("FlightType",type);

        txtDepartAirport.setText(departPort.toUpperCase()+" - "+arrivalPort.toUpperCase());
        txtFlightType.setText(type);

        //Reformat Date
        String departDate = obj.getJourneys().get(0).getDeparture_date();
        //String[] output = departDate.split(" ");
        //String month = output[1];
        txtDepartureDate.setText(departDate);

        /*Basic*/
        departListBasic = new FlightDetailAdapter(getActivity(),departFlight,departPort,arrivalPort,BASIC,DEPART,this);
        flightDeparture.setAdapter(departListBasic);

        /*Premier*/
        departListPremier = new FlightDetailAdapter(getActivity(),departFlight,departPort,arrivalPort,PREMIER,DEPART,this);
        premierFlightDeparture.setAdapter(departListPremier);

        /*Return If Available*/
        if(obj.getJourneys().size() > 1){

            //Check From ManageFlight
            if(obj.getJourneys().get(1).getFlights().size() == 0){
                //goingFlightBlock.setVisibility(View.GONE);
                returnFlightNA.setVisibility(View.VISIBLE);
                returnBasicPremier.setVisibility(View.GONE);
            }else{
                returnFlightNA.setVisibility(View.GONE);
                returnBasicPremier.setVisibility(View.VISIBLE);
            }

            List<FlightInfo> returnFlight = obj.getJourneys().get(1).getFlights();
            returnFlightBlock.setVisibility(View.VISIBLE);

            //Return Airport
            returnDepartPort = obj.getJourneys().get(1).getDeparture_station_name();
            returnArrivalPort = obj.getJourneys().get(1).getArrival_station_name();
            String returnType = "("+obj.getJourneys().get(1).getType()+")";
            txtReturnAirport.setText(returnDepartPort.toUpperCase() + " - " + returnArrivalPort.toUpperCase());
            txtReturnType.setText(returnType);

            //Reformat Date
            String returnDate = obj.getJourneys().get(1).getDeparture_date();
            txtReturnDate.setText(returnDate);

            returnListBasic = new FlightDetailAdapter(getActivity(),returnFlight,returnDepartPort,returnArrivalPort,BASIC,RETURN,this);
            flightArrival.setAdapter(returnListBasic);

            returnListPremier = new FlightDetailAdapter(getActivity(),returnFlight,returnDepartPort,returnArrivalPort,PREMIER,RETURN,this);
            premierFlightArrival.setAdapter(returnListPremier);

            //mix change flight availability
            if(pnr != null){
                if(obj.getReturn_flight().getStatus().equals("N")){
                    returnFlightBlock.setVisibility(View.GONE);
                    returnFlightNA.setVisibility(View.GONE);
                    Log.e("You Here","OK");
                }
            }

        }else{
            returnBasicPremier.setVisibility(View.GONE);
        }

        btnListFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                proceed = false;
                boolean minute90 = true;

                AnalyticsApplication.sendEvent("Click", "Select Flight");
                //check if flight checked
                int a = 1;

                if(flightType.equals("0")){
                    if(departFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please select a fare for your going flight.");
                    }else{
                        proceed = true;
                    }
                }else if(flightType.equals("1")){
                    if(departFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please select a fare for your going flight.");
                    }else if(returnFlightNumber == null){
                        Utils.toastNotification(getActivity(),"Please select a fare for your return flight.");
                    }else if(departDatePlain.equals(returnDatePlain) && timeCompare(departFlightArrivalTime,returnFlightDepartureTime)){
                            Utils.toastNotificationLong(getActivity(), "Please recheck the flights you selected. Your second flight leaves before your first flight arrives!");
                            proceed = false;
                            minute90 = false;
                    }else if(departDatePlain.equals(returnDatePlain) && compare90Minute(departFlightArrivalTime,returnFlightDepartureTime)){
                            Utils.toastNotificationLong(getActivity(), " In order to select flights that travel on the same day, you must allow at least 90 minutes between flights. Please select a different pair of flights.!");
                            proceed = false;
                    }else{
                        proceed = true;
                        Log.e("3","3");
                    }
                }else{

                    proceed = true;
                    changeFlightType = obj.getType();
                    status1 = obj.getGoing_flight().getStatus();
                    status2 = obj.getReturn_flight().getStatus();
                    Log.e("status2", status2);
                    if(status1.equals("Y")){
                        if(departFlightNumber == null){
                            Utils.toastNotification(getActivity(),"Please select a fare for your going flight.");
                            proceed = false;
                        }
                    }
                    else if(status2.equals("Y")){
                        if(returnFlightNumber == null){
                            Utils.toastNotification(getActivity(),"Please select a fare for your return flight.");
                            proceed = false;
                        }
                    }

                }
                if(proceed){
                    if(!fareRulesChkBox.isChecked()){
                        proceed = false;
                        croutonAlert(getActivity(), "You must agree to the terms and conditions.");
                    }else{
                        if(pnr == null){
                            if(loginStatus == null || loginStatus.equals("N")) {
                                continueAs();
                            }else{
                                goPersonalDetail();
                            }

                        }  else{
                            changeFlight();
                        }
                    }

                }
            }
        });

        btnBasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnBasic");
                switchFare(DEPART_BASIC);
            }
        });

        btnPremier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnPremier");
                switchFare(DEPART_PREMIER);
            }
        });

        btnBasicReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnBasicReturn");
                switchFare(RETURN_BASIC);
            }
        });

        btnPremierReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnPremierReturn");
                switchFare(RETURN_PREMIER);
            }
        });

        //Premium Flight Button Disable
        basicPremierLayout.setVisibility(View.GONE);
        returnBasicPremier.setVisibility(View.GONE);

        return view;
    }

    public void continueAs(){

        //popup = 1;
        //SCREEN_NAME = "Book FLight: Flight Details(Login Popup)";

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.continue_as, null);

        Button cont = (Button)myView.findViewById(R.id.btnContinue);
        Button close = (Button)myView.findViewById(R.id.btnClose);
        Button login = (Button)myView.findViewById(R.id.btnLogin);
        final EditText userId = (EditText)myView.findViewById(R.id.txtUserId);
        final EditText password = (EditText)myView.findViewById(R.id.txtPassword);
        password.setTransformationMethod(new PasswordTransformationMethod());

        final EditText editEmail = (EditText)myView.findViewById(R.id.editTextemail_login);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goPersonalDetail();
                dialog.dismiss();

            }

        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userId.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "User ID is required", Toast.LENGTH_LONG).show();
                } else if (!userId.getText().toString().matches(emailPattern)) {
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_LONG).show();
                } else {
                    loginFragment(userId.getText().toString(), AESCBC.encrypt(App.KEY, App.IV, password.getText().toString()));
                }
            }

        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myView);

        dialog = builder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = 570;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }


    /* SENT REQUEST TO LOGIN API*/
    public void loginFragment(String username,String password){
        /*Start Loading*/
        initiateLoading(getActivity());
        LoginRequest data = new LoginRequest();
        data.setUsername(username);
        data.setPassword(password);

        storeUsername = username;
        storePassword = password;

        presenter.requestLogin(data);
    }

    @Override
    public void onLoginSuccess(LoginReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            pref.setLoginStatus("Y");
            pref.setNewsletterStatus(obj.getUser_info().getNewsletter());
            pref.setSignatureToLocalStorage(obj.getUser_info().getSignature());
            pref.setUsername(obj.getUser_info().getFirst_name());

            Log.e(storeUsername,storePassword);
            pref.setUserEmail(storeUsername);
            pref.setUserPassword(storePassword);

            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(obj.getUser_info());
            pref.setUserInfo(userInfo);

            dialog.dismiss();
            goPersonalDetail();
        }
    }


    /*Inner Func*/
    public void goPersonalDetail()
    {

        initiateLoading(getActivity());

        selectFlightObj = new SelectFlight();
        selectFlightObj.setType(flightType);

        selectFlightObj.setDeparture_station(departPortCode);
        selectFlightObj.setArrival_station(arrivalPortCode);
        selectFlightObj.setDeparture_date(departDatePlain);
        selectFlightObj.setReturn_date(returnDatePlain);
        selectFlightObj.setAdult(adult);
        selectFlightObj.setInfant(infant);

        HashMap<String, String> initUserEmail = pref.getUserEmail();
        String userEmail = initUserEmail.get(SharedPrefManager.USER_EMAIL);
        if( userEmail == null){
            userEmail = "";
        }

        selectFlightObj.setUsername(userEmail); //Get Username from Pref manager

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

    public void changeFlight() {

        initiateLoading(getActivity());

        normalFlightList = true;
        SelectChangeFlight changeFlightObj = new SelectChangeFlight();

        changeFlightObj.setPnr(pnr);
        changeFlightObj.setBooking_id(bookingId);
        changeFlightObj.setSignature(obj.getSignature());

        changeFlightObj.setType(changeFlightType);

        changeFlightObj.setDeparture_station(departPortCode);
        changeFlightObj.setArrival_station(arrivalPortCode);
        changeFlightObj.setDeparture_date(departDatePlain);
        changeFlightObj.setReturn_date(returnDatePlain);

        //SENT EMPTY VALUE
        if(departFlightNumber == null){
            departFlightNumber = "";
        }if(departFlightDepartureTime == null){
            departFlightDepartureTime = "";
        }if(departFlightArrivalTime == null){
            departFlightArrivalTime = "";
        }if(departFlightJourneyKey == null){
            departFlightJourneyKey = "";
        }if(departFlightFareSellKey == null){
            departFlightFareSellKey = "";
        }

        changeFlightObj.setStatus_1(status1);
        changeFlightObj.setFlight_number_1(departFlightNumber);
        changeFlightObj.setDeparture_time_1(departFlightDepartureTime);
        changeFlightObj.setArrival_time_1(departFlightArrivalTime);
        changeFlightObj.setJourney_sell_key_1(departFlightJourneyKey);
        changeFlightObj.setFare_sell_key_1(departFlightFareSellKey);

        changeFlightObj.setStatus_2(status2);
        changeFlightObj.setFlight_number_2(returnFlightNumber);
        changeFlightObj.setDeparture_time_2(returnFlightDepartureTime);
        changeFlightObj.setArrival_time_2(returnFlightArrivalTime);
        changeFlightObj.setJourney_sell_key_2(returnFlightJourneyKey);
        changeFlightObj.setFare_sell_key_2(returnFlightFareSellKey);

        presenter.changeFlight(changeFlightObj);

        AnalyticsApplication.sendEvent("StartActivity", "Passenger Information");
    }

    @Override
    public void onSeletFlightReceive(SelectFlightReceive obj) {

        dismissLoading();
        pref.setBookingID(obj.getBookingId());

        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
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
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Log.e("Success","true");
            Intent intent = new Intent(getActivity(), CommitChangeActivity.class);
            intent.putExtra("COMMIT_UPDATE", (new Gson()).toJson(obj));
            getActivity().startActivity(intent);
        }
    }

    //Switch Flight Type
    public void switchFare(String way)
    {
        if(way == DEPART_BASIC) {

            premierFlightDeparture.setVisibility(View.GONE);
            flightDeparture.setVisibility(View.VISIBLE);
            btnBasic.setBackgroundColor(getResources().getColor(R.color.white));
            btnPremier.setBackgroundColor(getResources().getColor(R.color.grey));
            departListBasic.invalidateSelected();
            departFlightNumber = null;

        }else if (way == DEPART_PREMIER){

            premierFlightDeparture.setVisibility(View.VISIBLE);
            flightDeparture.setVisibility(View.GONE);
            btnBasic.setBackgroundColor(getResources().getColor(R.color.grey));
            btnPremier.setBackgroundColor(getResources().getColor(R.color.white));
            departListPremier.invalidateSelected();
            departFlightNumber = null;

        }else if (way == RETURN_BASIC){

            premierFlightArrival.setVisibility(View.GONE);
            flightArrival.setVisibility(View.VISIBLE);
            btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.grey));
            btnBasicReturn.setBackgroundColor(getResources().getColor(R.color.white));
            returnListBasic.invalidateSelected();
            returnFlightNumber = null;

        }else if (way == RETURN_PREMIER)
        {
            premierFlightArrival.setVisibility(View.VISIBLE);
            flightArrival.setVisibility(View.GONE);
            btnBasicReturn.setBackgroundColor(getResources().getColor(R.color.grey));
            btnPremierReturn.setBackgroundColor(getResources().getColor(R.color.white));
            returnListPremier.invalidateSelected();
            returnFlightNumber = null;

        }
    }


    public void selectedInfo(FlightInfo obj,String type,String way) {

        if (way.equals(DEPART)) {

            departFlightNumber = obj.getFlight_number();
            departFlightDepartureTime = obj.getDeparture_time();
            departFlightArrivalTime = obj.getArrival_time();
            departFlightJourneyKey = obj.getJourney_sell_key();
            if (type.equals(BASIC)) {
                departFlightFareSellKey = obj.getBasicObj().getFare_sell_key();
            } else {
                departFlightFareSellKey = obj.getFlexObj().getFare_sell_key();
            }
        } else if (way.equals(RETURN)) {

            returnFlightNumber = obj.getFlight_number();
            returnFlightDepartureTime = obj.getDeparture_time();
            returnFlightArrivalTime = obj.getArrival_time();
            returnFlightJourneyKey = obj.getJourney_sell_key();

            if (type.equals(BASIC)) {
                returnFlightFareSellKey = obj.getBasicObj().getFare_sell_key();
            } else {
                returnFlightFareSellKey = obj.getFlexObj().getFare_sell_key();
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

        if (pnr == null) {
            AnalyticsApplication.sendScreenView(SCREEN_LABEL);
            Log.e("Tracker", SCREEN_LABEL);

        }else{
            AnalyticsApplication.sendScreenView(SCREEN_LABEL_MANAGE);
            Log.e("Tracker", SCREEN_LABEL_MANAGE);
        }

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());

        if(!normalFlightList){
            if(result.size() > 0){
                Log.e("x","1");
                Gson gson = new Gson();
                SelectFlightReceive obj = gson.fromJson(result.get(0).getCachedResult(), SelectFlightReceive.class);
                onSeletFlightReceive(obj);
             }
        }else{
            if(result.size() > 0){
                Log.e("x","1");
                Gson gson = new Gson();
                ManageChangeContactReceive obj = gson.fromJson(result.get(0).getCachedResult(), ManageChangeContactReceive.class);
                onChangeFlightSuccess(obj);
            }else {
                Log.e("x", "2");
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
