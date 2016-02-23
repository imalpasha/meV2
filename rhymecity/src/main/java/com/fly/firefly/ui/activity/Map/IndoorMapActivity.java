package com.fly.firefly.ui.activity.Map;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.R;
import com.fly.firefly.ui.activity.FragmentContainerActivity;
//import com.fly.firefly.ui.fragment.Map.IndoorMapFragment;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class IndoorMapActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //@InjectView(R.id.btnLogin) Button btnLogin;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
       ///fragmentManager.beginTransaction().replace(R.id.main_activity_fragment_container, IndoorMapFragment.newInstance()).commit();
///
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
