package com.metech.firefly.ui.activity.UpdateProfile;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.firefly.AnalyticsApplication;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.R;
import com.metech.firefly.ui.activity.FragmentContainerActivity;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;



public class UpdateProfileActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //@InjectView(R.id.btnLogin) Button btnLogin;
    private Tracker mTracker;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, UpdateProfileFragment.newInstance()).commit();

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]
        setMenuButton();


    }


    @Override
    public void onResume() {
        super.onResume();
       // presenter.onResume();

    }


    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
