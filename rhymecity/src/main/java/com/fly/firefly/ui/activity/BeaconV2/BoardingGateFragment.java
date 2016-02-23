package com.fly.firefly.ui.activity.BeaconV2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fly.firefly.AlarmReceiver;
import com.fly.firefly.BeaconController;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.google.gson.Gson;

import java.util.Calendar;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class BoardingGateFragment extends BaseFragment {

    private int fragmentContainerId;

    @InjectView(R.id.btnClose)
    Button btnClose;

    @InjectView(R.id.txtMessage)
    TextView txtMessage;

    public static BoardingGateFragment newInstance(Bundle bundle) {

        BoardingGateFragment fragment = new BoardingGateFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.boarding_gate_message, container, false);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();

        if(bundle.containsKey("MESSAGE")){
            String flightSummary = bundle.getString("MESSAGE");
            txtMessage.setText(flightSummary);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
