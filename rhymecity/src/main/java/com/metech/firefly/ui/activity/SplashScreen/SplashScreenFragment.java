package com.metech.firefly.ui.activity.SplashScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.FireFlyApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.Controller;
import com.metech.firefly.api.obj.DeviceInfoSuccess;
import com.metech.firefly.api.obj.LoginReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.metech.firefly.ui.activity.Homepage.HomeActivity;
import com.metech.firefly.ui.module.SplashScreenModule;
import com.metech.firefly.ui.object.CachedResult;
import com.metech.firefly.ui.object.DeviceInformation;
import com.metech.firefly.ui.presenter.HomePresenter;
import com.metech.firefly.utils.App;
import com.metech.firefly.utils.RealmObjectController;
import com.metech.firefly.utils.SharedPrefManager;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

import static com.metech.firefly.ui.activity.PushNotification.CommonUtilities.SENDER_ID;

public class SplashScreenFragment extends BaseFragment implements HomePresenter.SplashScreen {

    @Inject
    HomePresenter presenter;
    private int fragmentContainerId;
    private SharedPrefManager pref;
    private DeviceInformation info;
    private Boolean running = false;
    private static SweetAlertDialog pDialog;
    private boolean proceed = false;
    private static Activity activity;
    private String localDataVersion;

    public static SplashScreenFragment newInstance() {

        SplashScreenFragment fragment = new SplashScreenFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SplashScreenModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Controller.setHomeStatus();

        View view = inflater.inflate(R.layout.splash_screen, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        activity = getActivity();

        //2JUN - COMMENT
        //String gcmKey = bundle.getString("GCM_KEY");

        String gcmKey = GCMRegistrar.getRegistrationId(getActivity());
        if (gcmKey.equals("")) {
            GCMRegistrar.register(getActivity(), SENDER_ID);
        } else {
            proceed = true;
        }

//        String gcmKey = "";
        //       proceed = true;

        if (proceed) {
            HashMap<String, String> initUserEmail = pref.getUserEmail();
            String userEmail = initUserEmail.get(SharedPrefManager.USER_EMAIL);

            HashMap<String, String> initUserPassword = pref.getUserPassword();
            String userPassword = initUserPassword.get(SharedPrefManager.PASSWORD);
            if (userEmail == null && userPassword == null) {
                userEmail = "";
                userPassword = "";
                pref.setLoginStatus("N");
            }
            //retrieve data
            String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            String version = android.os.Build.VERSION.RELEASE;
            int sdkVersion = android.os.Build.VERSION.SDK_INT;

            //HashMap<String, String> initLogin = pref.getDataVesion();
            //String localDataVersion = initLogin.get(SharedPrefManager.DATA_VERSION);
            //if(localDataVersion == null) {
            //     localDataVersion = "0.01";
            /// }

            localDataVersion = "0.01";

            info = new DeviceInformation();
            info.setSdkVersion(Integer.toString(sdkVersion));
            info.setVersion(version);
            info.setDeviceId(deviceId);
            info.setBrand(Build.BRAND);
            info.setModel(Build.MODEL);
            //info.setModel("test");
            info.setDataVersion("0");
            info.setSignature("");
            info.setUsername(userEmail);
            info.setPassword(userPassword);
            info.setGCMKey(gcmKey);

            Log.e("GCM KEY", gcmKey);

            if (localDataVersion == null && Controller.connectionAvailable(getActivity())) {
                sendDeviceInformationToServer(info);
                // pref.setAppVersion("0.10");
            } else if (localDataVersion == null && !Controller.connectionAvailable(getActivity())) {
                connectionRetry("No Internet Connection");
            } else if (localDataVersion != null && Controller.connectionAvailable(getActivity())) {
                sendDeviceInformationToServer(info);
            } else if (localDataVersion != null && !Controller.connectionAvailable(getActivity())) {

                HashMap<String, String> initApp = pref.getAppVersion();
                String localAppVersion = initApp.get(SharedPrefManager.APP_VERSION);
                if (localAppVersion == null) {
                    connectionRetry("No Internet Connection");
                } else {
                    goHomepage();
                }
            }

            running = true;
            //RealmObjectController.deleteRealmFile(getActivity());
        } else {
            //just wait ....
        }

        return view;
    }

    public static void splash(Context act, String regId) {
        Intent home = new Intent(act, SplashScreenActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(home);
    }

    public void sendDeviceInformationToServer(DeviceInformation info) {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (Controller.connectionAvailable(getActivity())) {
            presenter.deviceInformation(info);

        } else {
            connectionRetry("No Internet Connection");
        }
    }

    public void connectionRetry(String msg) {

        pDialog.setTitleText("Connection Error");
        pDialog.setCancelable(false);
        pDialog.setContentText(msg);
        pDialog.setConfirmText("Retry");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sendDeviceInformationToServer(info);
            }
        })
                .show();

    }

    @Override
    public void loadingSuccess(DeviceInfoSuccess obj) {

        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

            //forceLogout
            HashMap<String, String> initForceLogout = pref.getForceLogout();
            String forceLogout = initForceLogout.get(SharedPrefManager.FORCE_LOGOUT);

            if (forceLogout == null) {
                //if(!forceLogout.equals("Y")){
                pref.setLoginStatus("N");
                Controller.clearAll(getActivity());
                RealmObjectController.deleteRealmFile(MainFragmentActivity.getContext());
                pref.setForceLogout("Y");
                //}
            }

            //HashMap<String, String> initLogin = pref.getDataVesion();
            //String localDataVersion = initLogin.get(SharedPrefManager.DATA_VERSION);

            HashMap<String, String> initAppData = pref.getAppVersion();
            String localAppVersion = initAppData.get(SharedPrefManager.APP_VERSION);

            String dataVersion = obj.getObj().getData_version();
            String appVersion = obj.getObj().getData_version_mobile().getVersion();
            String updateStatus = obj.getObj().getData_version_mobile().getForce_update();

            // forceUpdate() update app if needed
            // update() - update data in app if needed
            //forceUpdate();

            //Check App Version. Update if needed
            if (!App.APP_VERSION.equals(appVersion) && updateStatus.equals("Y")) {
                forceUpdate();
            } else {
                if (localDataVersion == null) {
                    update(obj);
                } else if (localDataVersion != null && !localDataVersion.equals(dataVersion)) {
                    update(obj);
                } else {
                    goHomepage();
                }
            }

        }


    }

    public void update(DeviceInfoSuccess obj) {

        String signature = obj.getObj().getSignature();
        String bannerUrl = obj.getObj().getBanner_default();
        String promoBannerUrl = obj.getObj().getBanner_promo();
        String bannerModule = obj.getObj().getBanner_module();
        String dataVersion = obj.getObj().getData_version();
        String appVersion = obj.getObj().getData_version_mobile().getVersion();
        String bannerRedirectURL = obj.getObj().getBanner_redirect_url();

        DeviceInfoSuccess.SocialMedia socialMediaObj = obj.getObj().getSocial_media();

        /*Save All to pref for reference*/
        Gson gson = new Gson();
        String title = gson.toJson(obj.getObj().getData_title());
        pref.setUserTitle(title);

        String country = gson.toJson(obj.getObj().getData_country());
        pref.setCountry(country);

        String state = gson.toJson(obj.getObj().getData_state());
        pref.setState(state);

        String flight = gson.toJson(obj.getObj().getData_market());
        pref.setFlight(flight);

        String socialMedia = gson.toJson(socialMediaObj);
        pref.setSocialMedia(socialMedia);
        /*End*/

        /*Save Signature to local storage*/
        pref.setSignatureToLocalStorage(signature);
        pref.setBannerUrl(bannerUrl);
        pref.setPromoBannerUrl(promoBannerUrl);
        pref.setBannerModule(bannerModule);
        pref.setBannerRedirectURL(bannerRedirectURL);
        pref.setDataVersion(dataVersion);
        pref.setAppVersion(appVersion);

        //Check If app need update
        HashMap<String, String> initApp = pref.getAppVersion();
        String localAppVersion = initApp.get(SharedPrefManager.APP_VERSION);

        //thru homepage
        goHomepage();

    }

    public void goHomepage() {
        Controller.setHomeStatus();
        Intent home = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(home);
        getActivity().finish();
    }

    public void forceUpdate() {
        Intent home = new Intent(getActivity(), ForceUpdateActivity.class);
        getActivity().startActivity(home);
        getActivity().finish();
    }

    @Override
    public void onConnectionFailed() {
        connectionRetry("Unable to connect to server");
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        running = false;
    }

}
