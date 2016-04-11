package com.fly.firefly.ui.activity.MobileCheckIn;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;

import butterknife.ButterKnife;

public class MobileCheckInActivity3 extends MainFragmentActivity implements FragmentContainerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, MobileCheckInFragment3.newInstance(bundle)).commit();

        setMenuButton();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
