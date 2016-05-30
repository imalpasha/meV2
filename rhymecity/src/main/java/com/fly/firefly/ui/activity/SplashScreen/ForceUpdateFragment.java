package com.fly.firefly.ui.activity.SplashScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ForceUpdateFragment extends BaseFragment{

    @Inject
    HomePresenter presenter;

    @InjectView(R.id.btnUpdate)
    Button btnUpdate;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    private DeviceInformation info;
    private Boolean running = false;
    private static SweetAlertDialog pDialog;

    public static ForceUpdateFragment newInstance() {

        ForceUpdateFragment fragment = new ForceUpdateFragment();
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

        View view = inflater.inflate(R.layout.force_update_screen, container, false);
        ButterKnife.inject(this, view);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.fly.firefly" +
                        "" +
                        ""));
                getActivity().startActivity(intent);
                System.exit(0);
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
