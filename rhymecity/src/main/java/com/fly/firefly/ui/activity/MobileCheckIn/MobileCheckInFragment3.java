package com.fly.firefly.ui.activity.MobileCheckIn;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fly.firefly.Controller;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.MobileCheckInPassengerReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.module.MobileCheckInModule3;
import com.fly.firefly.ui.object.MobileConfirmCheckInPassenger;
import com.fly.firefly.ui.object.PassengerInfo;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MobileCheckInFragment3 extends BaseFragment implements MobileCheckInPresenter.MobileCheckInView3,Validator.ValidationListener  {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.txtCheckIn3)
    TextView txtCheckIn3;

    @InjectView(R.id.txtCheckIn2)
    TextView txtCheckIn2;

    @InjectView(R.id.txtCheckIn1)
    TextView txtCheckIn1;

    @Checked
    @InjectView(R.id.c1)
    CheckBox c1;

    @Checked
    @InjectView(R.id.c2)
    CheckBox c2;

    @Checked
    @InjectView(R.id.c3)
    CheckBox c3;

    @InjectView(R.id.btnContinueCheckIn)
    Button btnContinueCheckIn;


    private int fragmentContainerId;
    private String mobileCheckInRule;
    private MobileCheckInPassengerReceive obj;
    private AlertDialog dialog;
    private Validator mValidator;
    private SharedPrefManager pref;
    private String storeUsername;

    public static MobileCheckInFragment3 newInstance(Bundle bundle) {

        MobileCheckInFragment3 fragment = new MobileCheckInFragment3();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new MobileCheckInModule3(this)).inject(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mobile_checkin_3, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        Bundle bundle = getArguments();

          /* If Passenger Already Login - Auto display necessary data */
        HashMap<String, String> initLogin = pref.getLoginStatus();
        String loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);
        if(loginStatus != null && loginStatus.equals("Y")) {

            HashMap<String, String> initUsername = pref.getUserEmail();
            storeUsername = initUsername.get(SharedPrefManager.USER_EMAIL);

        }

        if(bundle.containsKey("MOBILE_CHECK_IN")){
            mobileCheckInRule = bundle.getString("MOBILE_CHECK_IN");
            Gson gson = new Gson();
            obj = gson.fromJson(mobileCheckInRule, MobileCheckInPassengerReceive.class);
        }

        setCheckBoxRule();

        btnContinueCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
            }
        });


        return view;
    }

    public void setCheckBoxRule(){

        txtCheckIn1.setText(obj.getObj().getRules().get(0).getRule_1().getText());
        txtCheckIn2.setText(obj.getObj().getRules().get(0).getRule_2().getText());
        txtCheckIn3.setText(obj.getObj().getRules().get(0).getRule_3().getText());

        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox1Clicked(true);
                } else {
                    checkBox1Clicked(false);
                }

            }
        });

        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox2Clicked(true);
                } else {
                    checkBox2Clicked(false);
                }

            }
        });


    }

    public void checkBox1Clicked(Boolean stat){

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.check_box_1_rule, null);
        Button btnRule1 = (Button)myView.findViewById(R.id.btnRule1);

        if(stat){ btnRule1.setText("Agree"); }
        else{ btnRule1.setText("Disagree");  }

        aq.recycle(myView);
        aq.id(R.id.dangerousGoodImg).image(obj.getObj().getRules().get(0).getRule_1().getImage());

        btnRule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        popupBox(myView);

    }

    public void checkBox2Clicked(Boolean stat){

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.check_box_2_rule, null);
        Button btnRule1 = (Button)myView.findViewById(R.id.btnRule2);

        if(stat){ btnRule1.setText("Agree"); }
        else{ btnRule1.setText("Disagree");  }

        aq.recycle(myView);

        aq.id(R.id.chechkBox2Title1).getTextView().setText(obj.getObj().getRules().get(0).getRule_2().getTitle_1());
        aq.id(R.id.chechkBox2Title2).getTextView().setText(obj.getObj().getRules().get(0).getRule_2().getTitle_2());
        aq.id(R.id.chechkBox2Title3).getTextView().setText(obj.getObj().getRules().get(0).getRule_2().getTitle_3());

        aq.id(R.id.chechkBox2HTML1).getTextView().setText(Html.fromHtml(obj.getObj().getRules().get(0).getRule_2().getHtml_1().replaceAll("</br>", "<p>")));
        aq.id(R.id.chechkBox2HTML2).getTextView().setText(Html.fromHtml(obj.getObj().getRules().get(0).getRule_2().getHtml_2().replaceAll("</br>", "<p>")));
        aq.id(R.id.chechkBox2HTML3).getTextView().setText(Html.fromHtml(obj.getObj().getRules().get(0).getRule_2().getHtml_3().replaceAll("</br>", "<p>")));

        aq.id(R.id.dangerousGoodImg).image(obj.getObj().getRules().get(0).getRule_1().getImage());

        btnRule1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        popupBox(myView);

    }

    @Override
    public void onValidationSucceeded() {
        Log.e("Success", "True");
        checkInPassenger();
    }

    public void checkInPassenger(){
        ArrayList<PassengerInfo> mainObj = new ArrayList<PassengerInfo>();

        initiateLoading(getActivity());
        int selected = 1;
        for(int i = 0 ; i < obj.getObj().getPassengers().size() ; i++){

            PassengerInfo passengerObj = new PassengerInfo();
            passengerObj.setExpiration_date(obj.getObj().getPassengers().get(i).getExpiration_date());
            passengerObj.setDocument_number(obj.getObj().getPassengers().get(i).getDocument_number());
            passengerObj.setIssuing_country(obj.getObj().getPassengers().get(i).getIssuing_country());
            passengerObj.setTravel_document(obj.getObj().getPassengers().get(i).getTravel_document());
            passengerObj.setPassenger_number(obj.getObj().getPassengers().get(i).getPassenger_number());
            passengerObj.setStatus(obj.getObj().getPassengers().get(i).getStatus());
            mainObj.add(passengerObj);

        }

        MobileConfirmCheckInPassenger obj2 = new MobileConfirmCheckInPassenger();
        obj2.setPassengers(mainObj);
        obj2.setPnr(obj.getObj().getPnr());
        obj2.setDeparture_station_code(obj.getObj().getDeparture_station_code());
        obj2.setArrival_station_code(obj.getObj().getArrival_station_code());
        obj2.setSignature(obj.getObj().getSignature());

        presenter.confirmCheckInPassenger(obj2);
    }
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();

             /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");


        }
        croutonAlert(getActivity(), "You must read and understand the important dangerous goods information and terms and condition");

    }

    public void popupBox(View myView){

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

    @Override
    public void onConfirmCheckInPassenger(MobileConfirmCheckInPassengerReceive obj) {

        dismissLoading();
        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

            //if(RealmObjectController.currentPNR(getActivity(),obj,storeUsername)){
                RealmObjectController.currentPNR(getActivity(),obj,storeUsername);
            //    RealmObjectController.saveBoardingPass(getActivity(),obj,storeUsername);
            //}else{
            //    Log.e("New PNR","True");
            //}

            Intent next = new Intent(getActivity(), MobileCheckInActivity4.class);
            next.putExtra("MOBILE_CHECK_IN", (new Gson()).toJson(obj));
            getActivity().startActivity(next);
            getActivity().finish();
            //setSuccessDialog(getActivity(), obj.getMessage(), MobileCheckInActivity4.class);
        }

    }

    public void saveToRealmObject(){

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
