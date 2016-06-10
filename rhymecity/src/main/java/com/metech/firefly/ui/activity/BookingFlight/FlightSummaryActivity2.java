package com.metech.firefly.ui.activity.BookingFlight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.ui.activity.FragmentContainerActivity;

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
