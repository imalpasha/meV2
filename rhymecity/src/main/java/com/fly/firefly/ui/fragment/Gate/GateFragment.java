/*package com.fly.firefly.ui.fragment.Gate;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.Beacon.RangingActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.activity.Map.IndoorMapActivity;
import com.fly.firefly.ui.presenter.HomePresenter;

import java.util.List;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GateFragment extends BaseFragment implements HomePresenter.HomeView {

    //@Inject
    //HomePresenter presenter;

    /* Beacon - Testing Module *//*
    private BeaconManager beaconManager;
    private int fragmentContainerId;
    private Boolean notify = false;
    private BeaconManager beaconNoti;

    @InjectView(R.id.btnNotify)
    Button btnNotify;

    @InjectView(R.id.btnExplore)
    Button btnExplore;

    @InjectView(R.id.rangeBtn)
    Button rangeBtn;

    public static GateFragment newInstance() {

        GateFragment fragment = new GateFragment();
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

        View view = inflater.inflate(R.layout.gate_screen, container, false);
        ButterKnife.inject(this, view);

        beaconNoti = new BeaconManager(getActivity());
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notify) {
                    notify = false;
                    btnNotify.setText("Notify : Off");
                    offNotification();
                } else {
                    notify = true;
                    btnNotify.setText("Notify : On");
                    onNotification();
                }
                Log.e("Notification", Boolean.toString(notify));
            }
        });

        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent mapIntent = new Intent(getActivity(), IndoorMapActivity.class);
               getActivity().startActivity(mapIntent);

            }
        });

        rangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent range = new Intent(getActivity(), RangingActivity.class);
                getActivity().startActivity(range);

            }
        });

        /*Log.e("NOTI AFTER CLICK",Boolean.toString(notify));
        if(!notify){
            btnNotify.setText("Notify : Off");
            Log.e("Notify Off", "true");

        }else{
            btnNotify.setText("Notify : On");
            Log.e("Start Monitoring","true");
            beaconNoti.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconNoti.startMonitoring(new Region("monitored region", UUID.fromString("8492E75F-4FD6-469D-B132-043FE94921D8"), 2793, 19481));
                }
            });
        }*//*


        beaconNoti.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                //if(notify) {
                triggerNotification();
                // }
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.e("Exit Region", "True");
            }
        });

        return view;
    }

    public void onNotification(){
        beaconNoti.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconNoti.startMonitoring(new Region("monitored region", UUID.fromString("8492E75F-4FD6-469D-B132-043FE94921D8"), 2793, 19481));
            }
        });    }

    public void offNotification(){
        beaconNoti.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconNoti.stopMonitoring(new Region("monitored region", UUID.fromString("8492E75F-4FD6-469D-B132-043FE94921D8"), 2793, 19481));
            }
        });    }


    public void triggerNotification()
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
                        .setContentTitle("Arrive At Gate 18A")
                        .setContentText("Flight will depart in 20 minutes")
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

   // private void getSampleData() {
   //     presenter.onRequestGoogleData();
   // }

    @Override
    public void onResume() {
        super.onResume();
       // presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
       // presenter.onPause();
    }
}*/
