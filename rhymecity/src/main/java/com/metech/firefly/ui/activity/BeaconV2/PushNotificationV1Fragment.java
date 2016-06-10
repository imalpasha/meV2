package com.metech.firefly.ui.activity.BeaconV2;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.metech.firefly.R;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.GeoFencing.GeoFencingFragment;
import com.metech.firefly.ui.activity.GeoFencing.GeofenceTransitionReceiver;
import com.metech.firefly.ui.activity.GeoFencing.MyPlaces;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PushNotificationV1Fragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private int fragmentContainerId;
    public static final String TAG = GeoFencingFragment.class.getSimpleName();
    private static final long LOCATION_ITERATION_PAUSE_TIME = 1000;
    private static final int NUMBER_OF_LOCATION_ITERATIONS = 10;
    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private MyPlaces happyPlace;
    private MyPlaces home;
    private List<Geofence> myFences = new ArrayList<>();
    private GoogleApiClient googleApiClient;
    private PendingIntent geofencePendingIntent;
    private LocationManager locationManager;
    private int marker = 0;
    private Location lastLocation;

    @InjectView(R.id.btnClose)
    Button btnClose;

    @InjectView(R.id.btnGPS)
    Button btnGPS;

    @InjectView(R.id.txtMessage)
    TextView txtMessage;

    @InjectView(R.id.txtGPS)
    TextView txtGPS;

    public static PushNotificationV1Fragment newInstance(Bundle bundle) {

        PushNotificationV1Fragment fragment = new PushNotificationV1Fragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_geo_fence_message, container, false);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();

        if(bundle.containsKey("MESSAGE")){
           String flightSummary = bundle.getString("MESSAGE");
           //txtMessage.setText(flightSummary);
        }
        txtMessage.setText("Reminder from Firefly: \n\n Your flight FY1234 from Subang to Penang will depart at 10:00AM on 04 FEB 2016.");
        txtGPS.setText("Please turn on device GPS to use our geo-fence notification and receive bonuslink reward !  ");
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
                setUpMap();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }




    /* -------------------------------------------------------------------------------------------------------- */

    private void setUpMap() {

        Log.e("Set Map","True");

        // PRES 1
        /*
            1. Create a "Place" that will become a Geofence
            2. Add a place marker on our Map
            3. Add our place to our list of Geofences
            4. Repeat for each place
         */

        // Add a place with a Geofence
        home = new MyPlaces("Pier @ Folly Beach", "This is my Happy Place!",0.00,0.00, new LatLng(3.0139278, 101.3995878), 10000, 10, R.drawable.departure_icon);
        //addPlaceMarker(happyPlace);
        addFence(home);

        // Add a place with a Geofence
        // Work 39.3336585, -84.3146718
        // Home 39.2697455, -84.269921
        happyPlace = new MyPlaces("Home", "This is where I live.",0.00,0.00, new LatLng(2.9243610, 101.6559994
        ), 100, 10, R.drawable.departure_icon);
        //addPlaceMarker(home);
        addFence(happyPlace);

        // Add a place w/o a Geofence
        MyPlaces charleston = new MyPlaces("Charleston, SC", "This is where I want to live!",0.00,0.00, new LatLng(32.8210454, -79.9704779), 0, 10, R.drawable.departure_icon);
        //addPlaceMarker(charleston);
        addFence(charleston);

        /*
            After all your places have been created and markers added you can monitor your fences.
         */
        monitorFences(myFences);

       /* map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (updateLocationRunnable != null && updateLocationRunnable.isAlive() && !updateLocationRunnable.isInterrupted()) {
                    updateLocationRunnable.interrupt();
                }
                updateLocationRunnable = new UpdateLocationRunnable(locationManager, latLng);
                updateLocationRunnable.start();

                MyPlaces touchedPlace = new MyPlaces(String.format("Marker %1$d", ++marker), "",0.00,0.00, latLng, 65, 12, 0);
                addPlaceMarker(touchedPlace);
            }
        });*/
    }

    /**
     * If our place has a fence radius > 0 then add it to our monitored fences.
     *
     * @param place the place to take action on
     */
    private void addFence(MyPlaces place) {
        if (place.getFenceRadius() <= 0) {
            // Nothing to monitor
            return;
        }
        Geofence geofence = new Geofence.Builder()
                .setCircularRegion(place.getCoordinates().latitude, place.getCoordinates().longitude, place.getFenceRadius())
                .setRequestId(place.getTitle()) // every fence must have an ID
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT) // can also have DWELL
                .setExpirationDuration(Geofence.NEVER_EXPIRE) // how long do we care about this geofence?
                        //.setLoiteringDelay(60000) // 1 min.
                .build();
        myFences.add(geofence);
    }

    /**
     * Connect our GoogleApiClient so we can begin monitoring our fences.
     *
     * @param fences our list of Geofences to monitor
     */
    private void monitorFences(List<Geofence> fences) {
        if (fences.isEmpty()) {
            throw new RuntimeException("No fences to monitor. Call addPlaceMarker() First.");
        }
        // PRES 2
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        /*
            TODO
            1. Display a spinner in the progress bar while we're waiting for location
            2. When connected & not null update map position to location
            3. If location null try again once every 10 seconds until we get an answer or quit after x minutes
            4. ?
         */
        Toast.makeText(getActivity(), "GoogleApiClient Connected", Toast.LENGTH_SHORT).show();
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        String lastLocationMessage;
        if (lastLocation == null) {
            lastLocationMessage = "Last Location is NULL";
            //moveToLocation(home);
        } else {
            lastLocationMessage = String.format("Last Location (%1$s, %2$s)", lastLocation.getLatitude(), lastLocation.getLongitude());
            //moveToLocation(new MyPlaces("Last Location", "I am here.", lastLocation.getLatitude() , lastLocation.getLongitude() ,new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 0, 13, 0));
        }
        Toast.makeText(getActivity(), lastLocationMessage, Toast.LENGTH_SHORT).show();
        // PRES 3
        geofencePendingIntent = getRequestPendingIntent();
        PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(googleApiClient, myFences, geofencePendingIntent);
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "GoogleApiClient Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.e("connectionResult",connectionResult.getErrorMessage());
        Toast.makeText(getActivity(), "GoogleApiClient Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResult(Status status) {
        String toastMessage;
        // PRES 4
        if (status.isSuccess()) {
            toastMessage = "Success: We Are Monitoring Our Fences";
        } else {
            toastMessage = "Error: We Are NOT Monitoring Our Fences";
        }
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns the current PendingIntent to the caller.
     *
     * @return The PendingIntent used to create the current set of geofences
     */
    public PendingIntent getRequestPendingIntent() {
        return createRequestPendingIntent();
    }

    /**
     * Get a PendingIntent to send with the request to add Geofences. Location
     * Services issues the Intent inside this PendingIntent whenever a geofence
     * transition occurs for the current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence
     * transitions.
     */
    private PendingIntent createRequestPendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        } else {
            Intent intent = new Intent(getActivity(), GeofenceTransitionReceiver.class);
            intent.setAction("geofence_transition_action");
            return PendingIntent.getBroadcast(getActivity(), R.id.geofence_transition_intent, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }


    // ------------------------------------------------------------------------------------------- //



    /* -------------------------------------------------------------------------------------------------------- */

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
