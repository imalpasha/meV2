package com.fly.firefly.ui.activity.SplashScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fly.firefly.FireFlyApplication;
import com.fly.firefly.R;
import com.fly.firefly.Controller;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeActivity;
import com.fly.firefly.ui.module.SplashScreenModule;
import com.fly.firefly.ui.object.DeviceInformation;
import com.fly.firefly.ui.presenter.HomePresenter;
import com.fly.firefly.utils.RealmObjectController;
import com.fly.firefly.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SplashScreenFragment extends BaseFragment implements HomePresenter.SplashScreen {

    @Inject
    HomePresenter presenter;
    private int fragmentContainerId;
    private SharedPrefManager pref;
    private DeviceInformation info;
    private Boolean running = false;
    private static SweetAlertDialog pDialog;

    public static SplashScreenFragment newInstance() {

        SplashScreenFragment fragment = new SplashScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireFlyApplication.get(getActivity()).createScopedGraph(new SplashScreenModule(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //RealmObjectController.deleteRealmFile(getActivity());

        View view = inflater.inflate(R.layout.splash_screen, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);

        //retrieve data
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        String version = android.os.Build.VERSION.RELEASE;
        int sdkVersion = android.os.Build.VERSION.SDK_INT;

        info = new DeviceInformation();
        info.setSdkVersion(Integer.toString(sdkVersion));
        info.setVersion(version);
        info.setDeviceId(deviceId);
        info.setBrand(Build.BRAND);
        info.setModel(Build.MODEL);
        info.setDataVersion("0");
        info.setSignature("");
        info.setUsername("");
        info.setPassword("");

        HashMap<String, String> initLogin = pref.getDataVesion();
        String localDataVersion = initLogin.get(SharedPrefManager.DATA_VERSION);

        if(localDataVersion == null){
           sendDeviceInformationToServer(info);
        }else if(localDataVersion != null && Controller.connectionAvailable(getActivity())){
           sendDeviceInformationToServer(info);
        }else if(localDataVersion != null && !Controller.connectionAvailable(getActivity())){
            goHomepage();
        }

        running = true;

        //RealmObjectController.deleteRealmFile(getActivity());

        return view;
    }



    public void sendDeviceInformationToServer(DeviceInformation info){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
        if(Controller.connectionAvailable(getActivity())){
            presenter.deviceInformation(info);
            Log.e("Internet", "Ok");
        }else{
            Log.e("Internet", "X");
            connectionRetry("No Internet Connection");
        }
    }

    public void connectionRetry(String msg){

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

      /*  new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setContentText(msg)
                .setConfirmText("Retry")
*/

    }

    @Override
    public void loadingSuccess(DeviceInfoSuccess obj) {

        Boolean status = Controller.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

            Log.e("Object",obj.toString());

            HashMap<String, String> initLogin = pref.getDataVesion();
            String localDataVersion = initLogin.get(SharedPrefManager.DATA_VERSION);
            String dataVersion = obj.getObj().getData_version();

            if (localDataVersion == null) {
                update(obj);
            }else{

                if(!localDataVersion.equals(dataVersion)){
                    update(obj);
                }else{
                    Log.e("No Update","True");
                }

            }


            }
            //Redirect to homepage after success loading splashscreen
            if (true) {
                //forceUpdate();
                goHomepage();

            }

    }

    public void update(DeviceInfoSuccess obj){

        String signature = obj.getObj().getSignature();
        String bannerUrl = obj.getObj().getBanner_default();
        String promoBannerUrl = obj.getObj().getBanner_promo();
        String bannerModule = obj.getObj().getBanner_module();
        String dataVersion = obj.getObj().getData_version();
        DeviceInfoSuccess.SocialMedia socialMediaObj = obj.getObj().getSocial_media();
        Log.e("Facebook", socialMediaObj.getFacebook());


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
        Log.e("socialMedia", socialMedia);
        pref.setSocialMedia(socialMedia);
                /*End*/

                /*Save Signature to local storage*/
        pref.setSignatureToLocalStorage(signature);
        pref.setBannerUrl(bannerUrl);
        pref.setPromoBannerUrl(promoBannerUrl);
        pref.setBannerModule(bannerModule);
        pref.setDataVersion(dataVersion);

    }

    public void goHomepage(){
        Intent home = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(home);
        getActivity().finish();
    }

    public void forceUpdate(){
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
        if(!running){
            sendDeviceInformationToServer(info);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        running = false;
    }
}
