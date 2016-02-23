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
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.module.MobileCheckInModule3;
import com.fly.firefly.ui.object.MobileConfirmCheckInPassenger;
import com.fly.firefly.ui.object.PassengerInfo;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.fly.firefly.utils.RealmObjectController;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MobileCheckInFragment4 extends BaseFragment {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.btnCloseM4)
    Button btnCloseM4;

    @InjectView(R.id.txtMessage)
    TextView txtMessage;

    private int fragmentContainerId;
    private String mobileCheckInRule;
    private MobileConfirmCheckInPassengerReceive obj;
    private AlertDialog dialog;
    private Validator mValidator;

    public static MobileCheckInFragment4 newInstance(Bundle bundle) {

        MobileCheckInFragment4 fragment = new MobileCheckInFragment4();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mobile_checkin_4, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        if(bundle.containsKey("MOBILE_CHECK_IN")){
            mobileCheckInRule = bundle.getString("MOBILE_CHECK_IN");
            Gson gson = new Gson();
            obj = gson.fromJson(mobileCheckInRule, MobileConfirmCheckInPassengerReceive.class);
            txtMessage.setText(Html.fromHtml(obj.getObj().getHtml().replaceAll("</br>","<p>")));
        }


        btnCloseM4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent closeIntent = new Intent(getActivity(), HomeActivity.class);
                closeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(closeIntent);
                getActivity().finish();

            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
