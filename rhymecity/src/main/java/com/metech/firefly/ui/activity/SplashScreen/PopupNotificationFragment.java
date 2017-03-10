package com.metech.firefly.ui.activity.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.metech.firefly.Controller;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.base.BaseFragment;

import butterknife.ButterKnife;

public class PopupNotificationFragment extends BaseFragment {

    public static PopupNotificationFragment newInstance(Bundle bundle) {

        PopupNotificationFragment fragment = new PopupNotificationFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.splash_screen, container, false);
        ButterKnife.inject(this, view);

        MainFragmentActivity.setAppStatus("ready_for_notification");

        if (Controller.getHomeStatus()) {
            getActivity().finish();
        } else {

            Intent home = new Intent(getActivity(), TokenActivity.class);
            home.setAction("android.intent.action.MAIN");
            home.addCategory("android.intent.category.LAUNCHER");
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(home);
            getActivity().finish();

        }

        return view;
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
