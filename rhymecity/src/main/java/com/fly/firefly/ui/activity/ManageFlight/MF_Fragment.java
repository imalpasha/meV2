package com.fly.firefly.ui.activity.ManageFlight;

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

import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.CheckInListReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.BookingListAdapter;
import com.fly.firefly.ui.module.ManageFlightModule;
import com.fly.firefly.ui.object.CachedResult;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

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
    ListView listView;

    @InjectView(R.id.pnrLayout)
    LinearLayout pnrLayout;

    @InjectView(R.id.listviewLayout)
    LinearLayout listviewLayout;

    @InjectView(R.id.manageFlightNA)
    LinearLayout manageFlightNA;


    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String signature;
    private Validator mValidator;
    private String pnr,email;
    private BookingListAdapter adapter;
    private String storeUsername;
    private String loginStatus;
    private boolean cache_login = false;

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

        HashMap<String, String> initPassword = pref.getUserPassword();
        String storePassword = initPassword.get(SharedPrefManager.PASSWORD);

        if(loginStatus != null && loginStatus.equals("Y")) {
            cache_login = true;
            if(Controller.connectionAvailable(getActivity())){
                initiateLoading(getActivity());
                presenter.onSendPNRV2(storeUsername, storePassword, "manage_booking");
            }else{
                //Display No Internet connection
            }
            btnManageFlightContinue.setVisibility(View.GONE);

        }else{
            cache_login = false;
            pnrLayout.setVisibility(View.VISIBLE);
        }

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
        Log.e("Send Pnr", "true");
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
            Log.e("booking id", obj.getBooking_id());

            pref.setBookingID(obj.getBooking_id());
            //pref.setUserID(obj.getObj().getUse());

            if(loginStatus != null){
                if(loginStatus.equals("Y")){
                    //Log.e("PNR",obj.getObj().getPnr());
                    pref.setPNR(obj.getItenerary_information().getPnr() + "," + storeUsername);
                }else{
                    pref.setPNR(txtPNR.getText().toString() + "," + txtUsername.getText().toString());
                }
            }else{
                pref.setPNR(txtPNR.getText().toString() + "," + txtUsername.getText().toString());
            }

            HashMap<String, String> initUsername = pref.getPNR();
            String pnrUserEmail = initUsername.get(SharedPrefManager.PNR);
            Log.e("XXX",pnrUserEmail);

            pref.setSignatureToLocalStorage(obj.getSignature());

            /*SaveAllInPref*/
            displayActionSelection(obj);
        }

    }

    @Override
    public void onUserPnrList(final ListBookingReceive obj){

        dismissLoading();
        pref.setUserID(obj.getUser_id());

        Boolean status = Controller.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            if(obj.getList_booking().size() == 0){
                manageFlightNA.setVisibility(View.VISIBLE);
                listviewLayout.setVisibility(View.GONE);
            }else{
                adapter = new BookingListAdapter(getActivity(),obj.getList_booking(),"MF");
                listView.setAdapter(adapter);
                pref.setSignatureToLocalStorage(obj.getSignature());
            }

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                ListBookingReceive.ListBooking selectedFromList = (ListBookingReceive.ListBooking) (listView.getItemAtPosition(myItemInt));

                //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.YOUR_ANIMATION);
                //view.startAnimation(hyperspaceJumpAnimation);

                Log.e("Selected PNR", selectedFromList.getPnr());
                Log.e("Username", pref.getUserEmail().toString());
                initiateLoading(getActivity());

                ManageFlightObj manageFlightObj = new ManageFlightObj();
                manageFlightObj.setPnr(selectedFromList.getPnr());
                manageFlightObj.setUsername(storeUsername);
                manageFlightObj.setUser_id(obj.getUser_id());
                manageFlightObj.setSignature(obj.getSignature());

                cache_login = false;
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

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());

       if(!cache_login){
           if(result.size() > 0){
               Log.e("x","1");
               Gson gson = new Gson();
               FlightSummaryReceive obj = gson.fromJson(result.get(0).getCachedResult(), FlightSummaryReceive.class);
               onGetFlightFromPNR(obj);
           }else {
               Log.e("x", "2");
           }
       }else{
           if(result.size() > 0){
               Log.e("x","1");
               Gson gson = new Gson();
               ListBookingReceive obj = gson.fromJson(result.get(0).getCachedResult(), ListBookingReceive.class);
               onUserPnrList(obj);
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
