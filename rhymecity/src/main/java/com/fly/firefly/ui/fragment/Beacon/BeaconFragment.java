/*package com.fly.firefly.ui.fragment.Beacon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Gate.CurrentGateActivity;
import com.fly.firefly.ui.presenter.HomePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;

public class BeaconFragment extends BaseFragment implements HomePresenter.HomeView {

    //@Inject
    //HomePresenter presenter;
*/
    /* Beacon - Testing Module *//*
    private BeaconManager beaconManager,beaconNoti;
    private int fragmentContainerId;
    private static final Map<String, List<String>> PLACES_BY_BEACONS;
    private Region region;
    int splashTime = 0;

    public static BeaconFragment newInstance() {

        BeaconFragment fragment = new BeaconFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FireFlyApplication.get(getActivity()).createScopedGraph(new HomeModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.beacon_smart_assist, container, false);
        ButterKnife.inject(this, view);


        /*Enable Bluetooth*/
       /* BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.disable()) {
            mBluetoothAdapter.enable();
        }else{
            mBluetoothAdapter.enable();
        }*/

        /* Beacon - Region*//*
        beaconManager = new BeaconManager(getActivity());

        /*Detect All*//*
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {

                region = new Region("ranged region", UUID.fromString("8492E75F-4FD6-469D-B132-043FE94921D8"), null, null);
                beaconManager.startMonitoring(region);

            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                currentGate();
            }

            @Override
            public void onExitedRegion(Region region) {

                Log.e("Exit Region", "True");

            }
        });



       /*beaconManager = new BeaconManager(getActivity());
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    Log.e("xx", places.toString());
                }
            }
        });

        region = new Region("ranged region", UUID.fromString("8492E75F-4FD6-469D-B132-043FE94921D8"), null, null);
        *//*
        return view;
    }



    public void currentGate()
    {
        Intent gateIntent = new Intent(getActivity(), CurrentGateActivity.class);
        getActivity().startActivity(gateIntent);
        getActivity().finish();
    }

    /* Ranging - Static Function *//*
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("2394:20240", new ArrayList<String>() {{
            add("Heavenly Sandwiches");
            add("Green & Green Salads");
            add("Mini Panini");
        }});
        placesByBeacons.put("648:12", new ArrayList<String>() {{
            add("Mini Panini");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});

        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }


    @Override
    public void onResume() {
        super.onResume();
        /*beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });*/
        // presenter.onResume();
    /*}/*

    @Override
    public void onPause() {
        super.onPause();
        beaconManager.stopMonitoring(region);
    }
}
*/