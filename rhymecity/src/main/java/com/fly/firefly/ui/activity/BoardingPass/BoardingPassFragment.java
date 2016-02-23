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
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity2;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity3;
import com.fly.firefly.ui.activity.SlidePage.ProductImagesPagerAdapter;
import com.fly.firefly.ui.adapter.BookingListAdapter;
import com.fly.firefly.ui.adapter.OfflineBookingListAdapter;
import com.fly.firefly.ui.module.BoardingPassModule;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.ProductImage;
import com.fly.firefly.ui.object.RetrieveBoardingPassObj;
import com.fly.firefly.ui.presenter.BoardingPassPresenter;
import com.fly.firefly.ui.presenter.LoginPresenter;
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

    private BitmapCache mMemoryCache;
    private int fragmentContainerId;
    private ArrayList<DropDownItem> dataFlightDeparture;
    private static ArrayList<DropDownItem> dataFlightArrival;
    private SharedPrefManager pref;
    private String DEPARTURE_FLIGHT = "Please choose your departure airport";
    private String ARRIVAL_FLIGHT = "Please choose your arrival airport";
    private static final String SCREEN_LABEL = "Boarding Pass";
    private Validator mValidator;

    private BookingListAdapter adapter;
    private OfflineBookingListAdapter offlineAdapter;

    private String storeUsername;
    private String loginStatus;
    private String signatureFromLocal;

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

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.retrieve_boarding_pass, container, false);
        ButterKnife.inject(this, view);

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

            if(!Controller.connectionAvailable(getActivity())){
                //if login & no internet.. display data from local database
                Realm realm = Realm.getInstance(getActivity());
                RealmResults<BoardingPassObj> result2 = realm.where(BoardingPassObj.class).findAll();

                Gson gson = new Gson();
                final MobileConfirmCheckInPassengerReceive obj = gson.fromJson(result2.get(0).getBoardingPassObj(), MobileConfirmCheckInPassengerReceive.class);

                offlineAdapter = new OfflineBookingListAdapter(getActivity(),obj.getObj().getBoarding_pass());
                listView.setAdapter(offlineAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                        MobileConfirmCheckInPassengerReceive.BoardingPass selectedFromList = (MobileConfirmCheckInPassengerReceive.BoardingPass) (listView.getItemAtPosition(myItemInt));

                        Log.e("Success", "True");
                        //startPagination();
                        Intent next = new Intent(getActivity(), BoardingPassDisplayActivity.class);
                        next.putExtra("OFFLINE_BOARDING_PASS_OBJ", (new Gson()).toJson(obj));
                        getActivity().startActivity(next);

                    }
                });
                Log.e("Result",result2.toString());
                /* ------------ */
                //dismissLoading();

            }else{
                initiateLoading(getActivity());
                presenter.retriveListOfBoardingPass(storeUsername, storePassword, "boarding_pass");
            }
        }else{
            pnrLayout.setVisibility(View.VISIBLE);

            /*Set PNR auto caps*/
            editPnr.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

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

       /*Departure Flight Clicked*/
        txtDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "txtDeparture");
                popupSelection(dataFlightDeparture, getActivity(), txtDeparture,true,view);
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
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
            Log.e("Success", "True");
            //startPagination();
            Intent next = new Intent(getActivity(), BoardingPassDisplayActivity.class);
            next.putExtra("BOARDING_PASS_OBJ", (new Gson()).toJson(obj));
            getActivity().startActivity(next);
        }

    }

    /*private void addBitmapToMemoryCache(final int key, final Bitmap bitmap) {
          if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(final int key) {
        return mMemoryCache.get(key);
    }*/

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
    public void onValidationSucceeded() {
        retrieveBoardingPass();
        Log.e("Success", "True");
    }

    public void retrieveBoardingPass(){

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
    public void onUserPnrList(ListBookingReceive obj){

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {
            adapter = new BookingListAdapter(getActivity(),obj.getObj().getList_booking());
            listView.setAdapter(adapter);
            //pnrLayout.setVisibility(View.GONE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                ListBookingReceive.ListBooking selectedFromList = (ListBookingReceive.ListBooking) (listView.getItemAtPosition(myItemInt));

                Log.e("Selected PNR", selectedFromList.getPnr());
                Log.e("Username", pref.getUserEmail().toString());
                initiateLoading(getActivity());

                RetrieveBoardingPassObj flightObj = new RetrieveBoardingPassObj();
                flightObj.setPnr(selectedFromList.getPnr());
                flightObj.setDeparture_station(selectedFromList.getDeparture_station_code());
                flightObj.setArrival_station(selectedFromList.getArrival_station_code());
                flightObj.setSignature(signatureFromLocal);

                presenter.retrieveBoardingPass(flightObj);
            }
        });

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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


}
