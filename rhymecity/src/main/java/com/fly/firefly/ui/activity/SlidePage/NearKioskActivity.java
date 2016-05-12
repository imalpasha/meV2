package com.fly.firefly.ui.activity.SlidePage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
//import android.view.WindowManager;

public class NearKioskActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, NearKioskFragment.newInstance()).commit();
        hideTitle();


    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }

    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
