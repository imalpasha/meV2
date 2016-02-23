package com.fly.firefly.ui.activity.ManageFlight;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.CheckInListReceive;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.ManageFlightReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.adapter.BookingListAdapter;
import com.fly.firefly.ui.adapter.FlightDetailAdapter;
import com.fly.firefly.ui.module.ManageFlightModule;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.object.PasssengerInfoV2;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.utils.ExpandAbleGridView;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ManageFlightFragment extends BaseFragment implements Validator.ValidationListener,ManageFlightPrenter.ManageFlightView{

    @Inject
    ManageFlightPrenter presenter;

    @InjectView(R.id.btnManageFlightContinue)
    Button btnManageFlightContinue;

    @NotEmpty(sequence = 1)
    @Order(1)
    @InjectView(R.id.txtPNR)
    TextView txtPNR;

    @NotEmpty(sequence = 1)
    @Order(2)
    @InjectView(R.id.txtUsername)
    TextView txtUsername;

    @InjectView(R.id.listView)
    ListView listView;

    @InjectView(R.id.pnrLayout)
    LinearLayout pnrLayout;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String signature;
    private Validator mValidator;
    private String pnr,email;
    private BookingListAdapter adapter;
    private String storeUsername;
    private String loginStatus;

    public static ManageFlightFragment newInstance() {

        ManageFlightFragment fragment = new ManageFlightFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new ManageFlightModule(this)).inject(this);

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
            initiateLoading(getActivity());
            presenter.onSendPNRV2(storeUsername,storePassword,"manage_booking");
        }else{
            pnrLayout.setVisibility(View.VISIBLE);
        }



        txtPNR.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
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
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

            //Gson gsonUserInfo = new Gson();
            //String userInfo = gsonUserInfo.toJson(obj);
            Log.e("booking id", obj.getObj().getBooking_id());

            pref.setBookingID(obj.getObj().getBooking_id());
            if(loginStatus != null){
                if(loginStatus.equals("Y")){
                    //Log.e("PNR",obj.getObj().getPnr());
                    pref.setPNR(obj.getObj().getItenerary_information().getPnr() + "," + storeUsername);
                }else{
                    pref.setPNR(txtPNR.getText().toString() + "," + txtUsername.getText().toString());
                }
            }else{
                pref.setPNR(txtPNR.getText().toString() + "," + txtUsername.getText().toString());
            }

            HashMap<String, String> initUsername = pref.getPNR();
            String pnrUserEmail = initUsername.get(SharedPrefManager.PNR);
            Log.e("XXX",pnrUserEmail);

            pref.setSignatureToLocalStorage(obj.getObj().getSignature());

            /*SaveAllInPref*/
            displayActionSelection(obj);
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

                //Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.YOUR_ANIMATION);
                //view.startAnimation(hyperspaceJumpAnimation);

                Log.e("Selected PNR", selectedFromList.getPnr());
                Log.e("Username",pref.getUserEmail().toString());
                initiateLoading(getActivity());

                ManageFlightObj manageFlightObj = new ManageFlightObj();
                manageFlightObj.setPnr(selectedFromList.getPnr());
                manageFlightObj.setUsername(storeUsername);

                presenter.onSendPNRV1(manageFlightObj);
            }
        });

    }

    @Override
    public void onCheckUserStatus(CheckInListReceive obj){

    }


    public void displayActionSelection(FlightSummaryReceive obj){
        Intent actionSelection = new Intent(getActivity(), ManageFlightActionActivity.class);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();

    }
}
