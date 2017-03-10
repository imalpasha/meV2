package com.metech.firefly.ui.activity.Homepage;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.Controller;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.GCMIntentService;
import com.metech.firefly.R;
import com.metech.firefly.api.obj.DeviceInfoSuccess;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.Beacon.BeaconRanging;
import com.metech.firefly.ui.activity.BoardingPass.BoardingPassActivity;
import com.metech.firefly.ui.activity.BookingFlight.SearchFlightActivity;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.GeoFencing.GeoFencingFragment;
import com.metech.firefly.ui.activity.Login.LoginActivity;
import com.metech.firefly.ui.activity.ManageFlight.MF_Activity;
import com.metech.firefly.ui.activity.MobileCheckIn.MobileCheckInActivity1;
import com.metech.firefly.ui.activity.PushNotification.PushNotificationActivity;
import com.metech.firefly.ui.activity.SplashScreen.Pop2NotificationActivity;
import com.metech.firefly.ui.module.HomeModule;
import com.metech.firefly.ui.presenter.HomePresenter;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

//import com.estimote.sdk.BeaconManager;

public class HomeFragment extends BaseFragment implements HomePresenter.HomeView {

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

    @InjectView(R.id.facebookLink)
    LinearLayout fbLink;

    @InjectView(R.id.twitterLink)
    LinearLayout twtLink;

    @InjectView(R.id.instagramLink)
    LinearLayout igLink;

    @InjectView(R.id.bookFlightBtn)
    TextView bookFlightBtn;

    private String facebookUrl, twitterUrl, instagramUrl;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Home";

    private SharedPrefManager pref;
    public static final String TAG = GeoFencingFragment.class.getSimpleName();
    View view;

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
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        aq.recycle(view);

        setUserEmailForCrashLytics();

        HashMap<String, String> initPromoBanner = pref.getPromoBanner();
        String banner = initPromoBanner.get(SharedPrefManager.PROMO_BANNER);

        if (banner == null || banner == "") {
            HashMap<String, String> initDefaultBanner = pref.getDefaultBanner();
            banner = initDefaultBanner.get(SharedPrefManager.DEFAULT_BANNER);
        }

        //put in try catch
        try {
            aq.id(R.id.bannerImg).image(banner);
        } catch (Exception e) {
        }

        HashMap<String, String> initBannerModule = pref.getBannerModule();
        HashMap<String, String> initBannerURL = pref.getBannerRedirectURL();

        final String bannerModule = initBannerModule.get(SharedPrefManager.BANNER_MODULE);
        final String bannerRedirectURL = initBannerURL.get(SharedPrefManager.BANNER_REDIRECT_URL);

        bannerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bannerRedirectURL.equals("")) {
                    Controller.clickableBannerWithURL(getActivity(), bannerRedirectURL);
                } else if (!bannerModule.equals("")) {
                    Controller.clickableBanner(getActivity(), bannerModule);
                } else {
                    //No Action
                }
            }
        });

        HashMap<String, String> initSocialMedia = pref.getSocialMedia();
        String socialMedia = initSocialMedia.get(SharedPrefManager.SOCIAL_MEDIA);

        Gson gson = new Gson();
        DeviceInfoSuccess.SocialMedia socialMediaObj = gson.fromJson(socialMedia, DeviceInfoSuccess.SocialMedia.class);

        try {
            facebookUrl = socialMediaObj.getFacebook();
            twitterUrl = socialMediaObj.getTwitter();
            instagramUrl = socialMediaObj.getInstagram();
        } catch (Exception e) {

        }

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
                //trySetAlarm();
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

        //facebook link
        fbLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("fb://page/" + facebookUrl)));

                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.facebook.com/" + facebookUrl)));
                }
            }
        });

        //twitter link
        twtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name=" + twitterUrl)));

                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/" + twitterUrl)));
                }
            }
        });

        //instagram link
        igLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("http://instagram.com/_u/" + instagramUrl);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);

                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/" + instagramUrl)));
                }
            }
        });

        //setUpMap();
        //trySetAlarm();
        //LocalNotification.convert(getActivity());

        // screenSize();


        // forceCrash(view);
        return view;
    }

    public void setUserEmailForCrashLytics() {
        //set user email for crashlitics report
        HashMap<String, String> initLoginStatus = pref.getLoginStatus();
        String loginStatus = initLoginStatus.get(SharedPrefManager.ISLOGIN);
        if (loginStatus.equals("Y")) {
            HashMap<String, String> initEmail = pref.getUserEmail();
            String userEmail = initEmail.get(SharedPrefManager.USER_EMAIL);
            Crashlytics.setUserEmail(userEmail);
        } else {
            Crashlytics.setUserEmail("Anonymous");
        }
    }

    public void forceCrash() {
        throw new RuntimeException("This is a crash");
    }


    public void screenSize() {

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

    }


    public void getScreenSize() {


        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch (screenSize) {
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
    }

    // ------------------------------------------------------------------------------------------- //

    private void generateNotification(Activity context) {

        int requestID = (int) System.currentTimeMillis();


        Intent notificationIntent = new Intent(context, Pop2NotificationActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);

        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), requestID, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.last_minute);
        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.bigPicture(largeIcon);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        mBuilder
                .setContentText("Enjoy 20% off when you book your tickets via Firefly mobile on this weekend.")
                .setContentTitle(String.format("Firefly Weekend Deal"))
                .setSmallIcon(R.drawable.push_icon)
                .setColor(Color.argb(0x55, 0x00, 0x00, 0xff))
                //.setLargeIcon(largeIcon)
                .setStyle(notiStyle)
                .setTicker("Firefly Ticket 20% off !!");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setContentIntent(contentIntent);

        notificationManager.notify(1, mBuilder.build());

    }

    public void gotPushRegistration() {
        Intent loginPage = new Intent(getActivity(), PushNotificationActivity.class);
        getActivity().startActivity(loginPage);
    }

    /*Public-Inner Func*/
    public void goToLoginPage() {
        Intent loginPage = new Intent(getActivity(), LoginActivity.class);
        // Intent loginPage = new Intent(getActivity(), SensorActivity.class);
        //Intent loginPage = new Intent(getActivity(), Touch.class);
        // Intent loginPage = new Intent(getActivity(), RelativeFragment.class);
        getActivity().startActivity(loginPage);

    }

    public void goToManageFlight() {
        Intent loginPage = new Intent(getActivity(), MF_Activity.class);
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
        //Intent loginPage = new Intent(getActivity(), MainActivity.class);
        //Intent loginPage = new Intent(getActivity(), GenFencingActivity.class);
        //Intent loginPage = new Intent(getActivity(), AutoCamera.class);

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

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                        System.exit(0);

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
