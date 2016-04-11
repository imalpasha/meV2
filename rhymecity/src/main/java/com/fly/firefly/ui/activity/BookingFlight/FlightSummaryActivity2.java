package com.fly.firefly.ui.activity.BookingFlight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class FlightSummaryActivity2 extends MainFragmentActivity implements FragmentContainerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, FlightSummaryFragment.newInstance(),"FlightSummary").commit();

        setMenuButton();

    }


    @Override
    public void onBackPressed(){

        final FragmentManager manager = getSupportFragmentManager();
        FlightSummaryFragment fragment = (FlightSummaryFragment) manager.findFragmentByTag("FlightSummary");
        fragment.backButton();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_content;
    }
}
