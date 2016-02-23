package com.fly.firefly.ui.activity.BeaconV2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
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
import com.fly.firefly.R;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.google.gson.Gson;

import java.util.Calendar;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArriveEntranceFragment extends BaseFragment {

    private int fragmentContainerId;

    @InjectView(R.id.btnClose)
    Button btnClose;

    @InjectView(R.id.btnBlueetooth)
    Button btnBlueetooth;

    @InjectView(R.id.txtMessage)
    TextView txtMessage;

    public static ArriveEntranceFragment newInstance(Bundle bundle) {

        ArriveEntranceFragment fragment = new ArriveEntranceFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.arrive_airport_message, container, false);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();

        //if(bundle.containsKey("MESSAGE")){
        //    String flightSummary = bundle.getString("MESSAGE");
            //txtMessage.setText(flightSummary);
        //}
        txtMessage.setText("Welcomes to Subang Airport. \n\nTurn on device bluetooth if you intend to check-in / print boarding pass using Firefly Kiosk. \n Thanks You.");
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btnBlueetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothAdapter.enable();
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
