package com.metech.firefly.ui.activity.GeoFencing;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.metech.firefly.R;
import com.metech.firefly.base.BaseFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import butterknife.InjectView;
import com.google.android.gms.maps.model.CircleOptions;
/**
 * Created by Dell on 9/9/2015.
 */

import java.util.ArrayList;
import java.util.List;

public class GeoFencingFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ResultCallback<Status> {

    @InjectView(R.id.ib_happy_place)
    ImageButton ib_happy_place;

    @InjectView(R.id.ib_home)
    ImageButton ib_home;

    @InjectView(R.id.ib_reset)
    ImageButton btnReset;

    @InjectView(R.id.mapview)
    MapView mapView2;

    public static final String TAG = GeoFencingFragment.class.getSimpleName();
    private static final long LOCATION_ITERATION_PAUSE_TIME = 1000;
    private static final int NUMBER_OF_LOCATION_ITERATIONS = 10;
    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private MyPlaces happyPlace;
    private MyPlaces home;
    private List<Geofence> myFences = new ArrayList<>();
    private GoogleApiClient googleApiClient;
    private PendingIntent geofencePendingIntent;
    //private UpdateLocationRunnable updateLocationRunnable;
    private LocationManager locationManager;
    private int marker = 0;
    private Location lastLocation;
    //FragmentManager fm;

    public static GeoFencingFragment newInstance() {
        GeoFencingFragment fragment = new GeoFencingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public GeoFencingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_map, container, false);
        aq.recycle(v);

        mapView2 = (MapView) v.findViewById(R.id.mapview);
        mapView2.onCreate(savedInstanceState);

        //fm = getActivity().getSupportFragmentManager();

        //ib_happy_place.setOnClickListener(this);
        //ib_home.setOnClickListener(this);



        //setUpMapIfNeeded();
        map = mapView2.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //map.setBuildingsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        MapsInitializer.initialize(getActivity());

        // Check if we were successful in obtaining the map.
        if (map != null) {
            setUpMap();
        }

       /* btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Resetting Our Map", Toast.LENGTH_SHORT).show();
                // if (updateLocationRunnable != null) {
                //     updateLocationRunnable.interrupt();
                // }
                googleApiClient.disconnect();
                map.clear();
                myFences.clear();
                setUpMap();
            }
        });*/


        return v;
    }

    @Override
    public void onClick(View v) {

        MyPlaces place;
        switch (v.getId()) {
            case R.id.ib_happy_place:
                Toast.makeText(getActivity(), "You Clicked Happy Place", Toast.LENGTH_SHORT).show();
                place = happyPlace;
                moveToLocation(place);
                break;
            case R.id.ib_home:
                Toast.makeText(getActivity(), "You Clicked Home", Toast.LENGTH_SHORT).show();
                place = home;
                moveToLocation(place);
                break;
            case R.id.ib_reset:
                Toast.makeText(getActivity(), "Resetting Our Map", Toast.LENGTH_SHORT).show();
               // if (updateLocationRunnable != null) {
               //     updateLocationRunnable.interrupt();
               // }
                googleApiClient.disconnect();
                map.clear();
                myFences.clear();
                setUpMap();
                break;
        }
    }

    @Override
    public void onPause() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Interrupt our runnable if we're going into the background or exiting
        //if (updateLocationRunnable != null) {
        //    updateLocationRunnable.interrupt();
        //}

        //Log.i(TAG, "Cleanup Our Fields");
        //locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        //locationManager.removeTestProvider(LocationManager.NETWORK_PROVIDER);
        //locationManager = null;
        //updateLocationRunnable = null;

        super.onPause();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume()
    {
        mapView2.onResume();
        super.onResume();
        //setUpMapIfNeeded();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Log.i(TAG, "Setup MOCK Location Providers");
        //locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Log.i(TAG, "GPS Provider");
        //locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, true, false, false, false, false, false, Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
        //locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

        Log.i(TAG, "Network Provider");
        //locationManager.addTestProvider(LocationManager.NETWORK_PROVIDER, true, false, true, false, false, false, false, Criteria.POWER_MEDIUM, Criteria.ACCURACY_FINE);
        //locationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */

    //private void setUpMapIfNeeded() {
    //    // Do a null check to confirm that we have not already instantiated the map.
    //    if (map == null) {
    //        // Try to obtain the map from the SupportMapFragment.
//
    //    }
   // }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
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
        happyPlace = new MyPlaces("Pier @ Folly Beach", "This is my Happy Place!",0.00,0.00, new LatLng(3.0139278, 101.3995878), 10000, 10, R.drawable.departure_icon);
        addPlaceMarker(happyPlace);
        addFence(happyPlace);

        // Add a place with a Geofence
        // Work 39.3336585, -84.3146718
        // Home 39.2697455, -84.269921
        home = new MyPlaces("Home", "This is where I live.",0.00,0.00, new LatLng(2.9243610, 101.6559994
        ), 100, 10, R.drawable.departure_icon);
        addPlaceMarker(home);
        addFence(home);

        // Add a place w/o a Geofence
        MyPlaces charleston = new MyPlaces("Charleston, SC", "This is where I want to live!",0.00,0.00, new LatLng(32.8210454, -79.9704779), 0, 10, R.drawable.departure_icon);
        addPlaceMarker(charleston);
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
     * Add a map marker at the place specified.
     *
     * @param place the place to take action on
     */
    private void addPlaceMarker(MyPlaces place) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place.getCoordinates())
                .title(place.getTitle());
        if (!TextUtils.isEmpty(place.getSnippet())) {
            markerOptions.snippet(place.getSnippet());
        }
        if (place.getIconResourceId() > 0) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(place.getIconResourceId()));
        }
        map.addMarker(markerOptions);
        drawGeofenceAroundTarget(place);
    }

    /**
     * If our place has a fence radius greater than 0 then draw a circle around it.
     *
     * @param place the place to take action on
     */
    private void drawGeofenceAroundTarget(MyPlaces place) {
        if (place.getFenceRadius() <= 0) {
            // Nothing to draw
            return;
        }
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(place.getCoordinates());
        circleOptions.fillColor(Color.argb(0x55, 0x00, 0x00, 0xff));
        circleOptions.strokeColor(Color.argb(0xaa, 0x00, 0x00, 0xff));
        circleOptions.radius(place.getFenceRadius());
        map.addCircle(circleOptions);
    }

    /**
     * Update our map's location to the place specified.
     *
     * @param place the place to take action on
     */
    private void moveToLocation(final MyPlaces place) {
        // Move the camera instantly to "place" with a zoom of 5.
        if (place.getTitle().equals("Charleston, SC")) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getCoordinates(), place.getDefaultZoomLevel()));
        }

        // Fly to our new location and then set the correct zoom level for the given place.
        //Current Location
        //Marker marker = googleMap.addMarker(new MarkerOptions()
        //        .position(new LatLng(Double.parseDouble(passLatitude), Double.parseDouble(passLongitude)))
        //        .title("Current Location")
        //        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        //marker.showInfoWindow();

        map.addMarker( new MarkerOptions()
                .position(new LatLng(place.getLat(),place.getLongi()))
                .title("latLong")
                .snippet("Radius: Not yet")).showInfoWindow();
        Log.e(Double.toString(place.getLat()), Double.toString(place.getLongi()));
        map.animateCamera(CameraUpdateFactory.newLatLng(place.getCoordinates()), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                map.animateCamera(CameraUpdateFactory.zoomTo(place.getDefaultZoomLevel()), 1000, null);
            }

            @Override
            public void onCancel() {
                // Nothing to see here.
            }
        });
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
            moveToLocation(home);
        } else {
            lastLocationMessage = String.format("Last Location (%1$s, %2$s)", lastLocation.getLatitude(), lastLocation.getLongitude());
            moveToLocation(new MyPlaces("Last Location", "I am here.", lastLocation.getLatitude() , lastLocation.getLongitude() ,new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 0, 13, 0));
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


    // /////////////////////////////////////////////////////////////////////////////////////////
    // // UpdateLocationRunnable                                                              //
    // /////////////////////////////////////////////////////////////////////////////////////////

   /* private Location createMockLocation(String locationProvider, double latitude, double longitude) {
        Location location = new Location(locationProvider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAccuracy(1.0f);
        location.setTime(System.currentTimeMillis());

            //setElapsedRealtimeNanos() was added in API 17

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        try {
            Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
            if (locationJellyBeanFixMethod != null) {
                locationJellyBeanFixMethod.invoke(location);
            }
        } catch (Exception e) {
            // There's no action to take here.  This is a fix for Jelly Bean and no reason to report a failure.
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location loc){
        Double lat = loc.getLatitude();
        Double longitude = loc.getLongitude();

        Log.e(Double.toString(lat),Double.toString(longitude));
    }

    @Override
    public void onProviderEnabled(String provider){
        //Double lat = loc.getLatitude();
        //Double longitude = loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider){
        //Double lat = loc.getLatitude();
        //Double longitude = loc.getLongitude();
    }

    @Override
    public void onProviderChanged(String provider){
        //Double lat = loc.getLatitude();
        //Double longitude = loc.getLongitude();
    }
    @Override
    public void onStatusChanged(String provider,int s,Bundle bundle){
        //Double lat = loc.getLatitude();
        //Double longitude = loc.getLongitude();
    }

*/



    // /////////////////////////////////////////////////////////////////////////////////////////
    // // CreateMockLocation                                                                  //
    // /////////////////////////////////////////////////////////////////////////////////////////

    /*class UpdateLocationRunnable extends Thread {

        private final LocationManager locMgr;
        private final LatLng latlng;
        Location mockGpsLocation;
        Location mockNetworkLocation;

        UpdateLocationRunnable(LocationManager locMgr, LatLng latlng) {
            this.locMgr = locMgr;
            this.latlng = latlng;
        }

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */



    /*
        @Override
        public void run() {
            try {
                Log.i(TAG, String.format("Setting Mock Location to: %1$s, %2$s", latlng.latitude, latlng.longitude));
                /*
                    Location can be finicky.  Iterate over our desired location every second for
                    NUMBER_OF_LOCATION_ITERATIONS seconds to help it figure out where we want it to
                    be.
                 */


    /*
                for (int i = 0; !isInterrupted() && i <= NUMBER_OF_LOCATION_ITERATIONS; i++) {
                    mockGpsLocation = createMockLocation(LocationManager.GPS_PROVIDER, latlng.latitude, latlng.longitude);
                    locMgr.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockGpsLocation);
                    mockNetworkLocation = createMockLocation(LocationManager.NETWORK_PROVIDER, latlng.latitude, latlng.longitude);
                    locMgr.setTestProviderLocation(LocationManager.NETWORK_PROVIDER, mockNetworkLocation);
                    Thread.sleep(LOCATION_ITERATION_PAUSE_TIME);
                }
            } catch (InterruptedException e) {
                Log.i(TAG, "Interrupted.");
                // Do nothing.  We expect this to happen when location is successfully updated.
            } finally {
                Log.i(TAG, "Done moving location.");
            }
        }
    }
*/
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView2.onDestroy();

    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView2.onLowMemory();

    }

}
