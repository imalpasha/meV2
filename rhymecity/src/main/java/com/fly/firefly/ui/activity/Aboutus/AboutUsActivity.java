package com.fly.firefly.ui.activity.Aboutus;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
import com.fly.firefly.ui.activity.Homepage.HomeFragment;

import butterknife.ButterKnife;

public class AboutUsActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, AboutUsFragment.newInstance(),"About").commit();

        hideTitle();
        setMenuButton();

        //unlockDrawer();
        //BaseFragment.removeLogoHeader(this);
    }


    /*@Override
    public ProgressBar getProgressIndicator() {
        return progressIndicator;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }*/
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
