package com.fly.firefly.ui.activity.BoardingPass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.util.BitmapCache;
import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity2;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity3;
import com.fly.firefly.ui.activity.SlidePage.ProductImagesPagerAdapter;
import com.fly.firefly.ui.adapter.BookingListAdapter;
import com.fly.firefly.ui.adapter.OfflineBookingListAdapter;
import com.fly.firefly.ui.module.BoardingPassModule;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.fly.firefly.ui.object.BoardingPassPNRList;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.object.MobileCheckInList;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.ProductImage;
import com.fly.firefly.ui.object.RetrieveBoardingPassObj;
import com.fly.firefly.ui.presenter.BoardingPassPresenter;
import com.fly.firefly.ui.presenter.LoginPresenter;
import com.fly.firefly.utils.DropDownItem;
import com.fly.firefly.utils.RealmObjectController;
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
import io.realm.Realm;
import io.realm.RealmResults;

public class BoardingPassFragment extends BaseFragment implements Validator.ValidationListener,BoardingPassPresenter.RetrieveBoardingPassView {

    @Inject
    BoardingPassPresenter presenter;

    @InjectView(R.id.boardingPassBtn)
    Button boardingPassBtn;

    @NotEmpty
    @InjectView(R.id.editPnr)
    EditText editPnr;

    @NotEmpty
    @InjectView(R.id.txtDeparture)
    TextView txtDeparture;

    @NotEmpty
    @InjectView(R.id.txtArrive)
    TextView txtArrive;

    @InjectView(R.id.pnrLayout)
    LinearLayout pnrLayout;

    @InjectView(R.id.listView)
    ListView listView;

    @InjectView(R.id.boardingPassNA)
    LinearLayout boardingPassNA;

    @InjectView(R.id.listviewLayout)
    LinearLayout listviewLayout;

    @InjectView(R.id.horizontalProgressBar)
    ProgressBar horizontalProgressBar;

    private BitmapCache mMemoryCache;
    private int fragmentContainerId;
    public String SCREEN_LABEL;
    private ArrayList<DropDownItem> dataFlightDeparture;
    private static ArrayList<DropDownItem> dataFlightArrival;
    private SharedPrefManager pref;
    private String DEPARTURE_FLIGHT = "Please choose your departure airport";
    private String ARRIVAL_FLIGHT = "Please choose your arrival airport";
    private Validator mValidator;

    private BookingListAdapter adapter;
    private OfflineBookingListAdapter offlineAdapter;

    private String storeUsername;
    private String loginStatus;
    private String signatureFromLocal;
    private boolean cachedDisplay = false;
    private boolean retrieveBoardingPass = false;
    private Bundle dataPassBundle;

    public static BoardingPassFragment newInstance() {

        BoardingPassFragment fragment = new BoardingPassFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new BoardingPassModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.retrieve_boarding_pass, container, false);
        ButterKnife.inject(this, view);

        dataPassBundle = new Bundle();

        /*Preference Manager*/
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> initLogin = pref.getLoginStatus();
        loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);

        HashMap<String, String> initUsername = pref.getUserEmail();
        storeUsername = initUsername.get(SharedPrefManager.USER_EMAIL);

        HashMap<String, String> initPassword = pref.getUserPassword();
        String storePassword = initPassword.get(SharedPrefManager.PASSWORD);

        if(loginStatus != null && loginStatus.equals("Y")) {
            SCREEN_LABEL = "Login Boarding Pass";

            if(!Controller.connectionAvailable(getActivity())){
                //if login & no internet.. display data from local database
                Realm realm = Realm.getInstance(getActivity());
                final RealmResults<BoardingPassObj> result2 = realm.where(BoardingPassObj.class).findAll();

                if(result2.size() == 0) {
                    //no cached boarding pass
                    boardingPassNA.setVisibility(View.VISIBLE);
                }else{

    //              offlineAdapter = new OfflineBookingListAdapter(getActivity(),obj.getObj().getBoarding_pass());
                    offlineAdapter = new OfflineBookingListAdapter(getActivity(),result2);

                    listView.setAdapter(offlineAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {

                            final MobileConfirmCheckInPassengerReceive obj = (new Gson()).fromJson(result2.get(myItemInt).getBoardingPassObj(), MobileConfirmCheckInPassengerReceive.class);

                            Intent next = new Intent(getActivity(), BoardingPassDisplayActivity.class);
                            next.putExtra("OFFLINE_BOARDING_PASS_OBJ", (new Gson()).toJson(obj));
                            getActivity().startActivity(next);

                        }
                    });
                }
                boardingPassBtn.setVisibility(View.GONE);

            }else{

                RealmResults<BoardingPassPNRList> cachedListResult = RealmObjectController.getBoardingPassPNRList(MainFragmentActivity.getContext());
                if(cachedListResult.size() > 0){

                    Gson gson = new Gson();
                    ListBookingReceive obj = gson.fromJson(cachedListResult.get(0).getCachedList(), ListBookingReceive.class);
                    onUserPnrList(obj);
                    Log.e("Use Cached List","True");
                    cachedDisplay = true;
                    horizontalProgressBar.setVisibility(View.VISIBLE);
                    //update the list on background
                    presenter.retriveListOfBoardingPass(storeUsername, storePassword, "boarding_pass");

                }else{
                    initiateLoading(getActivity());
                    presenter.retriveListOfBoardingPass(storeUsername, storePassword, "boarding_pass");
                    boardingPassBtn.setVisibility(View.GONE);
                }
            }

        }else{
            SCREEN_LABEL = "Boarding Pass";
            pnrLayout.setVisibility(View.VISIBLE);

            /*Set PNR auto caps*/
            InputFilter[] FilterArray = new InputFilter[2];
            FilterArray[0] = new InputFilter.LengthFilter(6);
            FilterArray[1] = new InputFilter.AllCaps();
            editPnr.setFilters(FilterArray);

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
                if(!row.optString("status").equals("N")){
                    al.add(row.optString("location")+"/-"+row.optString("location_code"));
                }            }
            hs.addAll(al);
            al.clear();
            al.addAll(hs);

            /*Display Airport*/
            for (int i = 0; i < al.size(); i++)
            {
                String flightSplit = al.get(i).toString();
                String[] str1 = flightSplit.split("/-");
                String p1 = str1[0];
                String p2 = str1[1];

                DropDownItem itemFlight = new DropDownItem();
                itemFlight.setText(p1+ " ("+p2+")");
                itemFlight.setCode(p2);
                itemFlight.setTag("FLIGHT");
                dataFlightDeparture.add(itemFlight);

            }

        }

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

        boardingPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();

            }
        });

        return view;
    }

    @Override
    public void onBoardingPassReceive(RetrieveBoardingPassReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Log.e("Success", "True");
            //startPagination();


            Intent next = new Intent(getActivity(), BoardingPassDisplayActivity.class);
            next.putExtra("BOARDING_PASS_OBJ", (new Gson()).toJson(obj));
            dataPassBundle.putString("LOAD_BACKGROUND","N");
            next.putExtras(dataPassBundle);
            getActivity().startActivity(next);
        }

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
                itemFlight.setText(row.optString("travel_location")+" ("+row.optString("travel_location_code")+")");
                itemFlight.setCode(row.optString("travel_location_code" + ""));
                itemFlight.setTag("FLIGHT_DEPARTURE");
                dataFlightArrival.add(itemFlight);

            }
        }
        Log.e("Arrive", dataFlightArrival.toString());

    }

    @Override
    public void onValidationSucceeded() {
        retrieveBoardingPass();
        Log.e("Success", "True");
    }

    public void retrieveBoardingPass(){

        retrieveBoardingPass = true;

        initiateLoading(getActivity());

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

        RetrieveBoardingPassObj flightObj = new RetrieveBoardingPassObj();
        flightObj.setPnr(editPnr.getText().toString());
        flightObj.setDeparture_station(txtDeparture.getTag().toString());
        flightObj.setArrival_station(txtArrive.getTag().toString());
        flightObj.setSignature(signatureFromLocal);




        presenter.retrieveBoardingPass(flightObj);
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

    @Override
    public void onUserPnrList(final ListBookingReceive obj){

        if(!cachedDisplay){
            dismissLoading();
        }else{
            horizontalProgressBar.setVisibility(View.INVISIBLE);
        }

        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            if(obj.getList_booking().size() == 0){
                boardingPassNA.setVisibility(View.VISIBLE);
                listviewLayout.setVisibility(View.GONE);
            }else{
                adapter = new BookingListAdapter(getActivity(),obj.getList_booking(),"BP");
                listView.setAdapter(adapter);
                pref.setSignatureToLocalStorage(obj.getSignature());
            }
            //pnrLayout.setVisibility(View.GONE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {

                ListBookingReceive.ListBooking selectedFromList = (ListBookingReceive.ListBooking) (listView.getItemAtPosition(myItemInt));

                dataPassBundle.putString("PNR",selectedFromList.getPnr());
                dataPassBundle.putString("USER_ID",obj.getUser_id());
                dataPassBundle.putString("DEPARTURE_STATION_CODE",selectedFromList.getDeparture_station_code());
                dataPassBundle.putString("ARRIVAL_STATION_CODE",selectedFromList.getArrival_station_code());
                dataPassBundle.putString("SIGNATURE",obj.getSignature());

                //check if already cached, display cached data first and load new data at background
                Realm realm = Realm.getInstance(MainFragmentActivity.getContext());
                final RealmResults<BoardingPassObj> result2 = realm.where(BoardingPassObj.class).findAll();
                Log.e("result",result2.toString());

                //do query here
                RealmResults<BoardingPassObj> boardingPass = realm.where(BoardingPassObj.class).equalTo("pnr",selectedFromList.getPnr()).equalTo("departureDateTime", selectedFromList.getDeparture_datetime()).findAll();
                Log.e("qryResult",boardingPass.toString());
                if(boardingPass.size() > 0){

                    //if more than 0 - boarding pass cached. just display using realm object
                    //but still need to reload new data incase user check-in through website
                    final RetrieveBoardingPassReceive ccc = (new Gson()).fromJson(boardingPass.get(0).getBoardingPassObj(), RetrieveBoardingPassReceive.class);

                    Log.e("PNR",ccc.getBoarding_pass().get(0).getRecordLocator());
                    Intent next = new Intent(getActivity(), BoardingPassDisplayActivity.class);
                    next.putExtra("BOARDING_PASS_OBJ", (new Gson()).toJson(ccc));
                    dataPassBundle.putString("LOAD_BACKGROUND","Y");
                    next.putExtras(dataPassBundle);

                    getActivity().startActivity(next);



                }else{
                    initiateLoading(getActivity());
                    retrieveBoardingPass(selectedFromList,obj);
                }


            }
        });
    }

    public void retrieveBoardingPass(ListBookingReceive.ListBooking selectedFromList,ListBookingReceive obj){

        RetrieveBoardingPassObj flightObj = new RetrieveBoardingPassObj();
        flightObj.setUser_id(obj.getUser_id());
        flightObj.setPnr(selectedFromList.getPnr());
        flightObj.setDeparture_station(selectedFromList.getDeparture_station_code());
        flightObj.setArrival_station(selectedFromList.getArrival_station_code());
        flightObj.setSignature(obj.getSignature());
        retrieveBoardingPass = true;

        presenter.retrieveBoardingPass(flightObj);

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

        if(!retrieveBoardingPass){
            if(result.size() > 0){
                Log.e("x","1");
                Gson gson = new Gson();
                ListBookingReceive obj = gson.fromJson(result.get(0).getCachedResult(), ListBookingReceive.class);
                onUserPnrList(obj);
            }else {
                Log.e("x", "2");
            }
        }else{
            if(result.size() > 0){
                Log.e("x","1");
                Gson gson = new Gson();
                RetrieveBoardingPassReceive obj = gson.fromJson(result.get(0).getCachedResult(), RetrieveBoardingPassReceive.class);
                onBoardingPassReceive(obj);
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

    public void onBackPressed(){

        Intent backToHome = new Intent(getActivity(), HomeActivity.class);
        backToHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(backToHome);
        getActivity().finish();
        Log.e("Tedy","x");

    }


}
