package com.metech.firefly.ui.activity.ManageFlight;

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
import android.widget.ProgressBar;

import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.CheckInListReceive;
import com.metech.firefly.api.obj.FlightSummaryReceive;
import com.metech.firefly.api.obj.ListBookingReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.adapter.BookingListAdapter2;
import com.metech.firefly.ui.adapter.BookingListAdapter3;
import com.metech.firefly.ui.module.ManageFlightModule;
import com.metech.firefly.ui.object.ListFlight;
import com.metech.firefly.ui.object.ListFlight2;
import com.metech.firefly.ui.object.CachedResult;
import com.metech.firefly.ui.object.ManageFlightObj;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
import com.metech.firefly.utils.ExpandAbleGridView;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class MF_Fragment extends BaseFragment implements Validator.ValidationListener,ManageFlightPrenter.ManageFlightView{

    @Inject
    ManageFlightPrenter presenter;

    @InjectView(R.id.btnManageFlightContinue)
    Button btnManageFlightContinue;

    @NotEmpty(sequence = 1)
    @Order(1)
    @InjectView(R.id.txtPNR)
    EditText txtPNR;

    @NotEmpty(sequence = 1)
    @Order(2)
    @InjectView(R.id.txtUsername)
    EditText txtUsername;

    @InjectView(R.id.listView)
    ExpandAbleGridView listView;

    @InjectView(R.id.listViewDepart)
    ExpandAbleGridView listViewDepart;

    @InjectView(R.id.noUpcomingTrips)
    LinearLayout noUpcomingTrips;

    @InjectView(R.id.noCompletedTravel)
    LinearLayout noCompletedTravel;

    @InjectView(R.id.pnrLayout)
    LinearLayout pnrLayout;

    @InjectView(R.id.listviewLayout)
    LinearLayout listviewLayout;

    @InjectView(R.id.manageFlightNA)
    LinearLayout manageFlightNA;

    @InjectView(R.id.horizontalProgressBar)
    ProgressBar horizontalProgressBar;

    @InjectView(R.id.manageFlightNoInternet)
    LinearLayout manageFlightNoInternet;

    private int fragmentContainerId;
    public String SCREEN_LABEL;
    private SharedPrefManager pref;
    private String signature;
    private Validator mValidator;
    private String pnr,email;

    private BookingListAdapter2 adapter;
    private BookingListAdapter3 adapter2;

    //private BookingListAdapter2 adapter_depart;

    private String storeUsername,customerNumber;
    private String loginStatus;
    private boolean cache_login = false;
    private boolean cachedDisplay = false;

    public static MF_Fragment newInstance() {

        MF_Fragment fragment = new MF_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageFlightModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_flight, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initLogin = pref.getLoginStatus();
        loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);

        HashMap<String, String> initUsername = pref.getUserEmail();
        storeUsername = initUsername.get(SharedPrefManager.USER_EMAIL);

        HashMap<String, String> initCustomerNumber = pref.getCustomerNumber();
        customerNumber = initCustomerNumber.get(SharedPrefManager.CUSTOMER_NUMBER);

        HashMap<String, String> initPassword = pref.getUserPassword();
        String storePassword = initPassword.get(SharedPrefManager.PASSWORD);

        if(loginStatus != null && loginStatus.equals("Y")) {
            SCREEN_LABEL =  "Manage Flight: Login Manage Flight";
            cache_login = true;
            btnManageFlightContinue.setVisibility(View.GONE);

            //update the list on background

            if(Controller.connectionAvailable(getActivity())){
                initiateLoading(getActivity());
                presenter.onSendPNRV2(storeUsername, storePassword, "manage_booking",customerNumber);
            }else{
                manageFlightNoInternet.setVisibility(View.VISIBLE);
            }


           /* RealmResults<ManageFlightList> cachedListResult = RealmObjectController.getManageFlightList(MainFragmentActivity.getContext());
            if(cachedListResult.size() > 0){
                Gson gson = new Gson();
                ListBookingReceive obj = gson.fromJson(cachedListResult.get(0).getCachedList(), ListBookingReceive.class);
                onUserPnrList(obj);
                cachedDisplay = true;
                horizontalProgressBar.setVisibility(View.VISIBLE);

                //update the list on background
                presenter.onSendPNRV2(storeUsername, storePassword, "manage_booking");

            }else{
                if(Controller.connectionAvailable(getActivity())){
                    initiateLoading(getActivity());
                    presenter.onSendPNRV2(storeUsername, storePassword, "manage_booking");
                }else{
                    //if no connection. display no internet connection message - hide all
                }
            }

            btnManageFlightContinue.setVisibility(View.GONE);
*/
        }else{
            listviewLayout.setVisibility(View.GONE);
            pnrLayout.setVisibility(View.VISIBLE);
            SCREEN_LABEL =  "Manage Flight: Manage Flight";
        }
        cache_login = true;
        /*

         /*Set PNR auto caps*/
        InputFilter[] FilterArray = new InputFilter[2];
        FilterArray[0] = new InputFilter.LengthFilter(6);
        FilterArray[1] = new InputFilter.AllCaps();
        txtPNR.setFilters(FilterArray);

        btnManageFlightContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
            }
        });


        return view;
    }

    public void sentPNR() {

        initiateLoading(getActivity());
        ManageFlightObj manageFlightObj = new ManageFlightObj();
        //manageFlightObj.setSignature(signature);
        manageFlightObj.setUsername(txtUsername.getText().toString());
        manageFlightObj.setPnr(txtPNR.getText().toString());

        presenter.onSendPNRV1(manageFlightObj);
    }

    /* Validation Success - Start send data to server */
    @Override
    public void onValidationSucceeded() {
        sentPNR();
    }

    /* Validation Failed - Toast Error */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);

            /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            } else {
                croutonAlert(getActivity(), splitErrorMsg[0]);
            }
        }
    }

    @Override
    public void onGetFlightFromPNR(FlightSummaryReceive obj){

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //Gson gsonUserInfo = new Gson();
            //String userInfo = gsonUserInfo.toJson(obj);

            pref.setBookingID(obj.getBooking_id());
            //pref.setUserID(obj.getObj().getUse());

            if(loginStatus != null){
                if(loginStatus.equals("Y")){
                    pref.setPNR(obj.getItenerary_information().getPnr() + "," + storeUsername);
                }else{
                    pref.setPNR(txtPNR.getText().toString() + "," + txtUsername.getText().toString());
                }
            }else{
                pref.setPNR(txtPNR.getText().toString() + "," + txtUsername.getText().toString());
            }

            HashMap<String, String> initUsername = pref.getPNR();
            String pnrUserEmail = initUsername.get(SharedPrefManager.PNR);

            pref.setSignatureToLocalStorage(obj.getSignature());

            /*SaveAllInPref*/
            displayActionSelection(obj);
        }

    }

    @Override
    public void onUserPnrList(final ListBookingReceive obj){

        //special loading for cached list
        if(!cachedDisplay){
            dismissLoading();
        }else{
            //dismissCachedListLoading();
            horizontalProgressBar.setVisibility(View.INVISIBLE);
        }

        pref.setUserID(obj.getUser_id());

        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //need to split departed flight
            int a = 0;
            int b = 0;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date dt = new Date();
            String currentDateTimeString = dateFormat.format(dt);


            if(obj.getList_booking().size() == 0){
                manageFlightNA.setVisibility(View.VISIBLE);
                listviewLayout.setVisibility(View.GONE);
            }else{



                List<ListFlight> list_booking = new ArrayList<ListFlight>();
                List<ListFlight2> list_booking_depart = new ArrayList<ListFlight2>();

                //= obj.getList_booking();
                //list_booking.remove(0);

                for(int v = 0 ; v < obj.getList_booking().size(); v++){
                    ListFlight test = new ListFlight();
                    ListFlight2 test2 = new ListFlight2();

                    if(currentDateTimeString.compareTo(obj.getList_booking().get(v).getDeparture_datetime()) < 0){
                        test.setDate(obj.getList_booking().get(v).getDate());
                        test.setPnr(obj.getList_booking().get(v).getPnr());
                        list_booking.add(test);
                    }else{
                        test2.setDate(obj.getList_booking().get(v).getDate());
                        test2.setPnr(obj.getList_booking().get(v).getPnr());
                        list_booking_depart.add(test2);
                    }
                }

                if(list_booking.size() > 0){
                    noUpcomingTrips.setVisibility(View.GONE);
                    adapter = new BookingListAdapter2(getActivity(),list_booking,"MF");
                    listView.setAdapter(adapter);
                }

                if(list_booking_depart.size() > 0){
                    noCompletedTravel.setVisibility(View.GONE);
                    adapter2 = new BookingListAdapter3(getActivity(),list_booking_depart,"MF");
                    listViewDepart.setAdapter(adapter2);
                }

                pref.setSignatureToLocalStorage(obj.getSignature());

            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                ListFlight selectedFromList = (ListFlight) (listView.getItemAtPosition(myItemInt));

                //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.YOUR_ANIMATION);
                //view.startAnimation(hyperspaceJumpAnimation);

                initiateLoading(getActivity());

                ManageFlightObj manageFlightObj = new ManageFlightObj();
                manageFlightObj.setPnr(selectedFromList.getPnr());
                manageFlightObj.setUsername(storeUsername);
                manageFlightObj.setUser_id(obj.getUser_id());
                manageFlightObj.setSignature(obj.getSignature());
                manageFlightObj.setCustomer_number(customerNumber);

                cache_login = false;
                horizontalProgressBar.setVisibility(View.INVISIBLE);
                presenter.onSendPNRV1(manageFlightObj);
            }
        });

        listViewDepart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                ListFlight2 selectedFromList = (ListFlight2) (listViewDepart.getItemAtPosition(myItemInt));

                //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.YOUR_ANIMATION);
                //view.startAnimation(hyperspaceJumpAnimation);

                initiateLoading(getActivity());

                ManageFlightObj manageFlightObj = new ManageFlightObj();
                manageFlightObj.setPnr(selectedFromList.getPnr());
                manageFlightObj.setUsername(storeUsername);
                manageFlightObj.setUser_id(obj.getUser_id());
                manageFlightObj.setSignature(obj.getSignature());
                manageFlightObj.setCustomer_number(customerNumber);

                cache_login = false;
                horizontalProgressBar.setVisibility(View.INVISIBLE);
                presenter.onSendPNRV1(manageFlightObj);
            }
        });

    }

    @Override
    public void onCheckUserStatus(CheckInListReceive obj){

    }


    public void displayActionSelection(FlightSummaryReceive obj){
        Intent actionSelection = new Intent(getActivity(), MF_ActionActivity.class);
        actionSelection.putExtra("ITINENARY_INFORMATION", (new Gson()).toJson(obj));
        getActivity().startActivity(actionSelection);
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

       if(!cache_login){
           if(result.size() > 0){
               Gson gson = new Gson();
               FlightSummaryReceive obj = gson.fromJson(result.get(0).getCachedResult(), FlightSummaryReceive.class);
               onGetFlightFromPNR(obj);
           }
       }else{
           if(result.size() > 0){
               Gson gson = new Gson();
               ListBookingReceive obj = gson.fromJson(result.get(0).getCachedResult(), ListBookingReceive.class);
               onUserPnrList(obj);
           }
       }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();

    }
}
