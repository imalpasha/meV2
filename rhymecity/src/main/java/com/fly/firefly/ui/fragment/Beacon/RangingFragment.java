/*package com.fly.firefly.ui.fragment.Beacon;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Gate.CurrentGateActivity;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.presenter.HomePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RangingFragment extends BaseFragment implements HomePresenter.HomeView {

    //@InjectView
    //HomePresenter presenter;

    @InjectView(R.id.txtSearching)
    TextView txtSearching;

    /* Beacon - Testing Module *//*
    private BeaconManager beaconManager,beaconNoti;
    private int fragmentContainerId;
    private static final Map<String, List<String>> PLACES_BY_BEACONS;
    private Region region;
    int splashTime = 0;
    private String hello = "null";
    public static RangingFragment newInstance() {

        RangingFragment fragment = new RangingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.beacon_smart_assist, container, false);
        ButterKnife.inject(this, view);

        txtSearching.setText("Searching nearest Beacon");

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

        beaconManager = new BeaconManager(getActivity());
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    Log.e("xx", places.toString());

                    Log.e(hello,places.get(0).toString());

                    if(hello != places.get(0).toString()) {

                        if (places.get(0).toString() == "Beacon1") {
                            triggerNotification("Beacon : 1");
                            hello = "Beacon1";
                        } else {
                            triggerNotification("Beacon : 2");
                            hello = "Beacon2";
                        }
                    }

                }


            }
        });

        region = new Region("ranged region", UUID.fromString("8492E75F-4FD6-469D-B132-043FE94921D8"), null, null);


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
        placesByBeacons.put("2793:19481", new ArrayList<String>() {{
            add("Beacon1");

        }});
        placesByBeacons.put("7714:13156", new ArrayList<String>() {{
            add("Beacon2");
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

    public void triggerNotification(String nearestBeacon)
    {
        int notificationId = 001;
        // Build intent for notification content
        Intent viewIntent = new Intent(getActivity(), LoginActivity.class);
        //viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(getActivity(), 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.departure_icon)
                        .setContentTitle("Nearest Beacon")
                        .setContentText(nearestBeacon)
                        .setContentIntent(viewPendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

        // Build the notification and issues it with notification manager.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }


    @Override
    public void onResume() {
        super.onResume();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
        // presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        beaconManager.stopMonitoring(region);
    }
}
*/