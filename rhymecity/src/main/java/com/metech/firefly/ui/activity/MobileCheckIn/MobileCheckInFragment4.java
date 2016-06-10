package com.metech.firefly.ui.activity.MobileCheckIn;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.BoardingPass.BoardingPassDisplayActivity;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Homepage.HomeActivity;
import com.metech.firefly.ui.presenter.MobileCheckInPresenter;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.Validator;

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

    @InjectView(R.id.txtBoardingHere)
    TextView txtBoardingHere;


    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Success Check In View";
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

        txtBoardingHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(getActivity(), BoardingPassDisplayActivity.class);
                next.putExtra("OFFLINE_BOARDING_PASS_OBJ", (new Gson()).toJson(obj));
                getActivity().startActivity(next);

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

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
