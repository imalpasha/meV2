package com.fly.firefly.ui.activity.Homepage;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.Beacon.BeaconRanging;
import com.fly.firefly.ui.activity.BoardingPass.BoardingPassActivity;
import com.fly.firefly.ui.activity.BookingFlight.SearchFlightActivity;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.GeoFencing.GenFencingActivity;
import com.fly.firefly.ui.activity.GeoFencing.GeoFencingFragment;
import com.fly.firefly.ui.activity.GeoFencing.GeofenceTransitionReceiver;
import com.fly.firefly.ui.activity.GeoFencing.MyPlaces;
import com.fly.firefly.ui.activity.Login.LoginActivity;
import com.fly.firefly.ui.activity.ManageFlight.ManageFlightActivity;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity1;
import com.fly.firefly.ui.activity.PushNotification.MainActivity;
import com.fly.firefly.ui.activity.PushNotification.PushNotificationActivity;
import com.fly.firefly.ui.activity.PushNotification.RegisterActivity;
import com.fly.firefly.ui.activity.SlidePage.NearKioskActivity;
import com.fly.firefly.ui.module.HomeModule;
import com.fly.firefly.ui.object.BoardingPassObj;
import com.fly.firefly.ui.presenter.HomePresenter;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;

//import com.estimote.sdk.BeaconManager;

public class HomeFragment extends BaseFragment implements HomePresenter.HomeView{

    // --------------------------------------------------------------------------------//



    // --------------------------------------------------------------------------------//
    private Tracker mTracker;

    @Inject
    HomePresenter presenter;

    @InjectView(R.id.homeBookFlight)
    LinearLayout bookFlight;

    @InjectView(R.id.homeMobileCheckIn)
    LinearLayout mobileCheckIn;

    //@InjectView(R.id.homeBeacon)
    //LinearLayout homeBeacon;

    @InjectView(R.id.homeManageFlight)
    LinearLayout homeManageFlight;

    @InjectView(R.id.homeMobileBoardingPass)
    LinearLayout homeMobileBoardingPass;

    @InjectView(R.id.bannerImg)
    ImageView bannerImg;

    private static final String SCREEN_LABEL = "Home";
    private String facebookUrl,twitterUrl,instagramUrl;
    private int fragmentContainerId;
    private SharedPrefManager pref;
    public static final String TAG = GeoFencingFragment.class.getSimpleName();
   /* private static final int NUMBER_OF_LOCATION_ITERATIONS = 10;
    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private MyPlaces happyPlace;
    private MyPlaces home;
    private List<Geofence> myFences = new ArrayList<>();
    private GoogleApiClient googleApiClient;
    private PendingIntent geofencePendingIntent;
    //private UpdateLocationRunnable updateLocationRunnable;
    private LocationManager locationManager;
    private int marker = 0;
    private Location lastLocation;*/
    //FragmentManager fm;

    public static HomeFragment newInstance() {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new HomeModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        ButterKnife.inject(this, view);
        aq.recycle(view);
        pref = new SharedPrefManager(getActivity());

        /*Realm Obj Test*/
//        Realm realm = Realm.getInstance(getActivity());
//        RealmResults<BoardingPassObj> result2 = realm.where(BoardingPassObj.class).findAll();
//        Log.e("Result",result2.toString());
        /* ------------ */

        /*GET PREF DATA*/
        HashMap<String, String> init = pref.getDefaultBanner();
        String defaultBanner = init.get(SharedPrefManager.DEFAULT_BANNER);
        aq.id(R.id.bannerImg).image(defaultBanner);

        HashMap<String, String> initSocialMedia = pref.getSocialMedia();
        String socialMedia = initSocialMedia.get(SharedPrefManager.SOCIAL_MEDIA);

        Gson gson = new Gson();
        DeviceInfoSuccess.SocialMedia socialMediaObj = gson.fromJson(socialMedia, DeviceInfoSuccess.SocialMedia.class);

        facebookUrl = socialMediaObj.getFacebook();
        twitterUrl = socialMediaObj.getTwitter();
        instagramUrl = socialMediaObj.getInstagram();

        Log.e("Facebook",facebookUrl);
        Log.e("Facebook",twitterUrl);
        Log.e("Facebook",instagramUrl);


        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        bookFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "bookflight");
                goBookingPage();
            }
        });
        //getSampleData();

        mobileCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                //AnalyticsApplication.sendEvent("Click", "mobileCheckIn");
                goToMobileCheckIn();
            }
        });

        homeManageFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "homeManageFlight");
                goToManageFlight();
            }
        });


        /*mobileFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBeacon();
            }
        });*/

        //homeBeacon.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        AnalyticsApplication.sendEvent("Click", "homeBeacon");
        //        goToBeacon();
        //    }
        //});

        homeMobileBoardingPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AnalyticsApplication.sendEvent("Click", "homeMobileBoardingPass");
                goToBoardingPass();
                //gotPushRegistration();
            }
        });

        //setUpMap();
        getScreenSize();

        return view;
    }


    public void getScreenSize(){

        int screenSize = getResources().getConfiguration().screenLayout &  Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
            Log.e("toastMsg",toastMsg);
    }

    // ------------------------------------------------------------------------------------------- //

    public void gotPushRegistration(){
        Intent loginPage = new Intent(getActivity(), PushNotificationActivity.class);
        getActivity().startActivity(loginPage);
    }

    /*Public-Inner Func*/
    public void goToLoginPage(){
       Intent loginPage = new Intent(getActivity(), LoginActivity.class);
       // Intent loginPage = new Intent(getActivity(), SensorActivity.class);
       //Intent loginPage = new Intent(getActivity(), Touch.class);
       // Intent loginPage = new Intent(getActivity(), RelativeFragment.class);
        getActivity().startActivity(loginPage);

    }

    public void goToManageFlight() {
        Intent loginPage = new Intent(getActivity(), ManageFlightActivity.class);
        getActivity().startActivity(loginPage);

    }

    /*Public-Inner Func*/
    public void goToMobileCheckIn() {
        Intent mcheckin = new Intent(getActivity(), MobileCheckInActivity1.class);
        getActivity().startActivity(mcheckin);
    }

    public void goToBeacon() {
        Intent loginPage = new Intent(getActivity(), BeaconRanging.class);
        getActivity().startActivity(loginPage);
    }

    public void goToBoardingPass() {
        //Intent loginPage = new Intent(getActivity(), BeaconRanging.class);
        Intent loginPage = new Intent(getActivity(), BoardingPassActivity.class);
        //Intent loginPage = new Intent(getActivity(), GenFencingActivity.class);
        getActivity().startActivity(loginPage);

    }

    public void goBookingPage() {
        Intent loginPage = new Intent(getActivity(), SearchFlightActivity.class);
        getActivity().startActivity(loginPage);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    private void getSampleData() {
        presenter.onRequestGoogleData();
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

    public void registerBackFunction() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("EXIT FIREFLY")
                .setContentText("Confirm exit?")
                .showCancelButton(true)
                .setCancelText("Cancel")
                .setConfirmText("Close")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        getActivity().finish();

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }





























}