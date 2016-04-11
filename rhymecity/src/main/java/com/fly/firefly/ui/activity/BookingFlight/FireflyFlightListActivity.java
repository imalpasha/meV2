package com.fly.firefly.ui.activity.BookingFlight;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.fly.firefly.AnalyticsApplication;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeFragment;
import com.fly.firefly.utils.RealmObjectController;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class FireflyFlightListActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //private FragmentManager fragmentManager;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, FireflyFlightListFragment.newInstance(bundle)).commit();

        hideTitle();
        setMenuButton();

    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Flight Details" + "A");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
